package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpecialisedArchetypeWrongRMTypeTest extends SpecialisedArchetypeValidationTestBase{



	@Test
	public void testCheckArchetypesWithConformantRMTypeForChoice() throws Exception {
		checkSpecialization("adl-specialised-EVALUATION.wrongrmtype-specialised.v1", "adl-specialised-EVALUATION.wrongrmtype.v1");
		assertEquals("expected no validation errors", 0, errors.size());	
	}

	@Test
	public void testCheckArchetypesWithNonConformantRMType() throws Exception {
		checkSpecialization("adl-specialised-EVALUATION.wrongrmtype-specialised.v2", "adl-specialised-EVALUATION.wrongrmtype.v2");
		assertEquals("expected one validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VSONCT);
	}

	@Test
	public void testCheckArchetypesWithRMTypeWithGenericsWrong() throws Exception {
		checkSpecialization("adl-specialised-EVALUATION.wrongrmtype-specialised.v3", "adl-specialised-EVALUATION.wrongrmtype.v3");
		assertEquals("expected one validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VSONCT);
	}
	
	private void checkSpecialization(String name, String parentName) throws Exception {
		archetype = loadArchetype(name);
		parentArchetype = loadArchetype(parentName);
		
		errors = validator.validate(archetype, parentArchetype, true);
	}	
}
