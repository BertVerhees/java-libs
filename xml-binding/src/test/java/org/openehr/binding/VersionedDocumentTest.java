package org.openehr.binding;

import org.junit.Test;
import org.openehr.schemas.v1.VersionDocument;

public class VersionedDocumentTest extends XMLBindingTestBase {

	@Test
	public void testParsingVersionedDocument() throws Exception {
		VersionDocument vd = 
			VersionDocument.Factory.parse(
					fromClasspath("original_version_002.xml"));				
	}
}
