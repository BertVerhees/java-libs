package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchetypeInternalRefCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckWithRightReference() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v1.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithInvalidType() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v2.adl");
		assertFirstErrorType(ErrorType.VUNT);
	}

	@Test
	public void testCheckWithUnknownType() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v5.adl");
		assertFirstErrorType(ErrorType.VCORM);
	}

	@Test
	public void testCheckWithPathToWrongObject() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v3.adl");
		assertFirstErrorType(ErrorType.VUNP);
	}

	@Test
	public void testCheckWithPathToNonExistentObject() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v4.adl");
		assertFirstErrorType(ErrorType.VUNP);
	}

	@Test
	public void testCheckNoNodeIdRequired() throws Exception {
		checkInternalRef("openEHR-EHR-CLUSTER.internal_reference.v1.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithRightReferenceAndDifferentSourceParent() throws Exception {
		checkInternalRef("openEHR-EHR-OBSERVATION.internal_reference.v6.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithNonUniqueArchetypeInternalRefForSameElement() throws Exception {
		checkInternalRef("openEHR-EHR-CLUSTER.testNonUniqueInternalRef.v1.adl");
		assertFirstErrorType(ErrorType.VACMM);
	}

	@Test
	public void testCheckWithNonUniqueArchetypeInternalRefForDifferentElements() throws Exception {
		checkInternalRef("openEHR-EHR-CLUSTER.testNonUniqueInternalRef.v2.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}	
	
	private void checkInternalRef(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}
