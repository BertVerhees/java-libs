package org.openehr.am.parser;

import org.junit.Test;

public class StructureTest extends ParserTestBase {

	@Test
	public void testParseSimpleDADL() throws Exception {
		DADLParser parser = new DADLParser(loadFromClasspath(
			"blood_pressure_001.dadl"));
		parser.parse();
	}

	@Test
	public void testTypedObjectWithKeyedAttributes() throws Exception {
		DADLParser parser = new DADLParser(loadFromClasspath(
			"person_001.dadl"));
		parser.parse();
	}
}
