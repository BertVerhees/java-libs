package org.openehr.am.template;

import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.*;

public class SetNestedEvaluationNameTest extends TemplateTestBase {

	@Test
	public void testTopNodeNameConstraint() throws Exception {
		flattenTemplate("test_set_evaluation_name.oet");		
		
		String path = "/items[openEHR-EHR-EVALUATION.review_of_procedures.v1" +
				" and name/value='eval']/name/value";		
		
		// name attribute
		ArchetypeConstraint nameConstraint = flattened.node(path);
		this.assertCStringWithSingleValue(nameConstraint, "eval");
	}
	
	private String path;
}

