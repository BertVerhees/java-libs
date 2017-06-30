/*
 * component:   "openEHR Reference Implementation"
 * description: "Class OntologyDefinitions"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/am/archetype/ontology/OntologyDefinitions.java $"
 * revision:    "$LastChangedRevision: 2 $"
 * last_change: "$LastChangedDate: 2005-10-12 23:20:08 +0200 (Wed, 12 Oct 2005) $"
 */
package org.openehr.am.archetype.ontology;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents definitions for both terms and constraints
 *
 * @author Rong Chen
 * @version 1.0
 */
public class OntologyDefinitions implements Serializable{

    /**
     * Constructor
     *
     * @param language
     * @param definitions
     */
    public OntologyDefinitions(String language,
                               List<ArchetypeTerm> definitions) {
        this.language = language;
        this.definitions = definitions;
    }

    public String getLanguage() {
        return language;
    }

    public List<ArchetypeTerm> getDefinitions() {
        return definitions;
    }

    /**
     * String representation of this object
     *
     * @return string form
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OntologyDefinitions)) return false;

        OntologyDefinitions that = (OntologyDefinitions) o;

        if (getLanguage() != null ? !getLanguage().equals(that.getLanguage()) : that.getLanguage() != null)
            return false;
        return getDefinitions() != null ? getDefinitions().equals(that.getDefinitions()) : that.getDefinitions() == null;
    }

    @Override
    public int hashCode() {
        int result = getLanguage() != null ? getLanguage().hashCode() : 0;
        result = 31 * result + (getDefinitions() != null ? getDefinitions().hashCode() : 0);
        return result;
    }

    /* fields */
    private String language;
    private List<ArchetypeTerm> definitions;
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
 *  The Original Code is OntologyDefinitions.java
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