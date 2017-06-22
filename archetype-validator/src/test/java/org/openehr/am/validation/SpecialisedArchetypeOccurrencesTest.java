package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpecialisedArchetypeOccurrencesTest extends SpecialisedArchetypeValidationTestBase{

	@Test
	public void testCheckArchetypesWithNonConformantOccurrences() throws Exception {
		checkSpecialization("adl-specialised-EVALUATION.occurrences-specialised.v1", "adl-specialised-EVALUATION.occurrences.v1");
		assertEquals("expected one validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VSONCO);
	}
	
	
	private void checkSpecialization(String name, String parentName) throws Exception {
		archetype = loadArchetype(name);
		parentArchetype = loadArchetype(parentName);
		
		errors = validator.validate(archetype, parentArchetype, true);
	}	
}
