/*
 * component:   "openEHR Reference Implementation"
 * description: "Class CDvOrdinal"
 * keywords:    "openehr archetype profile"
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

package org.openehr.am.openehrprofile.datatypes.quantity;

import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.archetype.constraintmodel.CAttribute;
import org.openehr.am.archetype.constraintmodel.CComplexObject;
import org.openehr.am.archetype.constraintmodel.CDomainType;
import org.openehr.rm.support.basic.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class specifying constraints on instances of DV_ORDINAL. Custom 
 * constraint type for instances of DV_ORDINAL.
 * 
 * @author Rong Chen
 * @version 1.0
 */
public class CDvOrdinal extends CDomainType<Ordinal> {

	/**
	 * Creates a CDvOrdinal
	 * 
	 * @param path
	 * @param occurrences
	 * @param nodeID
	 * @param list
	 */
	public CDvOrdinal(String path, Interval<Integer> occurrences,
			String nodeID, CAttribute parent, List<Ordinal> list,
			Ordinal defaultValue, Ordinal assumedValue) {
		super(list == null, path, "DV_ORDINAL", occurrences, nodeID, defaultValue,
				assumedValue, parent);
		if (list != null && list.isEmpty()) {
			throw new IllegalArgumentException("list is empty");
		}
		this.list = list == null ? null : new ArrayList<Ordinal>(list);
	}
	
	@Override
    public CDvOrdinal copy() {
		return new CDvOrdinal(path(), getOccurrences(), getNodeId(), getParent(), list,				
			getDefaultValue(), getAssumedValue());
	}
	
	/**
	 * Convenience constructor
	 * 
	 * @param path
	 * @param occurrences
	 * @param list
	 */
	public CDvOrdinal(String path, Interval<Integer> occurrences, 
			List<Ordinal> list) {
		this(path, occurrences, null, null, list, null, null);
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPath(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubsetOf(ArchetypeConstraint constraint) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * List of allowed DV_ORDINAL values.
	 * 
	 * @return unmodifiable set of Ordinal
	 */
	public List<Ordinal> getList() {
		if (list == null) {
			return null;
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CDvOrdinal)) return false;
		if (!super.equals(o)) return false;

		CDvOrdinal that = (CDvOrdinal) o;

		return getList() != null ? getList().equals(that.getList()) : that.getList() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getList() != null ? getList().hashCode() : 0);
		return result;
	}

	@Override
	public boolean validValue(Ordinal arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CComplexObject standardEquivalent() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Ordinal> list;
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
 *  The Original Code is CDvOrdinal.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2003-2008
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