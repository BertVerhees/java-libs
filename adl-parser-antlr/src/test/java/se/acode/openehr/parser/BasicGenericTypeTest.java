package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertNotNull;

public class BasicGenericTypeTest extends ParserTestBase {
    @Test
    public void testParse() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-SOME_TYPE.generic_type_basic.draft.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();
        assertNotNull(archetype);
	}
}
