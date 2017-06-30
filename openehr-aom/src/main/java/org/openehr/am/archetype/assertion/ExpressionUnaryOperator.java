/*
 * component:   "openEHR Reference Implementation"
 * description: "Class ExpressionUnaryOperator"
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

public class ExpressionUnaryOperator extends ExpressionOperator {

	public ExpressionUnaryOperator(String type, OperatorKind operator,
			boolean precedenceOverridden, ExpressionItem operand) {
		super(type, operator, precedenceOverridden);
		this.operand = operand;
	}

	public ExpressionItem getOperand() {
		return operand;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionUnaryOperator)) return false;
        if (!super.equals(o)) return false;

        ExpressionUnaryOperator that = (ExpressionUnaryOperator) o;

        return getOperand() != null ? getOperand().equals(that.getOperand()) : that.getOperand() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getOperand() != null ? getOperand().hashCode() : 0);
        return result;
    }

    /**
     * Returns the String representation of this unary operator
     * 
     * @return string 
     */
    public String toString() {
    	return getOperator() + " " + operand.toString();
    }

	private ExpressionItem operand;
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
 *  The Original Code is ExpressionUnaryOperator.java
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