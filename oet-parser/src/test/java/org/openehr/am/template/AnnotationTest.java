package org.openehr.am.template;

import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;

public class AnnotationTest extends TemplateTestBase {

	@Test
	public void testSingleSectionWithName() throws Exception {
		flattenTemplate("test_annotation.oet");
		
		ArchetypeConstraint ac = flattened.node("/items[at0001]");
		assertEquals("status", ac.getAnnotation()); 
	}	
}
