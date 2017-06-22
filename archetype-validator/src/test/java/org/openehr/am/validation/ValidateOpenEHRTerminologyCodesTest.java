package org.openehr.am.validation;

import org.junit.Test;

public class ValidateOpenEHRTerminologyCodesTest
		extends ArchetypeValidationTestBase {

	@Test
	public void testCheckTypeNameDvIntervalWithWrongGenericType() throws Exception {
		CheckCodes("adl-test-ELEMENT.type_multimedia.v2.adl");
	//	assertFirstErrorType(ErrorType.VOTC);
	}
	
	private void CheckCodes(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}
