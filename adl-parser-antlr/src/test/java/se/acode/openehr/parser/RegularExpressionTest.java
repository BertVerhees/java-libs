package se.acode.openehr.parser;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RegularExpressionTest extends ParserTestBase {

	@Test
	public void testParseRegularExpressions() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
			"adl-test-entry.regular_expression.test.adl"));
		assertNotNull(parser.parse().getDefinition());
	}	
}
