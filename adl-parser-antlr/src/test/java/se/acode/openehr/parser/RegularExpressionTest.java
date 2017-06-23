package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertNotNull;

public class RegularExpressionTest extends ParserTestBase {

	@Test
	public void testParseRegularExpressions() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
			"adl-test-entry.regular_expression.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		assertNotNull(archetype.getDefinition());
	}	
}
