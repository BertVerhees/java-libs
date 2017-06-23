package se.acode.openehr.parser;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * This test case verifies that the parser supports the optional BOM (Byte Order Mark) of UTF-8.
* This BOM is introduced by many Windows based Texteditors and not treated correctly by Java without extra help.
 * 
 * It parses an ADL file (UTF-8 with BOM encoded)  to ascertain that it is parsable.  
 *
 * @author Sebastian Garde
 * @author Rong Chen
 * @version 1.0
 */
public class UnicodeBOMSupportTest extends ParserTestBase {

	/**
	 * Create new test case
	 *
	 * @param test
	 * @throws Exception
	 */
	/**
	 * Tests parsing of Chinese text in the ADL file
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParsingWithUTF8Encoding() throws Exception {
		try {
			ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.unicode_BOM_support.test.adl"), "UTF-8");
			parser.parse();
			parser.generatedParserException();
		
		} catch(Throwable t) {
			fail("failed to parse BOM with UTF8 encoding..");
		}
	}

	@Test
	public void testParsingWithoutUTF8Encoding() throws Exception {
		try {
			ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.unicode_BOM_support.test.adl"), "ISO-8859-1");
			parser.parse();
			parser.generatedParserException();
		
		} catch(Throwable t) {
			fail("failed to parse BOM without UTF8 encoding..");
		}
	}	
}
