package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Testcase that verifies parser compatibility with archetypes 
 * that have missing language part
 * 
 * @author Rong Chen 
 */
public class MissingLanguageTest extends ParserTestBase {

	@Test
	public void testMissingLanguageCompatibility() throws Exception {
		boolean missingLanguageCompatible = true;
		boolean emptyPurposeCompatible = false;
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.missing_language.test.adl"), 
				missingLanguageCompatible, emptyPurposeCompatible);
		Archetype archetype = parser.parse();
		assertNotNull(archetype);
		
		assertEquals("originalLanguage wrong", "zh", 
				archetype.getOriginalLanguage().getCodeString());
	}
}
