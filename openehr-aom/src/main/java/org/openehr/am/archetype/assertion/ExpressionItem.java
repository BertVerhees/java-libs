/*
 * component:   "openEHR Reference Implementation"
 * description: "Class ExpressionItem"
 * keywords:    "archetype assertion"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2006 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL$"
 * revision:    "$LastChangedRevision$"
 * last_change: "$LastChangedDate$"
 */
 
package org.openehr.am.archetype.assertion;

import java.io.Serializable;

public abstract class ExpressionItem implements Serializable{
	
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
    //Possible type names of this item in the mathematical sense. For leaf nodes, must be the name of a
    //primitive type, or else a reference model type. The type for any relational or boolean
    //operator will be “Boolean”, while the type for any arithmetic operator, will be “Real” or “Integer”. (AOM spec for EXPR_ITEM)
	public final static String BOOLEAN = "Boolean"; 
	public final static String REAL = "Real";
	public final static String INTEGER = "Integer";
	public final static String STRING = "String";
	public final static String ARCHETYPE = "Archetype"; //SG: Not sure this type makes sense?
	public final static String RM = "RM";	//SG: Not sure this type makes sense?
	
	public ExpressionItem(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	/**
	 * Checks if type is BOOLEAN
	 *  
	 * @return true if type is BOOLEAN
	 */
	public boolean isTypeBoolean() {
		return BOOLEAN.equals(type);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExpressionItem)) return false;

		ExpressionItem that = (ExpressionItem) o;

		return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
	}

	@Override
	public int hashCode() {
		return getType() != null ? getType().hashCode() : 0;
	}

	@Override
    public abstract String toString();
	
	private String type;
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
 *  The Original Code is ExpressionItem.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2003-2010
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s): Sebastian Garde
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */