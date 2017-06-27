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

/**
 * Created by verhees on 9-1-16.
 */
public class OntologySectionBuilder {

    public static OntologySectionBuilder getInstance() {
        return new OntologySectionBuilder();
    }

    private List<OntologyDefinitions> buildDefinitionList(List<ArchetypeParser.Definition_keyed_objectContext> definitionKeyedObjectContexts) {
        List<OntologyDefinitions> list = new ArrayList<>();
        for (ArchetypeParser.Definition_keyed_objectContext definitionKeyObject : definitionKeyedObjectContexts) {
            String language = definitionKeyObject.definition_key().string_value().getText();
            language = language.substring(1, language.length() - 1);
            List<ArchetypeTerm> definitions = new ArrayList<>();
            for (ArchetypeParser.Archetype_termsContext archetypeTermsContext : definitionKeyObject.definition_key_object_value().archetype_terms()) {  //List<ArchetypeTerm> definitions (items)
                for (ArchetypeParser.Archetype_term_objectContext archetypeTermFromList : archetypeTermsContext.archetype_term().archetype_term_object()) {     //ArchetypeTerm
                    String code = archetypeTermFromList.string_value().getText();  //ArchetypeTerm code ATCODE
                    code = code.substring(1, code.length() - 1);
                    ArchetypeTerm archetypeTerm = new ArchetypeTerm(code);
                    definitions.add(archetypeTerm);
                    for (ArchetypeParser.Attr_valContext keyValue : archetypeTermFromList.archetype_term_item_object_value().key_object_value().object_value_block().attr_vals().attr_val()) {
                        String key = keyValue.attribute().ALPHA_LC_ID().getText();   ////ArchetypeTerm key
                        String value = keyValue.attribute_value().object_value_block().primitive_object().primitive_value().string_value().getText();  //ArchetypeTerm value
                        value = value.substring(1, value.length() - 1);
                        archetypeTerm.addItem(key, value);   //ArchetypeTerm addItem(key,value)
                    }
                }
            }
            OntologyDefinitions ontologyDefinitions = new OntologyDefinitions(language, definitions);
            list.add(ontologyDefinitions);
        }
        return list;
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
            ArchetypeParser.Ontology_textContext ontology = ontologyContext.ontology_text();
            ArchetypeParser.Ontology_itemsContext ontologyItemsContext = ontology.ontology_items();
            for (ArchetypeParser.Ontology_itemContext ontologyItemContext : ontologyItemsContext.ontology_item()) {
                if (ontologyItemContext.term_definition() != null) {
                    termDefinitionsList = buildDefinitionList(ontologyItemContext.term_definition().definition_value().definition_keyed_object());
                } else if (ontologyItemContext.constraint_definition() != null) {
                    constraintDefinitionsList = buildDefinitionList(ontologyItemContext.constraint_definition().definition_value().definition_keyed_object());
                } else if (ontologyItemContext.primary_language() != null) {
                    primaryLanguage = ontologyItemContext.primary_language().string_value().getText();
                    primaryLanguage = primaryLanguage.substring(1, primaryLanguage.length() - 1);
                } else if (ontologyItemContext.term_bindings() != null) {
                    termBindingList = new ArrayList<>();
                    OntologyBinding binding = null;
                    for (ArchetypeParser.Keyed_objectContext koc : ontologyItemContext.term_bindings().attribute_value().object_value_block().keyed_object()) {  //ontology_binding_body
                        String terminologyId = koc.key().primitive_value().string_value().getText();
                        terminologyId = terminologyId.substring(1, terminologyId.length() - 1);
                        String atCode;
                        TermBindingItem termBindingItem = null;
                        List<OntologyBindingItem> termBindingItems = new ArrayList<>();
                        for (ArchetypeParser.Attr_valContext avc1 : koc.key_object_value().object_value_block().attr_vals().attr_val()) {
                            for (ArchetypeParser.Keyed_objectContext koc2 : avc1.attribute_value().object_value_block().keyed_object()) {//term_binding_item
                                atCode = koc2.key().primitive_value().string_value().getText();
                                atCode = atCode.substring(1, atCode.length() - 1);
                                List<String> terms = new ArrayList();
                                if(koc2.key_object_value().object_value_block().primitive_object().primitive_value()!=null) {
                                    String term = koc2.key_object_value().object_value_block().primitive_object().primitive_value().getText();
                                    terms.add(term);
                                    termBindingItem = new TermBindingItem(atCode, terms);
                                    termBindingItems.add(termBindingItem);
                                    binding = new OntologyBinding(terminologyId, termBindingItems);
                                }else if(koc2.key_object_value().object_value_block().primitive_object().primitive_list_value()!=null){
                                    for(ArchetypeParser.Primitive_valueContext primitiveValueContext:koc2.key_object_value().object_value_block().primitive_object().primitive_list_value().primitive_value()) {
                                        String term = primitiveValueContext.getText();
                                        terms.add(term);
                                        termBindingItem = new TermBindingItem(atCode, terms);
                                        termBindingItems.add(termBindingItem);
                                    }
                                    binding = new OntologyBinding(terminologyId, termBindingItems);
                                }
                            }
                        }
                        termBindingList.add(binding);
                    }
                } else if (ontologyItemContext.constraint_bindings() != null) {
                    constraintBindingList = new ArrayList<>();
                    OntologyBinding binding = null;
                    for (ArchetypeParser.Constraint_bindings_keyed_objectContext koc : ontologyItemContext.constraint_bindings().constraint_bindings_keyed_object()) {  //ontology_binding_body
                        String terminologyId = koc.string_value().getText();
                        terminologyId = terminologyId.substring(1, terminologyId.length() - 1);
                        QueryBindingItem queryBindingItem;
                        for (ArchetypeParser.Constraint_bindings_itemContext avc1 : koc.constraint_bindings_item()) {
                            for (ArchetypeParser.Constraint_bindings_item_keyed_objectContext koc2 : avc1.constraint_bindings_item_attribute_value().constraint_bindings_item_keyed_object()) {
                                String acCode = koc2.string_value().getText();
                                acCode = acCode.substring(1, acCode.length() - 1);
                                List<OntologyBindingItem> queryBindingItems = new ArrayList<>();
                                if(koc2.uri_value()!=null) {
                                    String query = koc2.uri_value().getText();
                                    queryBindingItem = new QueryBindingItem(acCode, new Query(query));
                                    queryBindingItems.add(queryBindingItem);
                                    binding = new OntologyBinding(terminologyId, queryBindingItems);
                                    constraintBindingList.add(binding);
                                }else if(koc2.primitive_list_value()!=null){
                                    for(ArchetypeParser.Primitive_valueContext primitiveValueContext:koc2.primitive_list_value().primitive_value()) {
                                        String query = primitiveValueContext.uri_value().getText();
                                        queryBindingItem = new QueryBindingItem(acCode, new Query(query));
                                        queryBindingItems.add(queryBindingItem);
                                    }
                                    binding = new OntologyBinding(terminologyId, queryBindingItems);
                                    constraintBindingList.add(binding);
                                }
                            }
                        }
                    }
                } else if (ontologyItemContext.languages_available()!=null) {
                    languagesAvailable = new ArrayList<>();
                    for (ArchetypeParser.Primitive_valueContext primitiveValueContext : ontologyItemContext.languages_available().primitive_list_value().primitive_value()) {
                        String s = primitiveValueContext.getText();
                        languagesAvailable.add(s);
                    }
                } else if (ontologyItemContext.terminologies_available()!=null) {
                    terminologies = new ArrayList<>();
                    for (ArchetypeParser.Primitive_valueContext primitiveValueContext : ontologyItemContext.terminologies_available().primitive_list_value().primitive_value()) {
                        String s = primitiveValueContext.getText();
                        terminologies.add(s);
                    }
                }
            }
            if (languagesAvailable != null) {
                if (languagesAvailable.size() != termDefinitionsList.size()) {
                    errorListener.getParserErrors().addError("There is a different size in \"languages_available\" (" + languagesAvailable.size() + ") then available in \"term_definitions\" (" + termDefinitionsList.size() + ").");
                }
            }
            ArchetypeOntology archetypeOntology = new ArchetypeOntology(primaryLanguage, terminologies, termDefinitionsList, constraintDefinitionsList, termBindingList, constraintBindingList);
            for(ValidationError validationError : archetypeOntology.getValidationErrors()){
                errorListener.getParserErrors().addValidationError(validationError);
            }
            return archetypeOntology;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(ontologyContext, e.getMessage()));
            return null;
        }
    }
}
