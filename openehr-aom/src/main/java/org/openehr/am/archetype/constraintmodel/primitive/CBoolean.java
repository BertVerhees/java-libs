/*
 * component:   "openEHR Reference Implementation"
 * description: "Class CBoolean"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/am/archetype/constraintmodel/primitive/CBoolean.java $"
 * revision:    "$LastChangedRevision: 23 $"
 * last_change: "$LastChangedDate: 2006-03-31 02:40:54 +0200 (Fri, 31 Mar 2006) $"
 */
package org.openehr.am.archetype.constraintmodel.primitive;

import org.openehr.rm.datatypes.basic.DvBoolean;

/**
 * Constraint on instances of Boolean. Immutable.
 * 
 * @author Rong Chen
 * @version 1.1
 */
public final class CBoolean extends CPrimitive {

	/**
	 * Constructs a BooleanConstraint with an assumed value
	 * 
	 * @param trueValid
	 * @param falseValid
	 * @param assumedValue
	 * @param hasAssumedValue
	 * @throws IllegalArgumentException
	 *             either trueValid or falseValid true
	 */
	public CBoolean(boolean trueValid, boolean falseValid, 
			        boolean assumedValue, boolean hasAssumedValue,
			        boolean defaultValue, boolean hasDefaultValue) {
		if (!trueValid && !falseValid) {
			throw new IllegalArgumentException(
					"either of trueValid or falseValid, or both need to be true");
		}
		this.trueValid = trueValid;
		this.falseValid = falseValid;
		this.assumedValue = assumedValue;
		this.hasAssumedValue = hasAssumedValue;
		this.defaultValue = defaultValue;
		this.hasDefaultValue = hasDefaultValue;
	}
	
	public CBoolean(boolean trueValid, boolean falseValid, 
	        boolean assumedValue, boolean hasAssumedValue) {
		this(trueValid, falseValid, assumedValue, hasAssumedValue, false, false);
	}

	
	/**
	 * Constructs a BooleanConstraint without an assumed value
	 * 
	 * @param trueValid
	 * @param falseValid
	 * @throws IllegalArgumentException
	 *             either trueValid or falseValid true
	 */
	public CBoolean(boolean trueValid, boolean falseValid) {
		this(trueValid, falseValid, false, false);
	}
	

	/**
	 * Return the primitive type this constraint is applied on
	 * 
	 * @return name of the type
	 */
	@Override
    public String getType() {
		return "Boolean";
	}

	/**
	 * True if value is valid with respect to constraint
	 * 
	 * @param value
	 * @return true if valid
	 */
	@Override
    public boolean validValue(Object value) {
		boolean b = ((Boolean) value).booleanValue();
		return ((b && isTrueValid()) || !b && isFalseValid());
	}

	/**
	 * True if the value True is allowed
	 * 
	 * @return if true valid
	 */
	public boolean isTrueValid() {
		return trueValid;
	}

	/**
	 * True if the value False is allowed
	 * 
	 * @return if false valid
	 */
	public boolean isFalseValid() {
		return falseValid;
	}

	@Override
	public boolean hasAssumedValue() {
		return hasAssumedValue;
	}

	@Override
	public Boolean assumedValue() {
		return assumedValue;
	}
	
	@Override
	public boolean hasAssignedValue() {
		return !(trueValid && falseValid);
	}

	@Override
	public DvBoolean assignedValue() {
		if ((trueValid && falseValid)) {
            return null;
        }
        if(trueValid) {
            return DvBoolean.TRUE;
        } else {
            return DvBoolean.FALSE;
        }
	}
	
	@Override
	public boolean hasDefaultValue() {
		return hasDefaultValue;
	}
	
	@Override
	public DvBoolean defaultValue() {
		return defaultValue ? DvBoolean.TRUE : DvBoolean.FALSE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CBoolean)) return false;

		CBoolean cBoolean = (CBoolean) o;

		if (isTrueValid() != cBoolean.isTrueValid()) return false;
		if (isFalseValid() != cBoolean.isFalseValid()) return false;
		if (assumedValue != cBoolean.assumedValue) return false;
		if (hasAssumedValue != cBoolean.hasAssumedValue) return false;
		if (defaultValue != cBoolean.defaultValue) return false;
		return hasDefaultValue == cBoolean.hasDefaultValue;
	}

	@Override
	public int hashCode() {
		int result = (isTrueValid() ? 1 : 0);
		result = 31 * result + (isFalseValid() ? 1 : 0);
		result = 31 * result + (assumedValue ? 1 : 0);
		result = 31 * result + (hasAssumedValue ? 1 : 0);
		result = 31 * result + (defaultValue ? 1 : 0);
		result = 31 * result + (hasDefaultValue ? 1 : 0);
		return result;
	}

	/* fields */
	private final boolean trueValid;
	private final boolean falseValid;
	private final boolean assumedValue;
	private final boolean hasAssumedValue;
	private final boolean defaultValue;
	private final boolean hasDefaultValue;
}

/*
 * ***** BEGIN LICENSE BLOCK ***** Version: MPL 1.1/GPL 2.0/LGPL 2.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the 'License'); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is CBoolean.java
 * 
 * The Initial Developer of the Original Code is Rong Chen. Portions created by
 * the Initial Developer are Copyright (C) 2003-2004 the Initial Developer. All
 * Rights Reserved.
 * 
 * Contributor(s):
 * 
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * ***** END LICENSE BLOCK *****
 */