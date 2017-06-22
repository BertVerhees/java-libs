package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleValueAttributeChildUniquenessCheckTest
		extends ArchetypeValidationTestBase {

	@Test
	public void testCheckChildUniquenessithRightUniqueness() throws Exception {
		checkAttribute("adl-test-ENTRY.single_attribute_child_uniqueness.v1");
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testCheckChildUniquenessWithSingleChild() throws Exception {
		checkAttribute("adl-test-ENTRY.single_attribute_child_uniqueness.v3");
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testCheckChildUniquenessWithMissingIdentifier() throws Exception {
		checkAttribute("adl-test-ENTRY.single_attribute_child_uniqueness.v2");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VACSU);
	}
	
	private void checkAttribute(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}

