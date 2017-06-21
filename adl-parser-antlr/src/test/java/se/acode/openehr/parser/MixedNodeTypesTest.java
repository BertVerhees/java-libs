package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class MixedNodeTypesTest extends ParserTestBase {

	@Test
	public void testMixedNodeTypes() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
			"adl-test-entry.mixed_node_types.draft.adl"));
		
		try {
			Archetype archetype = parser.parse();
			assertNotNull(archetype);
		} catch(Exception e) {
			e.printStackTrace();
			fail("failed to parse mixed node types");
		}
	}
}
