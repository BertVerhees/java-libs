package org.openehr.rm.support.terminology;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OpenEHRTerminologyGroupIdentifiersTest  {

	@Test
	public void testValidateTerminologyGroupIdWithValidId() {
		String id = 
			OpenEHRTerminologyGroupIdentifiers.ATTESTATION_REASON.toString();
		assertTrue(id + " should be valid", 
				OpenEHRTerminologyGroupIdentifiers.validTerminologyGroupId(id));
	}

	@Test
	public void testValidateTerminologyGroupIdWithInvalidId() {
		String id = "unknow terminology group id";
		assertFalse(id + " should not be valid", 
				OpenEHRTerminologyGroupIdentifiers.validTerminologyGroupId(id));
	}
}
