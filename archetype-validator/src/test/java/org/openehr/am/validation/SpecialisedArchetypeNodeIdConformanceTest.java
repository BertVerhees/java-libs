package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpecialisedArchetypeNodeIdConformanceTest extends SpecialisedArchetypeValidationTestBase{

	@Test
	public void testCheckArchetypesWithNonConformantMultiplicity() throws Exception {
		checkSpecialization("adl-specialised-EVALUATION.node_id_conformance-specialised-again.v1", "adl-specialised-EVALUATION.node_id_conformance-specialised.v1");
		assertEquals("expected one validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VSONCI);
	}
	
	
	private void checkSpecialization(String name, String parentName) throws Exception {
		archetype = loadArchetype(name);
		parentArchetype = loadArchetype(parentName);
		errors = validator.validate(archetype, parentArchetype, true);
	}	
}
