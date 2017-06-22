package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchetypeDefinitionCodeCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckWithCorrectCode() throws Exception {
		checkCode("adl-test-ENTRY.definition_code.v1");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithWrongCode() throws Exception {
		checkCode("adl-test-ENTRY.definition_code.v2");
		assertFirstErrorType(ErrorType.VACCD);
	}
	
	private void checkCode(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkArchetypeDefinitionCodeValidity(archetype, errors);
	}
}
