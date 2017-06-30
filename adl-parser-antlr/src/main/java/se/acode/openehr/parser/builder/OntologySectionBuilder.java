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
import org.openehr.am.validation.ValidationError;
import org.openehr.rm.support.terminology.TerminologyService;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;
import se.acode.openehr.parser.v1_4.ArchetypeParser;

import java.util.ArrayList;
import java.util.List;

import static se.acode.openehr.parser.builder.BuilderUtils.*;

/**
 * Created by verhees on 9-1-16.
 * This code follows the grammar, that is why it has this structure. To understand, keep the grammar for reference.
 */
public class OntologySectionBuilder {

    public static OntologySectionBuilder getInstance() {
        return new OntologySectionBuilder();
    }

    public ArchetypeOntology getOntology(ArchetypeParser.Arch_ontologyContext ontologyContext, TerminologyService terminologyService, ArchetypeADLErrorListener errorListener) {
        try {
            String primaryLanguage = null;
            List<String> terminologies = null;
            List<String> languagesAvailable = null;
            List<OntologyDefinitions> termDefinitionsList = null;
            List<OntologyDefinitions> constraintDefinitionsList = null;
            List<OntologyBinding> termBindingList = null;
            List<OntologyBinding> constraintBindingList = null;
            if (    (ontologyContext.dadl_text() != null) &&
                    (ontologyContext.dadl_text().attr_vals() != null) &&
                    (ontologyContext.dadl_text().attr_vals().attr_val() != null)) {
                for (ArchetypeParser.Attr_valContext ontologyAttrVal : ontologyContext.dadl_text().attr_vals().attr_val()) {
                    if (ontologyAttrVal.ALPHA_LC_ID() != null) {
                        String ontologyKey = ontologyAttrVal.ALPHA_LC_ID().getText();
                        //single string item
                        if (    (ontologyAttrVal.object_block() != null) &&
                                (ontologyAttrVal.object_block().primitive_object() != null) &&
                                (ontologyAttrVal.object_block().primitive_object().primitive_value() != null) &&
                                (ontologyAttrVal.object_block().primitive_object().primitive_value().string_value() != null)) {
                            String item = ontologyAttrVal.object_block().primitive_object().primitive_value().string_value().STRING().getText();
                            item = item.substring(1, item.length() - 1);
                            if ("primary_language".equals(ontologyKey)) {
                                primaryLanguage = handleSingleStringItem(primaryLanguage, item, ontologyAttrVal, "primary_language", errorListener);
                            } else if ("terminologies_available".equals(ontologyKey)) {
                                terminologies = handleSingleStringItemList(terminologies, item, ontologyAttrVal, "terminologies_available", errorListener);
                            }
                            //list items
                        } else if ( (ontologyAttrVal.object_block() != null) &&
                                    (ontologyAttrVal.object_block().primitive_object() != null) &&
                                    (ontologyAttrVal.object_block().primitive_object().primitive_list_value() != null) &&
                                    (ontologyAttrVal.object_block().primitive_object().primitive_list_value().primitive_value() != null)) {
                            if ("terminologies_available".equals(ontologyKey)) {
                                terminologies = handleStringItemList(terminologies, ontologyAttrVal, "terminologies_available", errorListener);
                            } else if ("languages_available".equals(ontologyKey)) {
                                languagesAvailable = handleStringItemList(languagesAvailable, ontologyAttrVal, "languages_available", errorListener);
                            }
                            //constraint and term bindings
                        } else if ( (ontologyAttrVal.object_block() != null) &&
                                    (ontologyAttrVal.object_block().keyed_object() != null)) {
                            for (ArchetypeParser.Keyed_objectContext ontologyKeyedObject : ontologyAttrVal.object_block().keyed_object()) {
                                if (    (ontologyKeyedObject.primitive_value() != null) &&
                                        (ontologyKeyedObject.primitive_value().string_value() != null) &&
                                        (ontologyKeyedObject.object_block() != null) &&
                                        (ontologyKeyedObject.object_block().attr_vals() != null) &&
                                        (ontologyKeyedObject.object_block().attr_vals().attr_val() != null)) {
                                    String langOrTermKey = handleSingleStringItem(null, ontologyKeyedObject.primitive_value().string_value().getText(), ontologyAttrVal, "attribute", errorListener);
                                    OntologyDefinitions ontologyTermDefinitions = null;
                                    OntologyDefinitions ontologyConstraintDefinitions = null;
                                    OntologyBinding ontologyTermBinding = null;
                                    OntologyBinding ontologyConstraintBinding = null;
                                    for (ArchetypeParser.Attr_valContext attributeAttrValContext : ontologyKeyedObject.object_block().attr_vals().attr_val()) {
                                        List<ArchetypeTerm> definitions = null;
                                        List<OntologyBindingItem> termBindingItems = null;
                                        List<OntologyBindingItem> queryBindingItems = null;
                                        if ("items".equals(attributeAttrValContext.ALPHA_LC_ID().getText())) {
                                            if (attributeAttrValContext.object_block().keyed_object().size() > 0) {
                                                for (ArchetypeParser.Keyed_objectContext itemKeyedObject : attributeAttrValContext.object_block().keyed_object()) {
                                                    String atCode = handleSingleStringItem(null, itemKeyedObject.primitive_value().string_value().getText(), attributeAttrValContext, "term_definitions", errorListener);
                                                    if (("term_definitions".equals(ontologyKey)) || ("constraint_definitions".equals(ontologyKey))) {
                                                        ArchetypeTerm archetypeTerm = new ArchetypeTerm(atCode);
                                                        if (definitions == null) {
                                                            definitions = new ArrayList<>();
                                                        }
                                                        for (ArchetypeParser.Attr_valContext definitionAttrVal : itemKeyedObject.object_block().attr_vals().attr_val()) {
                                                            String keyText = handleSingleStringItem(null, definitionAttrVal.ALPHA_LC_ID().getText(), definitionAttrVal, "term_definitions", errorListener);
                                                            String text = handleSingleStringItem(null, definitionAttrVal.object_block().primitive_object().primitive_value().string_value().getText(), definitionAttrVal, "term_definitions", errorListener);
                                                            archetypeTerm.addItem(keyText, text);
                                                        }
                                                        definitions.add(archetypeTerm);
                                                        if ("term_definitions".equals(ontologyKey)) {
                                                            ontologyTermDefinitions = new OntologyDefinitions(langOrTermKey, definitions);
                                                        } else if ("constraint_definitions".equals(ontologyKey))
                                                            ontologyConstraintDefinitions = new OntologyDefinitions(langOrTermKey, definitions);
                                                    }else if ("term_bindings".equals(ontologyKey)) {
                                                        List<String> terms = new ArrayList();
                                                        TermBindingItem termBindingItem;
                                                        if (termBindingItems == null) {
                                                            termBindingItems = new ArrayList<>();
                                                        }
                                                        if (itemKeyedObject.object_block().primitive_object().primitive_value() != null) {
                                                            String term = itemKeyedObject.object_block().primitive_object().primitive_value().getText();
                                                            terms.add(term);
                                                            termBindingItem = new TermBindingItem(atCode, terms);
                                                            termBindingItems.add(termBindingItem);
                                                            ontologyTermBinding = new OntologyBinding(langOrTermKey, termBindingItems);
                                                        } else if (itemKeyedObject.object_block().primitive_object().primitive_list_value() != null) {
                                                            for (ArchetypeParser.Primitive_valueContext primitiveValueContext : itemKeyedObject.object_block().primitive_object().primitive_list_value().primitive_value()) {
                                                                String term = primitiveValueContext.getText();
                                                                terms.add(term);
                                                                termBindingItem = new TermBindingItem(atCode, terms);
                                                                termBindingItems.add(termBindingItem);
                                                            }
                                                            ontologyTermBinding = new OntologyBinding(langOrTermKey, termBindingItems);
                                                        }
                                                    } else if ("constraint_bindings".equals(ontologyKey)) {
                                                        QueryBindingItem queryBindingItem;
                                                        if (queryBindingItems == null) {
                                                            queryBindingItems = new ArrayList<>();
                                                        }
                                                        if (itemKeyedObject.object_block().primitive_object().primitive_value() != null) {
                                                            if (itemKeyedObject.object_block().primitive_object().primitive_value().uri_value() != null) {
                                                                String query = itemKeyedObject.object_block().primitive_object().primitive_value().uri_value().getText();
                                                                queryBindingItem = new QueryBindingItem(atCode, new Query(query));
                                                                queryBindingItems.add(queryBindingItem);
                                                                ontologyConstraintBinding = new OntologyBinding(langOrTermKey, queryBindingItems);
                                                            } else if (itemKeyedObject.object_block().primitive_object().primitive_list_value() != null) {
                                                                for (ArchetypeParser.Primitive_valueContext primitiveValueContext : itemKeyedObject.object_block().primitive_object().primitive_list_value().primitive_value()) {
                                                                    String query = primitiveValueContext.uri_value().getText();
                                                                    queryBindingItem = new QueryBindingItem(atCode, new Query(query));
                                                                    queryBindingItems.add(queryBindingItem);
                                                                }
                                                                ontologyConstraintBinding = new OntologyBinding(langOrTermKey, queryBindingItems);
                                                            }
                                                        }
                                                    }
                                                }
                                            }else{
                                                //empty items
                                                if (("term_definitions".equals(ontologyKey)) || ("constraint_definitions".equals(ontologyKey))) {
                                                    definitions = new ArrayList<>();
                                                    if ("term_definitions".equals(ontologyKey)) {
                                                        ontologyTermDefinitions = new OntologyDefinitions(langOrTermKey, definitions);
                                                    } else if ("constraint_definitions".equals(ontologyKey))
                                                        ontologyConstraintDefinitions = new OntologyDefinitions(langOrTermKey, definitions);
                                                } else if ("term_bindings".equals(ontologyKey)) {
                                                    termBindingItems = new ArrayList<>();
                                                    ontologyTermBinding = new OntologyBinding(langOrTermKey, termBindingItems);
                                                } else if ("constraint_bindings".equals(ontologyKey)) {
                                                    queryBindingItems = new ArrayList<>();
                                                    ontologyConstraintBinding = new OntologyBinding(langOrTermKey, queryBindingItems);
                                                }
                                            }
                                            if ("term_definitions".equals(ontologyKey)) {
                                                if (termDefinitionsList == null) {
                                                    termDefinitionsList = new ArrayList<>();
                                                }
                                                termDefinitionsList.add(ontologyTermDefinitions);
                                            } else if ("constraint_definitions".equals(ontologyKey)) {
                                                if (constraintDefinitionsList == null) {
                                                    constraintDefinitionsList = new ArrayList<>();
                                                }
                                                constraintDefinitionsList.add(ontologyConstraintDefinitions);
                                            } else if ("term_bindings".equals(ontologyKey)) {
                                                if (termBindingList == null) {
                                                    termBindingList = new ArrayList<>();
                                                }
                                                termBindingList.add(ontologyTermBinding);
                                            } else if ("constraint_bindings".equals(ontologyKey)) {
                                                if (constraintBindingList == null) {
                                                    constraintBindingList = new ArrayList<>();
                                                }
                                                constraintBindingList.add(ontologyConstraintBinding);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ArchetypeOntology archetypeOntology = new ArchetypeOntology(primaryLanguage, terminologies, languagesAvailable, termDefinitionsList, constraintDefinitionsList, termBindingList, constraintBindingList);
            for (ValidationError validationError : archetypeOntology.getValidationErrors()) {
                errorListener.getParserErrors().addValidationError(validationError);
            }
            return archetypeOntology;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(ontologyContext, e.getMessage()));
            return null;
        }
    }
}
