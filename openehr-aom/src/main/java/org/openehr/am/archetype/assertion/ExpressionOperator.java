/*
 * component:   "openEHR Reference Implementation"
 * description: "Class ExpressionOperator"
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

public abstract class ExpressionOperator extends ExpressionItem {
	private OperatorKind operator;
	private boolean precedenceOverridden;
	public OperatorKind getOperator() {
		return operator;
	}
	public boolean isPrecedenceOverridden() {
		return precedenceOverridden;
	}
	
	public ExpressionOperator(String type, OperatorKind operator, boolean precedenceOverridden) {
		super(type);
		this.operator = operator;
		this.precedenceOverridden = precedenceOverridden;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExpressionOperator)) return false;
		if (!super.equals(o)) return false;

		ExpressionOperator that = (ExpressionOperator) o;

		if (isPrecedenceOverridden() != that.isPrecedenceOverridden()) return false;
		return getOperator() == that.getOperator();
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getOperator() != null ? getOperator().hashCode() : 0);
		result = 31 * result + (isPrecedenceOverridden() ? 1 : 0);
		return result;
	}
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
 *  The Original Code is ExpressionOperator.java
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