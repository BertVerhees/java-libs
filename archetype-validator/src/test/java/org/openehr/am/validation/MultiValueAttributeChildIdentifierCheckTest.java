package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiValueAttributeChildIdentifierCheckTest
		extends ArchetypeValidationTestBase {

	@Test
	public void testCheckMultiAttributeChildIdentifierWithGoodIds() throws Exception {
		checkAttribute("adl-test-ITEM_TREE.multi_attribute_child_identifier.v1");
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testCheckMultiAttributeChildIdentifierWithMissingId() throws Exception {
		checkAttribute("adl-test-ITEM_TREE.multi_attribute_child_identifier.v2");
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VACMI);
	}

	@Test
	public void testCheckMultiAttributeChildIdentifierWithDuplicatedIds() throws Exception {
		checkAttribute("adl-test-ITEM_TREE.multi_attribute_child_identifier.v3");
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VACMM);
	}
	
	private void checkAttribute(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}

