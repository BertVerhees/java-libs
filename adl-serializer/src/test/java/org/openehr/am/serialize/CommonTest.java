/*
 * component:   "openEHR Reference Implementation"
 * description: "Class CommonTest"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004,2005,2006 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL:$"
 * revision:    "$LastChangedRevision: $"
 * last_change: "$LastChangedDate: $"
 */
/**
 * ADLOutputterTest
 *
 * @author Rong Chen
 * @author Mattias Forss, Johan Hjalmarsson
 *
 * @version 1.0 
 */
package org.openehr.am.serialize;

import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.CAttribute;
import org.openehr.am.archetype.constraintmodel.Cardinality;
import org.openehr.rm.support.basic.Interval;
import org.openehr.rm.support.identification.ArchetypeID;
import org.openehr.rm.support.identification.HierObjectID;

public class CommonTest extends SerializerTestBase {

	@Test
	public void testPrintHeader() throws Exception {
		String id = "openEHR-EHR-EVALUATION.adverse_reaction-medication.v1";
		String parentId = "openEHR-EHR-EVALUATION.adverse_reaction.v1";
		String conceptCode = "at0000";

		clean();
		outputter.printHeader(null,	new ArchetypeID(id), 
				new ArchetypeID(parentId), null, conceptCode, out);

		verify("archetype\r\n" + "\t" + id + "\r\n" + "specialize\r\n"
				+ "\t" + parentId + "\r\n\r\n" + "concept\r\n" + "\t["
				+ conceptCode + "]\r\n");
	}

	@Test
	public void testPrintHeaderAttributes() throws Exception {
		String id = "openEHR-EHR-EVALUATION.adverse_reaction-medication.v1";

		clean();
		HierObjectID uid = new HierObjectID("23e1c56c-0a44-11e7-93ae-92361f002671");
		outputter.printHeader("1.4", new ArchetypeID(id),
				null, uid, "at0000", out);

		verify("" +
				"archetype (adl_version=1.4; uid=23e1c56c-0a44-11e7-93ae-92361f002671)\r\n" +
				"\topenEHR-EHR-EVALUATION.adverse_reaction-medication.v1\r\n" +
				"\r\n" +
				"concept\r\n" +
				"\t[at0000]\r\n");
	}

	@Test
	public void testPrintExistence() throws Exception {
		clean();
		outputter.printExistence(CAttribute.Existence.REQUIRED, out);
		verify("");

		clean();
		outputter.printExistence(CAttribute.Existence.OPTIONAL, out);
		verify("existence matches {0..1}");

		clean();
		outputter.printExistence(CAttribute.Existence.NOT_ALLOWED, out);
		verify("existence matches {0}");
	}

	@Test
	public void testPrintCardinality() throws Exception {
		clean();
		outputter.printCardinality(Cardinality.LIST, out);
		verify("cardinality matches {0..*; ordered}");

		clean();
		outputter.printCardinality(Cardinality.SET, out);
		verify("cardinality matches {0..*; unordered; unique}");

		clean();
		Cardinality cardinality = new Cardinality(true, true,
				new Interval<Integer>(2, 8));
		outputter.printCardinality(cardinality, out);
		verify("cardinality matches {2..8; ordered; unique}");
	}

	@Test
	public void testPrintInterval() throws Exception {
		clean();
		outputter.printInterval(new Interval<Integer>(0, 10), out);
		verify("|0..10|");

		clean();
		outputter.printInterval(new Interval<Integer>(null, 10, false, false),
				out);
		verify("|<10|");

		clean();
		outputter.printInterval(new Interval<Integer>(null, 10, false, true),
				out);
		verify("|<=10|");

		clean();
		outputter.printInterval(new Interval<Integer>(0, null, false, false),
				out);
		verify("|>0|");

		clean();
		outputter.printInterval(new Interval<Integer>(0, null, true, false),
				out);
		verify("|>=0|");
		
		clean();
		outputter.printInterval(new Interval<Integer>(2, 2, true, true),
				out);
		verify("|2|");		
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
 *  The Original Code is CommonTest.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2004-2006
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