package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssumedValueTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckWrongQuantityAssumedValue() throws Exception {
		CheckAssumedValue("adl-test-ELEMENT.assumed_value_quantity.v1.adl");
		assertEquals("expected wrong assumed value error", 1,	errors.size());	
			assertFirstErrorType(ErrorType.VOBAV);
	}

	@Test
	public void testCheckWrongBooleanAssumedValue() throws Exception {
		CheckAssumedValue("adl-test-ELEMENT.assumed_value_boolean.v1.adl");
		assertEquals("expected wrong assumed value error", 1,	errors.size());	
			assertFirstErrorType(ErrorType.VOBAV);
	}

	@Test
	public void testCheckWrongCountAssumedValue() throws Exception {
		CheckAssumedValue("adl-test-ELEMENT.assumed_value_boolean.v1.adl");
		assertEquals("expected wrong assumed value error", 1,	errors.size());	
			assertFirstErrorType(ErrorType.VOBAV);
	}
	
	private void CheckAssumedValue(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}
