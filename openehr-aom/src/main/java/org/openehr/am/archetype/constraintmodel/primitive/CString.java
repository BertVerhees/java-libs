/*
 * component:   "openEHR Reference Implementation"
 * description: "Class CString"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/am/archetype/constraintmodel/primitive/CString.java $"
 * revision:    "$LastChangedRevision: 23 $"
 * last_change: "$LastChangedDate: 2006-03-31 02:40:54 +0200 (Fri, 31 Mar 2006) $"
 */
package org.openehr.am.archetype.constraintmodel.primitive;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Constraint on instances of String.
 *
 * @author Rong Chen
 * @version 1.0
 */
public final class CString extends CPrimitive {

    /**
     * Constructs a StringConstraint with an assumed value
     *
     * @param pattern
     * @param list    List<String>
     * @param assumedValue
     * @throws IllegalArgumentException if pattern not null and empty
     */
    public CString(String pattern, List<String> list, String assumedValue) {
        this(pattern, list, assumedValue, null);
    }
    
    /**
     * Constructs a StringConstraint with an assumed value
     *
     * @param pattern
     * @param list    List<String>
     * @param assumedValue
     * @throws IllegalArgumentException if pattern not null and empty
     */
    public CString(String pattern, List<String> list, String assumedValue,
    		String defaultValue) {
        if (pattern != null && StringUtils.isEmpty(pattern)) {
            throw new IllegalArgumentException("empty pattern");
        }
        this.pattern = pattern;
        this.list = ( list == null ? null : new ArrayList<String>(list) );
        this.assumedValue = assumedValue;
        this.defaultValue = defaultValue;
    }
    
    /**
     * Constructs a StringConstraint without assumed value
     *
     * @param pattern
     * @param list    List<String>
     * @throws IllegalArgumentException if pattern not null and empty
     */
    public CString(String pattern, List<String> list) {
    	this(pattern, list, null);
    }
    
    /**
     * Constructs a stringConstraint with pattern
     * 
     * @param pattern
     */
    public CString(String pattern) {
    	this(pattern, null, null);
    }
    	
    /**
     * Return the primitive type this constraint is applied on
     *
     * @return name of the type
     */
    @Override
    public String getType() {
        return "String";
    }

    /**
     * True if value is valid with respect to constraint
     *
     * @param value
     * @return true if valid
     */
    @Override
    public boolean validValue(Object value) {
        String str = value.toString();
        return ( (pattern == null && list == null) 
        		 || (pattern != null && str.matches(pattern)) 
        		 || (list != null && list.contains(str)) );
    }

    /**
     * Regular expression pattern for proposed instances of String to match.
     *
     * @return pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * List of Strings specifying constraint
     *
     * @return unmodifiable List<String> or null
     */
    public List<String> getList() {
        return list == null ? null : Collections.unmodifiableList(list);
    }
    @Override
	public boolean hasAssumedValue() {
		return assumedValue != null;
	}

	@Override
	public Object assumedValue() {
		return assumedValue;
	}
    
	@Override
	public boolean hasAssignedValue() {
		return list != null && list.size() == 1;
	}

	@Override
	public String assignedValue() {
        if (list == null || list.size() != 1) {
            return null;
        }
        return list.get(0);
    }
	
	@Override
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	@Override
	public String defaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CString)) return false;

        CString cString = (CString) o;

        if (getPattern() != null ? !getPattern().equals(cString.getPattern()) : cString.getPattern() != null)
            return false;
        if (getList() != null ? !getList().equals(cString.getList()) : cString.getList() != null) return false;
        if (assumedValue != null ? !assumedValue.equals(cString.assumedValue) : cString.assumedValue != null)
            return false;
        return defaultValue != null ? defaultValue.equals(cString.defaultValue) : cString.defaultValue == null;
    }

    @Override
    public int hashCode() {
        int result = getPattern() != null ? getPattern().hashCode() : 0;
        result = 31 * result + (getList() != null ? getList().hashCode() : 0);
        result = 31 * result + (assumedValue != null ? assumedValue.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
          append("pattern", pattern).
          append("list", list).
          append("assumedValue", assumedValue).
          append("defaultValue", defaultValue).
          toString();
      }

	
	/* fields */
    private final String pattern;
    private final List<String> list;
    private final String assumedValue;	
    private final String defaultValue;
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
 *  The Original Code is CString.java
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