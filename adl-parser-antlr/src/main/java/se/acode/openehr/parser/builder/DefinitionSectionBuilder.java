/*
 *   ***** BEGIN LICENSE BLOCK *****
 *   Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *   The contents of this file are subject to the Mozilla Public License Version
 *   1.1 (the 'License'); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *   http://www.mozilla.org/MPL/
 *
 *   Software distributed under the License is distributed on an 'AS IS' basis,
 *   WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *   for the specific language governing rights and limitations under the
 *   License.
 *
 *   Portions created by the Initial Developer are Copyright (C) 2016
 *   the Initial Developer. All Rights Reserved.
 *
 *   Contributor(s): Bert Verhees (bert.verhees@rosa.nl)
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *   ***** END LICENSE BLOCK *****
 *
 */

//TODO PATH Parent
package se.acode.openehr.parser.builder;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.openehr.am.archetype.assertion.*;
import org.openehr.am.archetype.constraintmodel.*;
import org.openehr.am.archetype.constraintmodel.primitive.*;
import org.openehr.am.openehrprofile.datatypes.quantity.CDvOrdinal;
import org.openehr.am.openehrprofile.datatypes.quantity.CDvQuantity;
import org.openehr.am.openehrprofile.datatypes.quantity.CDvQuantityItem;
import org.openehr.am.openehrprofile.datatypes.quantity.Ordinal;
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase;
import org.openehr.rm.datatypes.quantity.DvQuantity;
import org.openehr.rm.datatypes.quantity.datetime.DvDate;
import org.openehr.rm.datatypes.quantity.datetime.DvDateTime;
import org.openehr.rm.datatypes.quantity.datetime.DvDuration;
import org.openehr.rm.datatypes.quantity.datetime.DvTime;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.basic.Interval;
import org.openehr.rm.support.identification.TerminologyID;
import org.openehr.rm.support.measurement.MeasurementService;
import se.acode.openehr.parser.ArchetypeParser;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;

import java.util.*;

//java8 duration does not support years, weeks, etc

/**
 * Creates a definition object, starting with the ccomplexobject-context from the grammar.
 * Created by verhees on 9-1-16.
 */
public class DefinitionSectionBuilder {

    public static DefinitionSectionBuilder getInstance() {
        return new DefinitionSectionBuilder();
    }
    private ArchetypeADLErrorListener errorListener;

    // Starting point, the only public function
    public CComplexObject getDefinition(ArchetypeParser.Arch_definitionContext definitionSectionContext,
                                        MeasurementService measurementService, ArchetypeADLErrorListener errorListener)  {
        this.errorListener = errorListener;
        try {
            CComplexObject definition = c_complex_object(
                    new String("/"),
                    definitionSectionContext.c_complex_object(),
                    measurementService
            );
            //---------------------
            return definition;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(definitionSectionContext,  e.getMessage()));
        }
        return null;
    }

    private String getNodeId(TerminalNode terminalNode) {
        String nodeId = terminalNode.getText();
        if (nodeId.startsWith("["))
            nodeId = nodeId.substring(1);
        if (nodeId.endsWith("]"))
            nodeId = nodeId.substring(0, nodeId.length() - 1);
        return nodeId;
    }

    private CComplexObject c_complex_object(String path,
                                            ArchetypeParser.C_complex_objectContext cComplexObjectContext,
                                            MeasurementService measurementService) {
        List<CAttribute> attributes = null;
        if (cComplexObjectContext.c_complex_object_body() == null) {
            return null;
        }
        String nodeId = null;
        if (cComplexObjectContext.AT_CODE() != null) {
            nodeId = getNodeId(cComplexObjectContext.AT_CODE());
        }
        if ((nodeId != null) && (!("/".equals(path))))
            path = path + "[" + nodeId + "]";
        if (!(cComplexObjectContext.c_complex_object_body().c_attribute() == null) || (cComplexObjectContext.c_complex_object_body().c_attribute().size() == 0)) {
            attributes = attributes(path, cComplexObjectContext.c_complex_object_body().c_attribute(), measurementService);
        }
        String rmTypeName = cComplexObjectContext.rm_type_id().getText();
        Interval<Integer> occurrences = null;
        occurrences = occurrences(cComplexObjectContext.c_occurrences());
        CComplexObject cComplexObject = null;
        try {
            cComplexObject = new CComplexObject(
                    path,
                    rmTypeName,
                    occurrences,
                    nodeId,
                    attributes,
                    null
            );
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cComplexObjectContext,  e.getMessage()));
        }
        return cComplexObject;
    }

    private String extendPath(String path, String extension) {
        if (path.endsWith("/"))
            return path + extension;
        return path + "/" + extension;
    }

    private List<CAttribute> attributes(String path,
                                        List<ArchetypeParser.C_attributeContext> cAttributeContextList,
                                        MeasurementService measurementService) {
        if ((cAttributeContextList == null) || (cAttributeContextList.size() == 0))
            return null;
        List<CAttribute> result = new ArrayList<>();
        for (ArchetypeParser.C_attributeContext cAttributeContext : cAttributeContextList) {
            if (cAttributeContext.c_attr_value() != null) {
                String attributeName = cAttributeContext.ALPHA_LC_ID().getText();
                List<CObject> cObjects = children(extendPath(path, attributeName), cAttributeContext.c_attr_value(), measurementService);
                CAttribute.Existence existence = null;
                if ((cAttributeContext != null) && (cAttributeContext.c_existence() != null) && (cAttributeContext.c_existence().existence_spec() != null)) {
                    existence = existence(cAttributeContext.c_existence().existence_spec());
                }
                if (existence == null) {
                    existence = CAttribute.Existence.REQUIRED;
                }
                CAttribute ca;
                if (cAttributeContext.c_cardinality() != null) {
                    Cardinality cardinality = null;
                    if (cAttributeContext.c_cardinality().cardinality_spec() != null) {
                        cardinality = cardinality(cAttributeContext.c_cardinality().cardinality_spec());
                    }
                    ca = new CMultipleAttribute(
                            extendPath(path, attributeName),
                            attributeName,
                            existence,
                            cardinality,
                            cObjects);
                } else {
                    ca = new CSingleAttribute(
                            extendPath(path, attributeName),
                            attributeName,
                            existence,
                            cObjects
                    );
                }
                for (CObject cObject : cObjects) {
                    cObject.setParent(ca);  //PARENT SET TO ALL DESCENDANTS (AND CHILDREN) OF CATTRIBUTE
                }
                result.add(ca);
            }
        }
        if (result.size() == 0)
            return null;
        return result;
    }

    private CAttribute.Existence existence(ArchetypeParser.Existence_specContext existenceSpecContext) {
        Integer lower = -1, upper = -1;
        if (existenceSpecContext.INTEGER_VALUE().size() > 0) {
            lower = Integer.valueOf(existenceSpecContext.INTEGER_VALUE(0).getText());
            if (existenceSpecContext.INTEGER_VALUE().size() == 2) {
                upper = Integer.valueOf(existenceSpecContext.INTEGER_VALUE(1).getText());
            }
        }
        if (lower == 0) {
            if (upper < 1) {
                return CAttribute.Existence.NOT_ALLOWED;
            }
            if (upper == 1) {
                return CAttribute.Existence.OPTIONAL;
            }
        }
        if (lower == 1) {
            if ((upper == 1) || (upper == -1)) {
                return CAttribute.Existence.REQUIRED;
            }
        }
        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(existenceSpecContext,  "Illegal Existence encountered:" + existenceSpecContext.getPayload().getText()));
        return null;
    }

    private Interval<Integer> occurrences(ArchetypeParser.C_occurrencesContext occurrenceContext) {
        if (occurrenceContext == null) {
            return new Interval<>(1, 1);
        }
        return processMultiplicity(occurrenceContext.multiplicity_spec().getText());
    }

    private Interval<Integer> processMultiplicity(String multiplicity) {
        Integer lower = null, upper = null;
        if (multiplicity == null) {
            lower = 1;
            upper = 1;
        }
        if (multiplicity.contains("..")) {
            lower = new Integer(multiplicity.substring(0, multiplicity.indexOf("..")));
            if ("*".equals(multiplicity.substring(multiplicity.indexOf("..") + 2)))
                return new Interval<>(lower, null);
            upper = new Integer(multiplicity.substring(multiplicity.indexOf("..") + 2, multiplicity.length()));
            return new Interval<>(lower, upper);
        } else {
            if ("*".equals(multiplicity)) {
                return new Interval<>(new Integer(0), null);
            } else {
                return new Interval<>(new Integer(multiplicity), new Integer(multiplicity));
            }
        }
    }

    private Cardinality cardinality(ArchetypeParser.Cardinality_specContext cardinalityContext)  {
        if (cardinalityContext != null) {
            Boolean ordered = true, unique = false;
            try {
                if(cardinalityContext.multiplicity_mod()!=null) {
                    if (cardinalityContext.multiplicity_mod().size() > 2) {
                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cardinalityContext,  "Only two multiplicity-modes are allowed: (non-)unique and (un)ordered:" + cardinalityContext.getPayload().toString()));
                        return null;
                    }
                    for (ArchetypeParser.Multiplicity_modContext multiplicityModContext : cardinalityContext.multiplicity_mod()) {
                        if (multiplicityModContext.ordering_mod() != null) {
                            ordered = !"unordered".equals(multiplicityModContext.ordering_mod().getText().toLowerCase());
                        }
                        if (multiplicityModContext.unique_mod() != null) {
                            unique = !"non-unique".equals(multiplicityModContext.unique_mod().getText().toLowerCase());
                        }
                    }
                }
                Interval<Integer> multiplicityInterval = processMultiplicity(cardinalityContext.multiplicity_spec().getText());
                return new Cardinality(ordered, unique, multiplicityInterval);
            } catch (Exception e) {
                errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cardinalityContext, e.getMessage()));
            }
        }
        return null;
    }

    private List<CObject> children(String path,
                                   List<ArchetypeParser.C_attr_valueContext> c_attr_value,
                                   MeasurementService measurementService) {
        List<CObject> cObjects = new ArrayList<>();
        for (ArchetypeParser.C_attr_valueContext cAttrValueContext : c_attr_value) {
            ArchetypeParser.C_objectContext c_object = cAttrValueContext.c_object();
            if (c_object != null) {
                CObject cObject = null;
                if (c_object.archetype_internal_ref() != null) {
                    cObject = archetype_internal_ref(path, c_object.archetype_internal_ref());
                } else if (c_object.archetype_slot() != null) {
                    cObject = archetype_slot(path, c_object.archetype_slot());
                } else if (c_object.c_complex_object() != null) {
                    cObject = c_complex_object(path, c_object.c_complex_object(), measurementService);
                } else if (c_object.c_dv_quantity() != null) {
                    cObject = c_dv_quantity(path, c_object.c_dv_quantity(), measurementService);
                } else if (c_object.c_codephrase() != null) {
                    cObject = c_codephrase(path, c_object.c_codephrase());
                } else if (c_object.c_dv_ordinal() != null) {
                    cObject = c_dv_ordinal(path, c_object.c_dv_ordinal());
                } else if (c_object.c_primitive_object() != null) {
                    CPrimitive item = c_primitive(c_object.c_primitive_object().c_primitive());
                    cObject = c_primitive_object(path, c_object.c_primitive_object(), item);
                } else if (c_object.constraint_ref() != null) {
                    cObject = constraint_ref(path, c_object.constraint_ref());
                }
                if (cObject != null)
                    cObjects.add(cObject);
            }
        }
        return cObjects;
    }

    private ConstraintRef constraint_ref(String path,
                                         ArchetypeParser.Constraint_refContext constraint_ref) {
        String reference = constraint_ref.AC_CODE().getText();
        reference = reference.substring(1,reference.length()-1);
        return new ConstraintRef(path, "CODE_PHRASE", new Interval(1, 1),  null, null, reference);
    }

    private ArchetypeInternalRef archetype_internal_ref(String path,
                                                        ArchetypeParser.Archetype_internal_refContext internalRefContext) {
        Interval<Integer> occurrences = null;
        if (internalRefContext.c_occurrences() != null) {
            occurrences = occurrences(internalRefContext.c_occurrences());
        }
        if (occurrences == null) {
            occurrences = new Interval(1, 1);
        }
        String nodeId = null;
        if (internalRefContext.AT_CODE() != null)
            nodeId = getNodeId(internalRefContext.AT_CODE());
        if (nodeId != null)
            path = path + "[" + nodeId + "]";
        ArchetypeInternalRef archetypeInternalRef = new ArchetypeInternalRef(
                path,
                internalRefContext.rm_type_id().getText(),
                occurrences,
                nodeId,
                null,
                internalRefContext.adl_path().getText()
        );
        return archetypeInternalRef;
    }

    private CPrimitiveObject c_primitive_object(String path,
                                                ArchetypeParser.C_primitive_objectContext cPrimitiveObjectContext,
                                                CPrimitive item) {
        String nodeId = null;
        if (cPrimitiveObjectContext.AT_CODE() != null)
            nodeId = getNodeId(cPrimitiveObjectContext.AT_CODE());
        if (nodeId != null)
            path = path + "[" + nodeId + "]";
        Interval<Integer> occurrences = new Interval<Integer>(1, 1);
        CPrimitiveObject cPrimitiveObject = new CPrimitiveObject(
                path,
                occurrences,
                nodeId,
                null,
                item
        );
        return cPrimitiveObject;
    }

    private CDvOrdinal c_dv_ordinal(String path,
                                    ArchetypeParser.C_dv_ordinalContext cDvOrdinalContext) {
        Interval<Integer> occurrences = new Interval<Integer>(1, 1);
        int assumed = -1;
        Ordinal assumedValue = null;
        List<Ordinal> list = null;
        if (cDvOrdinalContext.ordinal() != null) {
            if (cDvOrdinalContext.ordinal().size() > 0) {
                list = new ArrayList<>();
                for (ArchetypeParser.OrdinalContext o : cDvOrdinalContext.ordinal()) {
                    Integer i = Integer.parseInt(o.INTEGER_VALUE().getText());
                    String terminology = o.TERMINOLOGY_ID_BLOCK().getText();
                    terminology = terminology.substring(0, terminology.length() - 2);
                    String cp = o.termcode().getText();
                    CodePhrase codePhrase = new CodePhrase( terminology, cp);
                    Ordinal ordinal = new Ordinal(i, codePhrase);
                    list.add(ordinal);
                }
            }
            if (cDvOrdinalContext.INTEGER_VALUE() != null) {
                assumed = Integer.parseInt(cDvOrdinalContext.INTEGER_VALUE().getText());
                if (list != null) {
                    for (Ordinal o : list) {
                        if (o.getValue() == assumed) {
                            assumedValue = o;
                            break;
                        }
                    }
                }
            }
            return new CDvOrdinal(path, occurrences, null, null, list, null, assumedValue);
        } else if (cDvOrdinalContext.SYM_C_DV_ORDINAL() != null) {
            return new CDvOrdinal(path, occurrences, null, null, null, null, null); //any allowed
        }
        return null;
    }

    private CDvQuantity c_dv_quantity(String path,
                                      ArchetypeParser.C_dv_quantityContext cDvQuantityContext,
                                      MeasurementService measurementService) {
        CodePhrase property = null;
        List<CDvQuantityItem> list = null;
        CDvQuantityItem item = null;
        Token t = null;
        DvQuantity defaultValue = null;
        DvQuantity assumedValue = null;
        Interval<Integer> occurrences = new Interval<Integer>(1, 1);
        for (ArchetypeParser.C_dv_quantity_main_itemsContext cDvQuantityMainItemsContext : cDvQuantityContext.c_dv_quantity_main_items()) {
            if (cDvQuantityMainItemsContext.property() != null) {
                if (cDvQuantityMainItemsContext.property().TERM_CODE_REF() != null) {
                    if (property != null)
                        errorListener.getParserErrors().addError("Property may not occur more then once in a c_dv_quantity set.");
                    String cp = cDvQuantityMainItemsContext.property().TERM_CODE_REF().getText();
                    property = BuilderUtils.returnCodePhraseFromTermCodeRefString(cp, errorListener);
                }
            }
            if (cDvQuantityMainItemsContext.c_dv_quantity_list() != null) {
                if (cDvQuantityMainItemsContext.c_dv_quantity_list() != null) {
                    if (list != null)
                        errorListener.getParserErrors().addError("List may not occur more then once in a c_dv_quantity set.");
                    if (cDvQuantityMainItemsContext.c_dv_quantity_list().c_dv_quantity_list_item().size() > 0) {
                        list = new ArrayList<>();
                        for (ArchetypeParser.C_dv_quantity_list_itemContext cDvQuantityListItemContext : cDvQuantityMainItemsContext.c_dv_quantity_list().c_dv_quantity_list_item()) {
                            if (cDvQuantityListItemContext.c_dv_quantity_list_item_item() != null) {
                                Interval<Double> magnitude = null;
                                Interval<Integer> precision = null;
                                String units = null;
                                for (ArchetypeParser.C_dv_quantity_list_item_itemContext cDvQuantityListItemItemContext : cDvQuantityListItemContext.c_dv_quantity_list_item_item()) {
                                    if (cDvQuantityListItemItemContext.magnitude() != null) {
                                        if (cDvQuantityListItemItemContext.magnitude().real_interval_value() != null) {
                                            if (magnitude != null)
                                                errorListener.getParserErrors().addError("Magnitude may not occur more then once in a dv_quantity set.");
                                            magnitude = processRealInterval(cDvQuantityListItemItemContext.magnitude().real_interval_value());
                                        }
                                    }
                                    if (cDvQuantityListItemItemContext.precision() != null) {
                                        if (cDvQuantityListItemItemContext.precision().integer_interval_value() != null) {
                                            if (precision != null)
                                                errorListener.getParserErrors().addError("Precision may not occur more then once in a dv_quantity set.");
                                            precision = processIntegerInterval(cDvQuantityListItemItemContext.precision().integer_interval_value());
                                        }
                                    }
                                    if (cDvQuantityListItemItemContext.units() != null) {
                                        if (cDvQuantityListItemItemContext.units().string_value() != null) {
                                            if (units != null)
                                                errorListener.getParserErrors().addError("Units may not occur more then once in a dv_quantity set.");
                                            units = cDvQuantityListItemItemContext.units().string_value().getText();
                                            units = units.substring(1, units.length() - 1);
                                        }
                                    }
                                }
                                item = new CDvQuantityItem(magnitude, precision, units);
                                list.add(item);
                            }
                        }
                    }
                }
            }
            if (cDvQuantityMainItemsContext.c_dv_quantity_assumed_value() != null) {
                Double magnitude = null;
                Integer precision = null;
                String units = null;
                if (cDvQuantityMainItemsContext.c_dv_quantity_assumed_value().dv_quantity_assumed() != null) {
                    for (ArchetypeParser.Dv_quantity_assumedContext dvQuantityAssumedContext : cDvQuantityMainItemsContext.c_dv_quantity_assumed_value().dv_quantity_assumed()) {
                        if (dvQuantityAssumedContext.magnitude_assumed() != null) {
                            if (magnitude != null)
                                errorListener.getParserErrors().addError("Magnitude may not occur more then once in a assumed set.");
                            magnitude = new Double(dvQuantityAssumedContext.magnitude_assumed().REAL_VALUE().getText());
                        }
                        if (dvQuantityAssumedContext.precision_assumed() != null) {
                            if (precision != null)
                                errorListener.getParserErrors().addError("Precision may not occur more then once in a assumed set.");
                            precision = new Integer(dvQuantityAssumedContext.precision_assumed().INTEGER_VALUE().getText());
                        }
                        if (dvQuantityAssumedContext.units() != null) {
                            if (units != null)
                                errorListener.getParserErrors().addError("Units may not occur more then once in a assumed set.");
                            units = dvQuantityAssumedContext.units().string_value().getText();
                            units = units.substring(1, units.length() - 1);
                        }
                    }
                    assumedValue = new DvQuantity(units, magnitude, precision, measurementService);
                }
            }
        }
        return new CDvQuantity(path, occurrences, null, null, list, property, null, assumedValue);
    }

    private CCodePhrase c_codephrase(String path,
                                     ArchetypeParser.C_codephraseContext cCodephraseContext) {
        String terminology;
        TerminologyID terminologyId = null;
        List<String> codeList = null;
        String assumed = null;
        CodePhrase assumedValue = null;
        CodePhrase defaultValue = null; // not used
        CodePhrase singleValue = null;
        Interval<Integer> occurrences = new Interval<Integer>(1, 1);
        codeList = null;
        if (cCodephraseContext.TERM_CODE_REF() != null) {
            codeList = new ArrayList<>();
            singleValue = BuilderUtils.returnCodePhraseFromTermCodeRefString(cCodephraseContext.TERM_CODE_REF().getText(), errorListener);
            codeList.add(singleValue.getCodeString());
            terminologyId = singleValue.getTerminologyId();
            return new CCodePhrase(path, occurrences, null, null, terminologyId, codeList, defaultValue, assumedValue);
        }
        if (cCodephraseContext.TERMINOLOGY_ID_BLOCK() != null) {
            terminology = cCodephraseContext.TERMINOLOGY_ID_BLOCK().getText();
            terminology = terminology.substring(0, terminology.length() - 2);
            terminologyId = new TerminologyID(terminology);
        }
        if (cCodephraseContext.termcode() != null) {
            if (cCodephraseContext.termcode().size() > 0) {
                codeList = new ArrayList<>();
                for (ArchetypeParser.TermcodeContext tc : cCodephraseContext.termcode()) {
                    codeList.add(tc.getText());
                }
            }
        }
        if (cCodephraseContext.assumed_code() != null) {
            assumed = cCodephraseContext.assumed_code().termcode().getText();
            assumedValue = new CodePhrase(terminologyId, assumed);
        }
        if (cCodephraseContext.TERM_CODE_REF() != null) {
            codeList = new ArrayList<>();
            String cp = cCodephraseContext.TERM_CODE_REF().getText();
            terminology = cp.substring(0, cp.indexOf("::"));
            terminologyId = new TerminologyID(terminology);
            String codeString = cp.substring(cp.indexOf("::") + 2, cp.length());
            codeList.add(codeString);
        }
        return new CCodePhrase(path, occurrences, null, null, terminologyId, codeList, defaultValue, assumedValue);
    }

    private CPrimitive c_primitive(ArchetypeParser.C_primitiveContext primitiveContext) {
        if (primitiveContext.c_integer() != null) {
            return c_integer(primitiveContext.c_integer());
        } else if (primitiveContext.c_real() != null) {
            return c_real(primitiveContext.c_real());
        } else if (primitiveContext.c_date() != null) {
            return c_date(primitiveContext.c_date());
        } else if (primitiveContext.c_time() != null) {
            return c_time(primitiveContext.c_time());
        } else if (primitiveContext.c_date_time() != null) {
            return c_date_time(primitiveContext.c_date_time());
        } else if (primitiveContext.c_duration() != null) {
            return c_duration(primitiveContext.c_duration());
        } else if (primitiveContext.c_string() != null) {
            return c_string(primitiveContext.c_string());
        } else if (primitiveContext.c_boolean() != null) {
            return c_boolean(primitiveContext.c_boolean());
        }
        return null;
    }

    private CInteger c_integer(ArchetypeParser.C_integerContext cIntegerContext)  {
        Interval<Integer> interval = null;
        Integer assumed = null;
        List<Integer> list = null;
        try {
            if (cIntegerContext.c_integer_spec().integer_assumed() != null)
                assumed = new Integer(cIntegerContext.c_integer_spec().integer_assumed().INTEGER_VALUE().getText());
            if (cIntegerContext.c_integer_spec().integer_interval_value() != null) {
                interval = processIntegerInterval(cIntegerContext.c_integer_spec().integer_interval_value());
            } else if (cIntegerContext.c_integer_spec().integer_list_value() != null) {
                list = new ArrayList<>();
                for (TerminalNode t : cIntegerContext.c_integer_spec().integer_list_value().INTEGER_VALUE()) {
                    String integerString = t.getText();
                    list.add(new Integer(integerString));
                }
            }else if (cIntegerContext.c_integer_spec().INTEGER_VALUE()!=null){
                list = new ArrayList<>();
                list.add(new Integer(cIntegerContext.c_integer_spec().INTEGER_VALUE().getText()));
            }
            return new CInteger(interval, list, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cIntegerContext,  e.getMessage()));
            return null;
        }
    }

    private Interval<Double> processRealInterval(ArchetypeParser.Real_interval_valueContext real_interval_value) {
        Double lower = null;
        Double upper = null;
        boolean lowerIncluded = true;
        boolean upperIncluded = true;
        if (real_interval_value.real_interval() != null) {
            if (real_interval_value.real_interval().SYM_INTERVAL_SEP() != null) {
                lower = new Double(real_interval_value.real_interval().REAL_VALUE(0).getText());
                upper = new Double(real_interval_value.real_interval().REAL_VALUE(1).getText());
                if (real_interval_value.real_interval().SYM_GT() != null)
                    lowerIncluded = false;
                if (real_interval_value.real_interval().SYM_LT() != null)
                    upperIncluded = false;
            }
        }
        if (real_interval_value.REAL_VALUE() != null) {
            if (real_interval_value.relop() != null) {
                if (real_interval_value.relop().SYM_LT() != null) {
                    upper = new Double(real_interval_value.REAL_VALUE().getText());
                    lowerIncluded = false;
                    upperIncluded = false;
                } else if (real_interval_value.relop().SYM_LE() != null) {
                    upper = new Double(real_interval_value.REAL_VALUE().getText());
                    lowerIncluded = false;
                } else if (real_interval_value.relop().SYM_GT() != null) {
                    lower = new Double(real_interval_value.REAL_VALUE().getText());
                    lowerIncluded = false;
                    upperIncluded = false;
                } else if (real_interval_value.relop().SYM_GE() != null) {
                    lower = new Double(real_interval_value.REAL_VALUE().getText());
                    upperIncluded = false;
                }
            } else {
                upper = new Double(real_interval_value.REAL_VALUE().getText());
                lower = new Double(real_interval_value.REAL_VALUE().getText());
            }
        }
        return new Interval<>(lower, upper, lowerIncluded, upperIncluded);
    }

    private Interval<Integer> processIntegerInterval(ArchetypeParser.Integer_interval_valueContext integer_interval_value) {
        Integer lower = null;
        Integer upper = null;
        boolean lowerIncluded = true;
        boolean upperIncluded = true;
        if (integer_interval_value.integer_interval() != null) {
            if (integer_interval_value.integer_interval().SYM_INTERVAL_SEP() != null) {
                lower = new Integer(integer_interval_value.integer_interval().INTEGER_VALUE(0).getText());
                upper = new Integer(integer_interval_value.integer_interval().INTEGER_VALUE(1).getText());
                if (integer_interval_value.integer_interval().SYM_GT() != null)
                    lowerIncluded = false;
                if (integer_interval_value.integer_interval().SYM_LT() != null)
                    upperIncluded = false;
            }
        }
        if (integer_interval_value.INTEGER_VALUE() != null) {
            if (integer_interval_value.relop() != null) {
                if (integer_interval_value.relop().SYM_LT() != null) {
                    upper = new Integer(integer_interval_value.INTEGER_VALUE().getText());
                    lowerIncluded = false;
                    upperIncluded = false;
                } else if (integer_interval_value.relop().SYM_LE() != null) {
                    upper = new Integer(integer_interval_value.INTEGER_VALUE().getText());
                    lowerIncluded = false;
                } else if (integer_interval_value.relop().SYM_GT() != null) {
                    lower = new Integer(integer_interval_value.INTEGER_VALUE().getText());
                    lowerIncluded = false;
                    upperIncluded = false;
                } else if (integer_interval_value.relop().SYM_GE() != null) {
                    lower = new Integer(integer_interval_value.INTEGER_VALUE().getText());
                    upperIncluded = false;
                }
            } else {
                upper = new Integer(integer_interval_value.INTEGER_VALUE().getText());
                lower = new Integer(integer_interval_value.INTEGER_VALUE().getText());
            }
        }
        return new Interval<>(lower, upper, lowerIncluded, upperIncluded);
    }

    private CReal c_real(ArchetypeParser.C_realContext cRealContext)  {
        Interval<Double> interval = null;
        Double assumed = null;
        List<Double> list = null;
        try {
            if (cRealContext.c_real_spec().real_assumed() != null)
                assumed = new Double(cRealContext.c_real_spec().real_assumed().REAL_VALUE().getText());
            if (cRealContext.c_real_spec().real_interval_value() != null) {
                interval = processRealInterval(cRealContext.c_real_spec().real_interval_value());
            } else if (cRealContext.c_real_spec().real_list_value() != null) {
                list = new ArrayList<>();
                for (TerminalNode t : cRealContext.c_real_spec().real_list_value().REAL_VALUE()) {
                    String realString = t.getText();
                    list.add(new Double(realString));
                }
            }else if (cRealContext.c_real_spec().REAL_VALUE()!=null){
                list = new ArrayList<>();
                list.add(new Double(cRealContext.c_real_spec().REAL_VALUE().getText()));
            }
            return new CReal(interval, list, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cRealContext,  e.getMessage()));
            return null;
        }
    }

    private CDate c_date(ArchetypeParser.C_dateContext cDateContext)  {
        String pattern = null;
        Interval<DvDate> interval = null;
        DvDate assumed = null;
        List<DvDate> list = null;
        try {
            if (cDateContext.c_date_spec().date_assumed() != null)
                assumed = new DvDate(cDateContext.c_date_spec().date_assumed().DATE_VALUE().getText());
            if (cDateContext.c_date_spec().date_interval_value() != null) {
                DvDate lower = null;
                DvDate upper = null;
                boolean lowerIncluded = true;
                boolean upperIncluded = true;
                if (cDateContext.c_date_spec().date_interval_value().date_interval() != null) {
                    if (cDateContext.c_date_spec().date_interval_value().date_interval().SYM_INTERVAL_SEP() != null) {
                        lower = new DvDate(cDateContext.c_date_spec().date_interval_value().date_interval().DATE_VALUE(0).getText());
                        upper = new DvDate(cDateContext.c_date_spec().date_interval_value().date_interval().DATE_VALUE(1).getText());
                        if (cDateContext.c_date_spec().date_interval_value().date_interval().SYM_GT() != null)
                            lowerIncluded = false;
                        if (cDateContext.c_date_spec().date_interval_value().date_interval().SYM_LT() != null)
                            upperIncluded = false;
                    }
                }
                if (cDateContext.c_date_spec().date_interval_value().DATE_VALUE() != null) {
                    if (cDateContext.c_date_spec().date_interval_value().relop() != null) {
                        if (cDateContext.c_date_spec().date_interval_value().relop().SYM_LT() != null) {
                            upper = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cDateContext.c_date_spec().date_interval_value().relop().SYM_LE() != null) {
                            upper = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                            lowerIncluded = false;
                        } else if (cDateContext.c_date_spec().date_interval_value().relop().SYM_GT() != null) {
                            lower = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cDateContext.c_date_spec().date_interval_value().relop().SYM_GE() != null) {
                            lower = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                            upperIncluded = false;
                        }
                    } else {
                        upper = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                        lower = new DvDate(cDateContext.c_date_spec().date_interval_value().DATE_VALUE().getText());
                    }
                }
                interval = new Interval<>(lower, upper, lowerIncluded, upperIncluded);
            }
            if (cDateContext.c_date_spec().date_list_value() != null) {
                list = new ArrayList<>();
                for (TerminalNode t : cDateContext.c_date_spec().date_list_value().DATE_VALUE()) {
                    String dateTime = t.getText();
                    list.add(new DvDate(dateTime));
                }
            }
            if (cDateContext.c_date_spec().date_pattern() != null) {
                pattern = cDateContext.c_date_spec().date_pattern().ISO8601_DATE_CONSTRAINT_PATTERN().getText();
            }else if (cDateContext.c_date_spec().DATE_VALUE()!=null){
                list = new ArrayList<>();
                list.add(new DvDate(cDateContext.c_date_spec().DATE_VALUE().getText()));
            }
            return new CDate(pattern, interval, list, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cDateContext,  e.getMessage()));
            return null;
        }
    }

    /**
     * A decimal mark, either a comma or a dot (without any preference as stated in resolution 10 of the 22nd General Conference CGPM in 2003,[15] but with a preference for a
     * comma according to ISO 8601:2004)[16] is used as a separator between the time element and its fraction.
     * @param cTimeContext
     * @return
     */
    private CTime c_time(ArchetypeParser.C_timeContext cTimeContext)  {
        String pattern = null;
        Interval<DvTime> interval = null;
        DvTime assumed = null;
        List<DvTime> list = null;
        try {
            if (cTimeContext.c_time_spec().time_assumed() != null)
                assumed = new DvTime(cTimeContext.c_time_spec().time_assumed().TIME_VALUE().getText());
            if (cTimeContext.c_time_spec().time_interval_value() != null) {
                DvTime lower = null;
                DvTime upper = null;
                boolean lowerIncluded = true;
                boolean upperIncluded = true;
                if (cTimeContext.c_time_spec().time_interval_value().time_interval() != null) {
                    if (cTimeContext.c_time_spec().time_interval_value().time_interval().SYM_INTERVAL_SEP() != null) {
                        lower = new DvTime(cTimeContext.c_time_spec().time_interval_value().time_interval().TIME_VALUE(0).getText());
                        upper = new DvTime(cTimeContext.c_time_spec().time_interval_value().time_interval().TIME_VALUE(1).getText());
                        if (cTimeContext.c_time_spec().time_interval_value().time_interval().SYM_GT() != null)
                            lowerIncluded = false;
                        if (cTimeContext.c_time_spec().time_interval_value().time_interval().SYM_LT() != null)
                            upperIncluded = false;
                    } 
                }
                if (cTimeContext.c_time_spec().time_interval_value().TIME_VALUE() != null) {
                    if (cTimeContext.c_time_spec().time_interval_value().relop() != null) {
                        if (cTimeContext.c_time_spec().time_interval_value().relop().SYM_LT() != null) {
                            upper = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cTimeContext.c_time_spec().time_interval_value().relop().SYM_LE() != null) {
                            upper = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                            lowerIncluded = false;
                        } else if (cTimeContext.c_time_spec().time_interval_value().relop().SYM_GT() != null) {
                            lower = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cTimeContext.c_time_spec().time_interval_value().relop().SYM_GE() != null) {
                            lower = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                            upperIncluded = false;
                        }
                    } else {
                        upper = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                        lower = new DvTime(cTimeContext.c_time_spec().time_interval_value().TIME_VALUE().getText());
                    }
                }
                interval = new Interval<>(lower, upper, lowerIncluded, upperIncluded);
            }
            if (cTimeContext.c_time_spec().time_list_value() != null) {
                list = new ArrayList<>();
                for (TerminalNode t : cTimeContext.c_time_spec().time_list_value().TIME_VALUE()) {
                    String dateTime = t.getText();
                    list.add(new DvTime(dateTime));
                }
            }
            if (cTimeContext.c_time_spec().time_pattern() != null) {
                pattern = cTimeContext.c_time_spec().time_pattern().ISO8601_TIME_CONSTRAINT_PATTERN().getText();
            }else if (cTimeContext.c_time_spec().TIME_VALUE()!=null){
                list = new ArrayList<>();
                list.add(new DvTime(cTimeContext.c_time_spec().TIME_VALUE().getText()));
            }
            return new CTime(pattern, interval, list, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cTimeContext,  e.getMessage()));
        }
        return null;
    }

    private CDateTime c_date_time(ArchetypeParser.C_date_timeContext cDateTimeContext)  {
        String pattern = null;
        Interval<DvDateTime> interval = null;
        DvDateTime assumed = null;
        List<DvDateTime> list = null;
        try {
            if (cDateTimeContext.c_date_time_spec().date_time_assumed() != null)
                assumed = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_assumed().DATE_TIME_VALUE().getText());
            if (cDateTimeContext.c_date_time_spec().date_time_interval_value() != null) {
                DvDateTime lower = null;
                DvDateTime upper = null;
                boolean lowerIncluded = true;
                boolean upperIncluded = true;
                if (cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval() != null) {
                    if (cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval().SYM_INTERVAL_SEP() != null) {
                        lower = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval().DATE_TIME_VALUE(0).getText());
                        upper = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval().DATE_TIME_VALUE(1).getText());
                        if (cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval().SYM_GT() != null)
                            lowerIncluded = false;
                        if (cDateTimeContext.c_date_time_spec().date_time_interval_value().date_time_interval().SYM_LT() != null)
                            upperIncluded = false;
                    }
                }
                if (cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE() != null) {
                    if (cDateTimeContext.c_date_time_spec().date_time_interval_value().relop() != null) {
                        if (cDateTimeContext.c_date_time_spec().date_time_interval_value().relop().SYM_LT() != null) {
                            upper = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cDateTimeContext.c_date_time_spec().date_time_interval_value().relop().SYM_LE() != null) {
                            upper = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                            lowerIncluded = false;
                        } else if (cDateTimeContext.c_date_time_spec().date_time_interval_value().relop().SYM_GT() != null) {
                            lower = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                            lowerIncluded = false;
                            upperIncluded = false;
                        } else if (cDateTimeContext.c_date_time_spec().date_time_interval_value().relop().SYM_GE() != null) {
                            lower = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                            upperIncluded = false;
                        }
                    } else {
                        upper = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                        lower = new DvDateTime(cDateTimeContext.c_date_time_spec().date_time_interval_value().DATE_TIME_VALUE().getText());
                    }
                }
                interval = new Interval<>(lower, upper, lowerIncluded, upperIncluded);
            }
            if (cDateTimeContext.c_date_time_spec().date_time_list_value() != null) {
                list = new ArrayList<>();
                for (TerminalNode t : cDateTimeContext.c_date_time_spec().date_time_list_value().DATE_TIME_VALUE()) {
                    String dateTime = t.getText();
                    list.add(new DvDateTime(dateTime));
                }
            }
            if (cDateTimeContext.c_date_time_spec().date_time_pattern() != null) {
                pattern = cDateTimeContext.c_date_time_spec().date_time_pattern().ISO8601_DATE_TIME_CONSTRAINT_PATTERN().getText();
            }else if (cDateTimeContext.c_date_time_spec().DATE_TIME_VALUE()!=null){
                list = new ArrayList<>();
                list.add(new DvDateTime(cDateTimeContext.c_date_time_spec().DATE_TIME_VALUE().getText()));
            }
            return new CDateTime(pattern, interval, list, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cDateTimeContext,  e.getMessage()));
            return null;
        }
    }

    private CDuration c_duration(ArchetypeParser.C_durationContext cDurationContext)  {
        DvDuration value = null;
        Interval<DvDuration> interval = null;
        DvDuration assumed = null;
        String pattern = null;
        try {
            if (cDurationContext.duration_assumed() != null)
                assumed = new DvDuration(cDurationContext.duration_assumed().DURATION_VALUE().getText());
            if (cDurationContext.c_duration_spec() != null) {
                for (ArchetypeParser.C_duration_specContext cDurationSpecContext : cDurationContext.c_duration_spec()) {
                    if (cDurationSpecContext.duration_interval_value() != null) {
                        DvDuration lower = null;
                        DvDuration upper = null;
                        boolean lowerIncluded = true;
                        boolean upperIncluded = true;
                        if (cDurationSpecContext.duration_interval_value().duration_interval() != null) {
                            if (cDurationSpecContext.duration_interval_value().duration_interval().SYM_INTERVAL_SEP() != null) {
                                lower = DvDuration.getInstance(cDurationSpecContext.duration_interval_value().duration_interval().DURATION_VALUE(0).getText());
                                upper = DvDuration.getInstance(cDurationSpecContext.duration_interval_value().duration_interval().DURATION_VALUE(1).getText());
                                if (cDurationSpecContext.duration_interval_value().duration_interval().SYM_GT() != null)
                                    lowerIncluded = false;
                                if (cDurationSpecContext.duration_interval_value().duration_interval().SYM_LT() != null)
                                    upperIncluded = false;
                            }
                        }
                        if (cDurationSpecContext.duration_interval_value().DURATION_VALUE() != null) {
                            if (cDurationSpecContext.duration_interval_value().relop() != null) {
                                if (cDurationSpecContext.duration_interval_value().relop().SYM_LT() != null) {
                                    upper = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                                    lowerIncluded = false;
                                    upperIncluded = false;
                                } else if (cDurationSpecContext.duration_interval_value().relop().SYM_LE() != null) {
                                    upper = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                                    lowerIncluded = false;
                                } else if (cDurationSpecContext.duration_interval_value().relop().SYM_GT() != null) {
                                    lower = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                                    lowerIncluded = false;
                                    upperIncluded = false;
                                } else if (cDurationSpecContext.duration_interval_value().relop().SYM_GE() != null) {
                                    lower = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                                    upperIncluded = false;
                                }
                            } else {
                                upper = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                                lower = new DvDuration(cDurationSpecContext.duration_interval_value().DURATION_VALUE().getText());
                            }
                        }
                        interval = new Interval<>(lower, upper, lowerIncluded, upperIncluded);
                    }
                    if (cDurationSpecContext.duration_pattern() != null) {
                        pattern = cDurationSpecContext.duration_pattern().ISO8601_DURATION_CONSTRAINT_PATTERN().getText();
                    }
                    if (cDurationSpecContext.DURATION_VALUE() != null) {
                        value = DvDuration.getInstance(cDurationSpecContext.DURATION_VALUE().getText());
                    }
                }
                return new CDuration(value, interval, assumed, pattern, null);
            }
        } catch(Exception e){
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cDurationContext, e.getMessage()));
        }
        return null;
    }

    private String removeEscapes(String value){
        if (value==null)
            return null;
        // remove escape, \" -> "
        value = value.replace("\\\"","\"");
        // remove escape, \\ -> \
        value = value.replace("\\\\","\\");
        return value;
    }

    private CString c_string(ArchetypeParser.C_stringContext cStringContext)  {
        Token t = null;
        String value = null;
        String pattern = null;
        String assumed = null;
        List list = null;
        String regexConstraint = null;
        List<String> constraint = null;
        try {
            if (cStringContext.STRING() != null) {
                assumed = cStringContext.STRING().getText();
                assumed = removeEscapes(assumed.substring(1, assumed.length()-1));
            }
            if ((cStringContext.c_string_spec() != null)) {
                constraint = new ArrayList<>();
                if (cStringContext.c_string_spec().string_list_value() != null) {
                    for (ArchetypeParser.String_valueContext iv : cStringContext.c_string_spec().string_list_value().string_value()) {
                        String c = iv.STRING().getText();
                        c = removeEscapes(c.substring(1, c.length() - 1));
                        constraint.add(c);
                    }
                } else if (cStringContext.c_string_spec().string_value() != null) {
                    String c = cStringContext.c_string_spec().string_value().getText();
                    c = removeEscapes(c.substring(1, c.length() - 1));
                    constraint.add(c);
                }
            }
            if (cStringContext.CONTAINED_REGEXP() != null) {
                regexConstraint = cStringContext.CONTAINED_REGEXP().getText();
                char regChar;
                if(regexConstraint.startsWith("{/")) regChar = '/';
                else regChar = '^';
                if((regexConstraint.startsWith("{"+regChar))&&(regexConstraint.endsWith(regChar+"}"))) {
                    regexConstraint = regexConstraint.substring(2, regexConstraint.length() - 2);
                }else {
                    String tmp = regexConstraint.substring(regexConstraint.lastIndexOf(regChar)).trim();
                    if (!"".equals(tmp)) {
                        if (tmp.indexOf(';') > 0) {
                            assumed = tmp.substring(tmp.indexOf(';')+1, tmp.length() - 1).trim();
                            assumed = assumed.substring(1, assumed.length() - 1);
                            regexConstraint = regexConstraint.substring(2, regexConstraint.lastIndexOf(regChar));
                        }
                    }
                }
////                regexConstraint.replaceAll("\\/", "/");
                return new CString(regexConstraint, null, assumed);
            }
            return new CString(null, constraint, assumed, null);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cStringContext,  e.getMessage()));
            return null;
        }
    }

    private CBoolean c_boolean(ArchetypeParser.C_booleanContext cBooleanContext)  {
        ArchetypeParser.C_boolean_specContext cBooleanSpecContext = cBooleanContext.c_boolean_spec();
        boolean trueAllowed = false;
        boolean falseAllowed = false;
        boolean assumed = false;
        boolean hasAssumed = false;
        try {
            if (cBooleanContext.BOOLEAN_VALUE() != null) { //assumed value
                assumed = Boolean.valueOf(cBooleanContext.BOOLEAN_VALUE().getText().toLowerCase());
                hasAssumed = true;
            }
            if (cBooleanContext.c_boolean_spec().BOOLEAN_VALUE() != null) {
                trueAllowed = "true".equals(cBooleanContext.c_boolean_spec().BOOLEAN_VALUE().getText().toLowerCase());
                falseAllowed = "false".equals(cBooleanContext.c_boolean_spec().BOOLEAN_VALUE().getText().toLowerCase());
            }else if (cBooleanContext.c_boolean_spec().boolean_list_value() != null) {
                for(TerminalNode terminalNode : cBooleanContext.c_boolean_spec().boolean_list_value().BOOLEAN_VALUE()){
                    if("true".equals(terminalNode.getText().toLowerCase()))
                        trueAllowed = true;
                    if("false".equals(terminalNode.getText().toLowerCase()))
                        falseAllowed = true;
                }
            }
            return new CBoolean(trueAllowed, falseAllowed, assumed, hasAssumed);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(cBooleanContext,  e.getMessage()));
            return null;
        }
    }

    private ArchetypeSlot archetype_slot(String path,
                                         ArchetypeParser.Archetype_slotContext archetypeSlotContext)  {
        try {
            String rmTypeName;
            String nodeId = null;
            Interval<Integer> occurrences;
            occurrences = occurrences(archetypeSlotContext.c_archetype_slot_head().c_occurrences());
            rmTypeName = archetypeSlotContext.c_archetype_slot_head().c_archetype_slot_id().rm_type_id().getText();
            if (archetypeSlotContext.c_archetype_slot_head().c_archetype_slot_id().AT_CODE() != null) {
                nodeId = getNodeId(archetypeSlotContext.c_archetype_slot_head().c_archetype_slot_id().AT_CODE());
            }
            if (nodeId != null)
                path = path + "[" + nodeId + "]";
            Set<Assertion> includes = null;
            Set<Assertion> excludes = null;
            if (archetypeSlotContext.c_includes() != null) {
                if (archetypeSlotContext.c_includes().assertion() != null) {
                    includes = new HashSet<>();
                    for (ArchetypeParser.AssertionContext assertionContext : archetypeSlotContext.c_includes().assertion()) {
                        includes.add(assertion(assertionContext));
                    }
                }
            }
            if (archetypeSlotContext.c_excludes() != null) {
                if (archetypeSlotContext.c_excludes().assertion() != null) {
                    excludes = new HashSet<>();
                    for (ArchetypeParser.AssertionContext assertionContext : archetypeSlotContext.c_excludes().assertion()) {
                        excludes.add(assertion(assertionContext));
                    }
                }
            }
            ArchetypeSlot archetypeSlot = new ArchetypeSlot(
                    path,
                    rmTypeName,
                    occurrences,
                    nodeId,
                    null,
                    includes,
                    excludes
            );
            return archetypeSlot;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(archetypeSlotContext,  e.getMessage()));
            return null;
        }
    }

    private Assertion assertion(ArchetypeParser.AssertionContext assertionContext) {
        if (assertionContext.boolean_assertion() != null) {
            ArchetypeParser.Boolean_assertionContext context = assertionContext.boolean_assertion();
            String tag = null;
            if (context.identifier() != null) {
                tag = context.identifier().getText();
            }
            ExpressionItem expressionItem = boolean_expression(assertionContext.boolean_assertion().boolean_expression());
            String stringExpression = expressionItem.toString();
            return new Assertion(tag, expressionItem, stringExpression, null);
        }
        return null;
    }

    private ExpressionItem boolean_expression(ArchetypeParser.Boolean_expressionContext booleanExprContext) {
        try {
            if (booleanExprContext.boolean_leaf() != null) {
                return boolean_leaf(booleanExprContext.boolean_leaf());
            } else if (booleanExprContext.boolean_node() != null) {
                return boolean_node(booleanExprContext.boolean_node());
            }
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(booleanExprContext,  e.getMessage()));
        }
        return null;
    }

    private ExpressionItem boolean_node(ArchetypeParser.Boolean_nodeContext booleanNodeContext) {
        ExpressionItem item;
        ExpressionItem item2;
        String attrId;
        OperatorKind op = null;
        if (booleanNodeContext.adl_absolute_path() != null) {
            item = ExpressionLeaf.pathConstant(booleanNodeContext.adl_absolute_path().getText());
            return new ExpressionUnaryOperator(
                    ExpressionItem.BOOLEAN,
                    OperatorKind.OP_EXISTS,
                    false,
                    item
            );
        } else if (booleanNodeContext.adl_relative_path() != null) {
            attrId = booleanNodeContext.adl_relative_path().getText();
//            if (booleanNodeContext.NOT_CONTAINED_REGEXP() == null) {
            CPrimitive cp;
            try {
                cp = c_primitive(booleanNodeContext.c_primitive());
            }catch(Exception e){
                errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(booleanNodeContext.c_primitive(), e.getMessage()));
                return null;
            }
            item2 = new ExpressionLeaf("C_" + cp.getType().toUpperCase(), cp, ExpressionLeaf.ReferenceType.CONSTRAINT);
//            } else {
//                item2 = ExpressionLeaf.stringConstant(booleanNodeContext.NOT_CONTAINED_REGEXP().getText());
//            }

            item = new ExpressionLeaf(ExpressionItem.STRING, attrId, ExpressionLeaf.ReferenceType.ATTRIBUTE);
            return new ExpressionBinaryOperator(ExpressionItem.BOOLEAN, OperatorKind.OP_MATCHES, false, item, item2);
        } else if (booleanNodeContext.SYM_NOT() != null) {
            item = boolean_expression(booleanNodeContext.boolean_expression());
            return new ExpressionUnaryOperator(ExpressionItem.BOOLEAN, OperatorKind.OP_NOT, false, item);
        } else {
            op = null;
            if (booleanNodeContext.SYM_EQ() != null) op = OperatorKind.OP_EQ;
            else if (booleanNodeContext.SYM_NE() != null) op = OperatorKind.OP_NE;
            else if (booleanNodeContext.SYM_LT() != null) op = OperatorKind.OP_LT;
            else if (booleanNodeContext.SYM_GT() != null) op = OperatorKind.OP_GT;
            else if (booleanNodeContext.SYM_LE() != null) op = OperatorKind.OP_LE;
            else if (booleanNodeContext.SYM_GE() != null) op = OperatorKind.OP_GE;
            if (op != null) {
                item = arithmetic_expression(booleanNodeContext.arithmetic_expression(0));
                item2 = arithmetic_expression(booleanNodeContext.arithmetic_expression(1));
                return new ExpressionBinaryOperator(item.getType(), op, false, item, item2);
            }
            op = null;
            if (booleanNodeContext.SYM_AND() != null) op = OperatorKind.OP_AND;
            else if (booleanNodeContext.SYM_OR() != null) op = OperatorKind.OP_OR;
            else if (booleanNodeContext.SYM_XOR() != null) op = OperatorKind.OP_XOR;
            else if (booleanNodeContext.SYM_IMPLIES() != null) op = OperatorKind.OP_IMPLIES;
            if (op != null) {
                item = boolean_leaf(booleanNodeContext.boolean_leaf());
                item2 = boolean_expression(booleanNodeContext.boolean_expression());
                if (item2 == null)
                    return item;
                return new ExpressionBinaryOperator(ExpressionItem.BOOLEAN, op, false, item, item2);
            }
        }
        return null;
    }

    private ExpressionItem arithmetic_expression(ArchetypeParser.Arithmetic_expressionContext arithmeticExpressionContext) {
        if (arithmeticExpressionContext.arithmetic_leaf() != null) {
            return arithmetic_leaf(arithmeticExpressionContext.arithmetic_leaf());
        } else if (arithmeticExpressionContext.arithmetic_node() != null) {
            return arithmetic_node(arithmeticExpressionContext.arithmetic_node());
        }
        return null;
    }

    private ExpressionItem boolean_leaf(ArchetypeParser.Boolean_leafContext booleanLeafContext) {
        if (booleanLeafContext.boolean_expression() != null) {
            return boolean_expression(booleanLeafContext.boolean_expression());
        } else if (booleanLeafContext.SYM_TRUE() != null) {
            return ExpressionLeaf.booleanConstant(true);
        } else if (booleanLeafContext.SYM_FALSE() != null) {
            return ExpressionLeaf.booleanConstant(false);
        }
        return null;
    }

    private ExpressionItem arithmetic_node(ArchetypeParser.Arithmetic_nodeContext arithmeticNodeContext) {
        ExpressionItem left;
        ExpressionItem right;
        OperatorKind op = null;
        if (arithmeticNodeContext.SYM_PLUS() != null) op = OperatorKind.OP_PLUS;
        else if (arithmeticNodeContext.SYM_MINUS() != null) op = OperatorKind.OP_MINUS;
        else if (arithmeticNodeContext.SYM_MULTIPLY() != null) op = OperatorKind.OP_MULTIPLY;
        else if (arithmeticNodeContext.SYM_DIVIDE() != null) op = OperatorKind.OP_DIVIDE;
        else if (arithmeticNodeContext.SYM_EXP() != null) op = OperatorKind.OP_EXP;
        if (op != null) {
            left = arithmetic_leaf(arithmeticNodeContext.arithmetic_leaf());
            right = arithmetic_expression(arithmeticNodeContext.arithmetic_expression());
            if (right == null)
                return left;
            return new ExpressionBinaryOperator(right.getType(), op, false, left, right);
        }
        return null;
    }

    private ExpressionItem arithmetic_leaf(ArchetypeParser.Arithmetic_leafContext arithmeticLeafContext) {
        if (arithmeticLeafContext.INTEGER_VALUE() != null) {
            int i = Integer.parseInt(arithmeticLeafContext.INTEGER_VALUE().getText());
            return ExpressionLeaf.intConstant(i);
        } else if (arithmeticLeafContext.REAL_VALUE() != null) {
            double i = Integer.parseInt(arithmeticLeafContext.REAL_VALUE().getText());
            return ExpressionLeaf.realConstant(i);
        } else if (arithmeticLeafContext.adl_absolute_path() != null) {
            return ExpressionLeaf.stringConstant(arithmeticLeafContext.adl_absolute_path().getText());
        }
        return null;
    }
}
