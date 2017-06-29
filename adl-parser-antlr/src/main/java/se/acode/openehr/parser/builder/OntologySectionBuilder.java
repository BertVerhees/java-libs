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

package se.acode.openehr.parser.builder;

import org.openehr.am.archetype.ontology.*;
import org.openehr.rm.support.terminology.TerminologyService;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;
import se.acode.openehr.parser.v1_4.ArchetypeParser;

import java.util.ArrayList;
import java.util.List;

import static se.acode.openehr.parser.builder.BuilderUtils.*;

/**
 * Created by verhees on 9-1-16.
 */
public class OntologySectionBuilder {

    public static OntologySectionBuilder getInstance() {
        return new OntologySectionBuilder();
    }

    public ArchetypeOntology getOntology(ArchetypeParser.Arch_ontologyContext ontologyContext, TerminologyService terminologyService, ArchetypeADLErrorListener errorListener)  {
        try {
            String primaryLanguage = null;
            List<String> terminologies = null;
            List<String> languagesAvailable = null;
            List<OntologyDefinitions> termDefinitionsList = null;
            List<OntologyDefinitions> constraintDefinitionsList = null;
            List<OntologyBinding> termBindingList = null;
            List<OntologyBinding> constraintBindingList = null;
            ArchetypeOntology archetypeOntology = null;
            if( (ontologyContext.dadl_text()!=null)&&
                    (ontologyContext.dadl_text().attr_vals()!=null)&&
                    (ontologyContext.dadl_text().attr_vals().attr_val()!=null)) {
                for (ArchetypeParser.Attr_valContext attrValContext : ontologyContext.dadl_text().attr_vals().attr_val()) {
                    if (attrValContext.ALPHA_LC_ID() != null) {
                        String key = attrValContext.ALPHA_LC_ID().getText();
                        if((attrValContext.object_block()!=null)&&
                                (attrValContext.object_block().object_value_block()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_value()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value()!=null)) {
                            String item = attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value().STRING().getText();
                            item = item.substring(1, item.length() - 1);
                            if ("primary_language".equals(key)) {
                                primaryLanguage = handleSingleStringItem(primaryLanguage, item, attrValContext, "primary_language", errorListener);
                            }else if ("terminologies_available".equals(key)) {
                                terminologies = handleSingleStringItemList(terminologies, item, attrValContext, "terminologies_available", errorListener);
                            }
                        } else if((attrValContext.object_block()!=null)&&
                                (attrValContext.object_block().object_value_block()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()!=null)) {
                            if ("terminologies_available".equals(key)) {
                                terminologies = handleStringItemList(terminologies, attrValContext, "terminologies_available", errorListener);
                            } else if ("languages_available".equals(key)) {
                                languagesAvailable = handleStringItemList(languagesAvailable, attrValContext, "languages_available", errorListener);
                            }
                        } else if((attrValContext.object_block()!=null)&&
                                (attrValContext.object_block().object_value_block()!=null)&&
                                (attrValContext.object_block().object_value_block().keyed_object()!=null)) {
                            for(ArchetypeParser.Keyed_objectContext keyedObjectContext:attrValContext.object_block().object_value_block().keyed_object()) {
                                if ((keyedObjectContext.primitive_value() != null) &&
                                        (keyedObjectContext.primitive_value().string_value() != null) &&
                                        (keyedObjectContext.object_block() != null) &&
                                        (keyedObjectContext.object_block().object_value_block() != null) &&
                                        (keyedObjectContext.object_block().object_value_block().attr_vals() != null) &&
                                        keyedObjectContext.object_block().object_value_block().attr_vals().attr_val() != null) {
                                    String attribute = null;
                                    attribute = handleSingleStringItem(attribute, keyedObjectContext.primitive_value().string_value().getText(), attrValContext, "attribute", errorListener);
                                    OntologyDefinitions ontologyTermDefinitions = null;
                                    OntologyDefinitions ontologyConstraintDefinitions = null;
                                    OntologyBinding ontologyBinding = null;
                                    for(ArchetypeParser.Attr_valContext attributeAttrValContext : keyedObjectContext.object_block().object_value_block().attr_vals().attr_val()){
                                        List<ArchetypeTerm> definitions = null;
                                        if("items".equals(attributeAttrValContext.ALPHA_LC_ID().getText())){
                                            for(ArchetypeParser.Keyed_objectContext itemKeyedObject: attributeAttrValContext.object_block().object_value_block().keyed_object()) {
                                                String atCode = handleSingleStringItem(null, itemKeyedObject.primitive_value().string_value().getText(), attributeAttrValContext, "term_definitions", errorListener);
                                                if (("term_definitions".equals(key)) || ("constraint_definitions".equals(key))) {
                                                    ArchetypeTerm archetypeTerm = new ArchetypeTerm(atCode);
                                                    if (definitions == null) {
                                                        definitions = new ArrayList<>();
                                                    }
                                                    definitions.add(archetypeTerm);
                                                    for (ArchetypeParser.Attr_valContext definitionAttrVal : itemKeyedObject.object_block().object_value_block().attr_vals().attr_val()) {
                                                        String keyText = handleSingleStringItem(null, definitionAttrVal.ALPHA_LC_ID().getText(), definitionAttrVal, "term_definitions", errorListener);
                                                        String text = handleSingleStringItem(null, definitionAttrVal.ALPHA_LC_ID().getText(), definitionAttrVal, "term_definitions", errorListener);
                                                        archetypeTerm.addItem(keyText, text);
                                                    }
                                                    if()
                                                    ontologyDefinitions = new OntologyDefinitions(attribute, definitions);
                                                }
                                                if ("term_bindings".equals(key)) {
                                                    List<String> terms = new ArrayList();
                                                    TermBindingItem termBindingItem;
                                                    List<OntologyBindingItem> termBindingItems = new ArrayList<>();
                                                    if(itemKeyedObject.object_block().object_value_block().primitive_object().primitive_value()!=null){
                                                        String term = itemKeyedObject.object_block().object_value_block().primitive_object().primitive_value().getText();
                                                        terms.add(term);
                                                        termBindingItem = new TermBindingItem(atCode, terms);
                                                        termBindingItems.add(termBindingItem);
                                                        ontologyBinding = new OntologyBinding(atCode, termBindingItems);
                                                    }else if(itemKeyedObject.object_block().object_value_block().primitive_object().primitive_list_value()!=null){
                                                        for(ArchetypeParser.Primitive_valueContext primitiveValueContext:itemKeyedObject.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()) {
                                                            String term = primitiveValueContext.getText();
                                                            terms.add(term);
                                                            termBindingItem = new TermBindingItem(atCode, terms);
                                                            termBindingItems.add(termBindingItem);
                                                        }
                                                        ontologyBinding = new OntologyBinding(atCode, termBindingItems);
                                                    }
                                                } else if ("constraint_bindings".equals(key)) {
                                                    QueryBindingItem queryBindingItem;
                                                    List<OntologyBindingItem> queryBindingItems = new ArrayList<>();
                                                    if(itemKeyedObject.object_block().object_value_block().primitive_object().primitive_value()!=null) {
                                                        if (itemKeyedObject.object_block().object_value_block().primitive_object().primitive_value().uri_value() != null) {
                                                            String query = itemKeyedObject.object_block().object_value_block().primitive_object().primitive_value().uri_value().getText();
                                                            queryBindingItem = new QueryBindingItem(atCode, new Query(query));
                                                            queryBindingItems.add(queryBindingItem);
                                                            ontologyBinding = new OntologyBinding(attribute, queryBindingItems);
                                                        } else if (itemKeyedObject.object_block().object_value_block().primitive_object().primitive_list_value() != null) {
                                                            for (ArchetypeParser.Primitive_valueContext primitiveValueContext : itemKeyedObject.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()) {
                                                                String query = primitiveValueContext.uri_value().getText();
                                                                queryBindingItem = new QueryBindingItem(atCode, new Query(query));
                                                                queryBindingItems.add(queryBindingItem);
                                                            }
                                                            ontologyBinding = new OntologyBinding(attribute, queryBindingItems);
                                                        }
                                                    }
                                                }
                                            }
                                            if ("term_definitions".equals(key)) {
                                                if (termDefinitionsList == null) {
                                                    termDefinitionsList = new ArrayList<>();
                                                }
                                                termDefinitionsList.add(ontologyDefinitions);
                                            } else if ("constraint_definitions".equals(key)) {
                                                if (constraintDefinitionsList == null) {
                                                    constraintDefinitionsList = new ArrayList<>();
                                                }
                                                constraintDefinitionsList.add(ontologyDefinitions);
                                            }
                                            if ("term_bindings".equals(key)) {
                                                if(termBindingList==null){
                                                    termBindingList = new ArrayList<>();
                                                }
                                                termBindingList.add(ontologyBinding);
                                            }else if ("constraint_bindings".equals(key)) {
                                                if(constraintBindingList==null){
                                                    constraintBindingList = new ArrayList<>();
                                                }
                                                constraintBindingList.add(ontologyBinding);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
//            ArchetypeParser.Ontology_textContext ontology = ontologyContext.ontology_text();
//            ArchetypeParser.Ontology_itemsContext ontologyItemsContext = ontology.ontology_items();
//            for (ArchetypeParser.Ontology_itemContext ontologyItemContext : ontologyItemsContext.ontology_item()) {
//                if (ontologyItemContext.term_definition() != null) {
//                    termDefinitionsList = buildDefinitionList(ontologyItemContext.term_definition().definition_value().definition_keyed_object());
//                } else if (ontologyItemContext.constraint_definition() != null) {
//                    constraintDefinitionsList = buildDefinitionList(ontologyItemContext.constraint_definition().definition_value().definition_keyed_object());
//                } else if (ontologyItemContext.primary_language() != null) {
//                    primaryLanguage = ontologyItemContext.primary_language().string_value().getText();
//                    primaryLanguage = primaryLanguage.substring(1, primaryLanguage.length() - 1);
//                } else if (ontologyItemContext.term_bindings() != null) {
//                    termBindingList = new ArrayList<>();
//                    OntologyBinding binding = null;
//                    for (ArchetypeParser.Keyed_objectContext koc : ontologyItemContext.term_bindings().attribute_value().object_value_block().keyed_object()) {  //ontology_binding_body
//                        String terminologyId = koc.primitive_value().string_value().getText();
//                        terminologyId = terminologyId.substring(1, terminologyId.length() - 1);
//                        String atCode;
//                        TermBindingItem termBindingItem = null;
//                        List<OntologyBindingItem> termBindingItems = new ArrayList<>();
//                        for (ArchetypeParser.Attr_valContext avc1 : koc.object_block().object_value_block().attr_vals().attr_val()) {
//                            for (ArchetypeParser.Keyed_objectContext koc2 : avc1.object_block().object_value_block().keyed_object()) {//term_binding_item
//                                atCode = koc2.primitive_value().string_value().getText();
//                                atCode = atCode.substring(1, atCode.length() - 1);
//                                List<String> terms = new ArrayList();
//                                if(koc2.object_block().object_value_block().primitive_object().primitive_value()!=null) {
//                                    String term = koc2.object_block().object_value_block().primitive_object().primitive_value().getText();
//                                    terms.add(term);
//                                    termBindingItem = new TermBindingItem(atCode, terms);
//                                    termBindingItems.add(termBindingItem);
//                                    binding = new OntologyBinding(terminologyId, termBindingItems);
//                                }else if(koc2.object_block().object_value_block().primitive_object().primitive_list_value()!=null){
//                                    for(ArchetypeParser.Primitive_valueContext primitiveValueContext:koc2.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()) {
//                                        String term = primitiveValueContext.getText();
//                                        terms.add(term);
//                                        termBindingItem = new TermBindingItem(atCode, terms);
//                                        termBindingItems.add(termBindingItem);
//                                    }
//                                    binding = new OntologyBinding(terminologyId, termBindingItems);
//                                }
//                            }
//                        }
//                        termBindingList.add(binding);
//                    }
//                } else if (ontologyItemContext.constraint_bindings() != null) {
//                    constraintBindingList = new ArrayList<>();
//                    OntologyBinding binding = null;
//                    for (ArchetypeParser.Constraint_bindings_keyed_objectContext koc : ontologyItemContext.constraint_bindings().constraint_bindings_keyed_object()) {  //ontology_binding_body
//                        String terminologyId = koc.string_value().getText();
//                        terminologyId = terminologyId.substring(1, terminologyId.length() - 1);
//                        QueryBindingItem queryBindingItem;
//                        for (ArchetypeParser.Constraint_bindings_itemContext avc1 : koc.constraint_bindings_item()) {
//                            for (ArchetypeParser.Constraint_bindings_item_keyed_objectContext koc2 : avc1.constraint_bindings_item_attribute_value().constraint_bindings_item_keyed_object()) {
//                                String acCode = koc2.string_value().getText();
//                                acCode = acCode.substring(1, acCode.length() - 1);
//                                List<OntologyBindingItem> queryBindingItems = new ArrayList<>();
//                                if(koc2.uri_value()!=null) {
//                                    String query = koc2.uri_value().getText();
//                                    queryBindingItem = new QueryBindingItem(acCode, new Query(query));
//                                    queryBindingItems.add(queryBindingItem);
//                                    binding = new OntologyBinding(terminologyId, queryBindingItems);
//                                    constraintBindingList.add(binding);
//                                }else if(koc2.primitive_list_value()!=null){
//                                    for(ArchetypeParser.Primitive_valueContext primitiveValueContext:koc2.primitive_list_value().primitive_value()) {
//                                        String query = primitiveValueContext.uri_value().getText();
//                                        queryBindingItem = new QueryBindingItem(acCode, new Query(query));
//                                        queryBindingItems.add(queryBindingItem);
//                                    }
//                                    binding = new OntologyBinding(terminologyId, queryBindingItems);
//                                    constraintBindingList.add(binding);
//                                }
//                            }
//                        }
//                    }
//                } else if (ontologyItemContext.languages_available()!=null) {
//                    languagesAvailable = new ArrayList<>();
//                    for (ArchetypeParser.Primitive_valueContext primitiveValueContext : ontologyItemContext.languages_available().primitive_list_value().primitive_value()) {
//                        String s = primitiveValueContext.getText();
//                        languagesAvailable.add(s);
//                    }
//                } else if (ontologyItemContext.terminologies_available()!=null) {
//                    terminologies = new ArrayList<>();
//                    for (ArchetypeParser.Primitive_valueContext primitiveValueContext : ontologyItemContext.terminologies_available().primitive_list_value().primitive_value()) {
//                        String s = primitiveValueContext.getText();
//                        terminologies.add(s);
//                    }
//                }
//            }
//            if (languagesAvailable != null) {
//                if (languagesAvailable.size() != termDefinitionsList.size()) {
//                    errorListener.getParserErrors().addError("There is a different size in \"languages_available\" (" + languagesAvailable.size() + ") then available in \"term_definitions\" (" + termDefinitionsList.size() + ").");
//                }
//            }
//            ArchetypeOntology archetypeOntology = new ArchetypeOntology(primaryLanguage, terminologies, termDefinitionsList, constraintDefinitionsList, termBindingList, constraintBindingList);
//            for(ValidationError validationError : archetypeOntology.getValidationErrors()){
//                errorListener.getParserErrors().addValidationError(validationError);
//            }
            return archetypeOntology;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(ontologyContext, e.getMessage()));
            return null;
        }
    }
}
