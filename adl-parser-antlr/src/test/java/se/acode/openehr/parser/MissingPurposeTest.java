package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertNotNull;

public class MissingPurposeTest extends ParserTestBase {
	@Test
	public void testMissingLanguageCompatibility() throws Exception {
		boolean missingLanguageCompatible = false;
		boolean emptyPurposeCompatible = true;
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_desc_missing_purpose.test.adl"), 
				missingLanguageCompatible, emptyPurposeCompatible);
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		assertNotNull(archetype);
		
		assertNotNull("purpose null", 
				archetype.getDescription().getDetails().get("en").getPurpose());
	}
}
