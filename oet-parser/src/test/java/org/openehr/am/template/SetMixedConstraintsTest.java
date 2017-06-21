package org.openehr.am.template;

import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.*;

public class SetMixedConstraintsTest extends TemplateTestBase {
	
	/*
	 * Test setting name, occurrences and c_dv_quantity at same time
	 * that can trigger 1) missed mandatory node;  
	 */
	@Test
	public void testSeNodeOccurrencesAndNameConstraint() throws Exception {
		flattenTemplate("test_set_mixed_constraints.oet");		
		path = "/items[openEHR-EHR-OBSERVATION.waist_hip.v2]/" +
			"data[at0001]/events[at0002]/data[at0003]/" +
			"items[at0004 and name/value='Bukomfång']";
		
		CComplexObject element = (CComplexObject) flattened.node(path);
		assertEquals("unexpected num of attributes", 2, 
				element.getAttributes().size());
	}

	@Test
	public void testTopNodeNameConstraint() throws Exception {
		flattenTemplate("test_set_mixed_constraints.oet");		
		
		// name attribute
		ArchetypeConstraint nameConstraint = flattened.node("/name/value");
		this.assertCStringWithSingleValue(nameConstraint, "STATUS");
	}
	
	private String path;
}

