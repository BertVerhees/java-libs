package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefinitionTypenameCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckTypenameWithWrongType() throws Exception {
		checkTypename("adl-test-ENTRY.definition_typename.v1");		
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VARDT);
	}

	@Test
	public void testCheckTypenameWithRightType() throws Exception {
		checkTypename("adl-test-ENTRY.definition_typename.v2");		
		assertEquals("expected no validation error", 0,	errors.size());	
		
		checkTypename("adl-test-ENTRY.definition_typename.v3");		
		assertEquals("expected no validation error", 0,	errors.size());	
	}
	
	private void checkTypename(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkArchetypeDefinitionTypename(archetype, errors);
	}

}
