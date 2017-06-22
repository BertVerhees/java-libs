package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchetypeSlotCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckWithRightArchetypeIdsInSlot() throws Exception {
		checkSlot("openEHR-EHR-OBSERVATION.slot.v1.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithInvalidSlot() throws Exception {
		checkSlot("openEHR-EHR-OBSERVATION.slot.v2.adl");
		assertEquals("wrong number of validation errors: " + errors, 4,	
				errors.size());	
		assertFirstErrorType(ErrorType.VDFAI);
		assertSecondErrorType(ErrorType.VDFAI);
	}
	
	
	private void checkSlot(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}
