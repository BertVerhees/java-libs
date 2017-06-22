package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SectionCardinalityTest
		extends ArchetypeValidationTestBase {

	@Test
	public void testCheckSectionItemsCardinality() throws Exception {
		checkObjectConstraints("openEHR-EHR-SECTION.testitemscardinality.v1.adl");
		assertEquals("expected NO validation error", 0, errors.size());			
	}
	
	
	private void checkObjectConstraints(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}

