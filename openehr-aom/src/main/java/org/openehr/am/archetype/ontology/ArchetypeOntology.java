/*
 * component:   "openEHR Reference Implementation"
 * description: "Class ArchetypeOntology"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/am/archetype/ontology/ArchetypeOntology.java $"
 * revision:    "$LastChangedRevision: 13 $"
 * last_change: "$LastChangedDate: 2006-03-21 21:27:55 +0100 (Tue, 21 Mar 2006) $"
 */
package org.openehr.am.archetype.ontology;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehr.am.validation.ErrorType;
import org.openehr.am.validation.ValidationError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents ontology section from an archetype
 *
 * @author Rong Chen
 * @version 1.0
 */
public class ArchetypeOntology  implements Serializable{

    /**
     * Creates an ArchetypeOntology
     * //TODO Can we add param validation? Any param can be null and it makes things brittle.
     *
     * @param primaryLanguage
     * @param terminologies
     * @param termDefinitionsList
     * @param constDefinitionsList
     * @param termBindingList
     * @param constraintBindingList
     */
    public ArchetypeOntology(String primaryLanguage,
                             List<String> terminologies,
                             List<String> languages,
                             List<OntologyDefinitions> termDefinitionsList,
                             List<OntologyDefinitions> constDefinitionsList,
                             List<OntologyBinding> termBindingList,
                             List<OntologyBinding> constraintBindingList) throws Exception {
        this.primaryLanguage = primaryLanguage;
        this.terminologies = terminologies;
        this.termDefinitionsList = termDefinitionsList;
        this.constraintDefinitionsList = constDefinitionsList;
        this.termBindingList = termBindingList;
        this.constraintBindingList = constraintBindingList;

        // load defintionsMap
        termDefinitionMap = new HashMap<String, Map<String, ArchetypeTerm>>();
        constraintDefinitionMap = new HashMap<String, Map<String, ArchetypeTerm>>();
        errors = new ArrayList<>();
        loadDefs(termDefinitionMap, termDefinitionsList);

        // make sure the languages list is consistent
        this.languages = new ArrayList<String>();
        this.languages.addAll(termDefinitionMap.keySet());

        loadDefs(constraintDefinitionMap, constDefinitionsList);

        if(languages!=null) {
            if (this.languages.size() != languages.size()) {
                ValidationError validationError = new ValidationError(
                        ErrorType.VDL,
                        "Languages in TermDefinitions differ from langauges available",
                        "The languages in TermDefinitions size:" + this.languages.size() + " seems to differ from the langauges_available-size:" + languages.size() + ".");
                errors.add(validationError);
            }
            for (String s : this.languages) {
                if (!languages.contains(s)) {
                    ValidationError validationError = new ValidationError(
                            ErrorType.VDL,
                            "Languages in TermDefinitions differ from langauges available",
                            "The language in TermDefinitions:\"" + s + "\" is not in the langauges_available list.");
                    errors.add(validationError);
                }
            }
            for (String s : languages) {
                if (!this.languages.contains(s)) {
                    ValidationError validationError = new ValidationError(
                            ErrorType.VDL,
                            "Languages in TermDefinitions differ from langauges available",
                            "The language in langauges_available:\"" + s + "\" is not in the TermDefinitions.");
                    errors.add(validationError);
                }
            }
        }

    }

    private List<ValidationError> errors;

    public  List<ValidationError> getValidationErrors(){
        return errors;
    }

    private void loadDefs(Map<String, Map<String, ArchetypeTerm>> map,
                          List<OntologyDefinitions> list) throws Exception {
        if (list == null) {
            return;
        }
        Map<String, ArchetypeTerm> codeMap = null;
        for (OntologyDefinitions defs : list) {
            codeMap = map.get(defs.getLanguage());
            if (null == codeMap) {
              codeMap = new HashMap<String, ArchetypeTerm>();
            }
            if(defs.getDefinitions()!=null) {
                for (ArchetypeTerm item : defs.getDefinitions()) {
                    codeMap.put(item.getCode(), item);
                }
                Map<String, ArchetypeTerm> codeMapAdded = map.put(defs.getLanguage(), codeMap);
                if (codeMapAdded != null) {
                    ValidationError validationError = new ValidationError(
                            ErrorType.VDL,
                            "Language occuring more times",
                            "The language:" + defs.getLanguage() + " seems to appear more then one time in this definition list.");
                    errors.add(validationError);
                }
            }else{
                ValidationError validationError = new ValidationError(
                        ErrorType.VONLC,
                        "Languages consistency",
                        "Each archetype term used as a node identifier the archetype definition must be defined for each language term_definitions part of the ontology, missing items for:\""+defs.getLanguage()+"\".");
                errors.add(validationError);
            }
        }
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getTerminologies() {
        return terminologies;
    }

    public List<OntologyDefinitions> getTermDefinitionsList() {
        return termDefinitionsList;
    }

    public List<OntologyDefinitions> getConstraintDefinitionsList() {
        return constraintDefinitionsList;
    }

    public List<OntologyBinding> getTermBindingList() {
        return termBindingList;
    }

    public List<OntologyBinding> getConstraintBindingList() {
        return constraintBindingList;
    }

    // null if not found
    private ArchetypeTerm definition(String language, String code,
    		Map<String, Map<String, ArchetypeTerm>> definitionMap) {
        Map<String, ArchetypeTerm> map = definitionMap.get(language);
        if (map == null) {
            return null;
        }
        return map.get(code);
    }

    /**
     * Constraint definition for a code, in a specified language.
     * 
     * @param language
     * @param code
     * @return null if not found
     */
    public ArchetypeTerm constraintDefinition(String language, String code) {
    	return definition(language, code, constraintDefinitionMap);
    }
    
    /**
     * Term definition for a code, in a specified language.
     * 
     * @param language
     * @param code
     * @return null if not found
     */
    public ArchetypeTerm termDefinition(String language, String code) {
    	return definition(language, code, termDefinitionMap);
    }
    
    /**
     * Term definition for a code in the primary language.
     * 
     * @param code
     * @return not null
     */
    public ArchetypeTerm termDefinition(String code) {
    	return definition(primaryLanguage, code, termDefinitionMap);
    }
    
    /**
     * String representation of this object
     *
     * @return string form
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("primaryLanguage", primaryLanguage)
                .append("languages", languages)
                .append("terminologies", terminologies)
                .append("termDefinitionsList", termDefinitionsList)
                .append("constraintDefinitionsList", constraintDefinitionsList)
                .append("termBindingList", termBindingList)
                .append("constraintBindingList", constraintBindingList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArchetypeOntology)) return false;

        ArchetypeOntology that = (ArchetypeOntology) o;

        if (errors != null ? !errors.equals(that.errors) : that.errors != null) return false;
        if (getPrimaryLanguage() != null ? !getPrimaryLanguage().equals(that.getPrimaryLanguage()) : that.getPrimaryLanguage() != null)
            return false;
        if (getLanguages() != null ? !getLanguages().equals(that.getLanguages()) : that.getLanguages() != null)
            return false;
        if (getTerminologies() != null ? !getTerminologies().equals(that.getTerminologies()) : that.getTerminologies() != null)
            return false;
        if (getTermDefinitionsList() != null ? !getTermDefinitionsList().equals(that.getTermDefinitionsList()) : that.getTermDefinitionsList() != null)
            return false;
        if (getConstraintDefinitionsList() != null ? !getConstraintDefinitionsList().equals(that.getConstraintDefinitionsList()) : that.getConstraintDefinitionsList() != null)
            return false;
        if (getTermBindingList() != null ? !getTermBindingList().equals(that.getTermBindingList()) : that.getTermBindingList() != null)
            return false;
        if (getConstraintBindingList() != null ? !getConstraintBindingList().equals(that.getConstraintBindingList()) : that.getConstraintBindingList() != null)
            return false;
        if (termDefinitionMap != null ? !termDefinitionMap.equals(that.termDefinitionMap) : that.termDefinitionMap != null)
            return false;
        return constraintDefinitionMap != null ? constraintDefinitionMap.equals(that.constraintDefinitionMap) : that.constraintDefinitionMap == null;
    }

    @Override
    public int hashCode() {
        int result = errors != null ? errors.hashCode() : 0;
        result = 31 * result + (getPrimaryLanguage() != null ? getPrimaryLanguage().hashCode() : 0);
        result = 31 * result + (getLanguages() != null ? getLanguages().hashCode() : 0);
        result = 31 * result + (getTerminologies() != null ? getTerminologies().hashCode() : 0);
        result = 31 * result + (getTermDefinitionsList() != null ? getTermDefinitionsList().hashCode() : 0);
        result = 31 * result + (getConstraintDefinitionsList() != null ? getConstraintDefinitionsList().hashCode() : 0);
        result = 31 * result + (getTermBindingList() != null ? getTermBindingList().hashCode() : 0);
        result = 31 * result + (getConstraintBindingList() != null ? getConstraintBindingList().hashCode() : 0);
        result = 31 * result + (termDefinitionMap != null ? termDefinitionMap.hashCode() : 0);
        result = 31 * result + (constraintDefinitionMap != null ? constraintDefinitionMap.hashCode() : 0);
        return result;
    }

    /* fields */
    private String primaryLanguage;
    private List<String> languages;
    private List<String> terminologies;
    private List<OntologyDefinitions> termDefinitionsList;
    private List<OntologyDefinitions> constraintDefinitionsList;
    private List<OntologyBinding> termBindingList;
    private List<OntologyBinding> constraintBindingList;

    /* calculated fields */
    // outer map language as key, inner map code as key
    private Map<String, Map<String, ArchetypeTerm>> termDefinitionMap;
    private Map<String, Map<String, ArchetypeTerm>> constraintDefinitionMap;
}

/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is ArchetypeOntology.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2003-2004
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */