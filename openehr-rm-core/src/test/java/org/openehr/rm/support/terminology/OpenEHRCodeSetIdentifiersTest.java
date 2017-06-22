package org.openehr.rm.support.terminology;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OpenEHRCodeSetIdentifiersTest  {
	
	public void testValidateCodeSetIdWithValidId() throws Exception {
		String id = OpenEHRCodeSetIdentifiers.CHARACTER_SETS.toString();
		assertTrue("id 'character sets' should be valid",
				OpenEHRCodeSetIdentifiers.validCodeSetId(id));
	}
	
	public void testValidateCodeSetIdWithInvalidId() throws Exception {
		String id = "some unknown id";
		assertFalse(id + " shouldn't be valid", 
				OpenEHRCodeSetIdentifiers.validCodeSetId(id));
	}
}
