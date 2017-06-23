package org.openehr.rm.support.terminology;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OpenEHRTerminologyGroupIdentifiersTest  {

	public void testValidateTerminologyGroupIdWithValidId() {
		String id = 
			OpenEHRTerminologyGroupIdentifiers.ATTESTATION_REASON.toString();
		assertTrue(id + " should be valid", 
				OpenEHRTerminologyGroupIdentifiers.validTerminologyGroupId(id));
	}
	
	public void testValidateTerminologyGroupIdWithInvalidId() {
		String id = "unknow terminology group id";
		assertFalse(id + " should not be valid", 
				OpenEHRTerminologyGroupIdentifiers.validTerminologyGroupId(id));
	}
}
