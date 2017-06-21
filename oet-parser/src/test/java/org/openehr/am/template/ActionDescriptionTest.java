package org.openehr.am.template;

import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;

public class ActionDescriptionTest extends TemplateTestBase {
	
	/**
	 * Verify flattened section with a single evaluation
	 * 
	 * @throws Exception
	 */
	@Test
	public void testActionDescriptionSlotFilling() throws Exception {
		flattenTemplate("test_action_description.oet");		
				
		// section level
		ac = flattened.node("/description[openEHR-EHR-ITEM_TREE.medication.v1]");
		assertItemTreeConstraint(ac);
	}
	
	private ArchetypeConstraint ac;
}
