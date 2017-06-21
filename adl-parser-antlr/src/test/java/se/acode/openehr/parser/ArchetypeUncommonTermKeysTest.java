package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.ontology.ArchetypeTerm;

import static org.junit.Assert.assertEquals;


/**
 * Testcase for uncommon term keys (other than text, description and comment)
 *
 * @author Sebastian Garde
 * @version 1.0
 */
public class ArchetypeUncommonTermKeysTest extends ParserTestBase {

	/**
	 * Verifies the content of description instance after parsing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testArchetypeUncommonTerm() throws Exception {
		ADLParser parser = new ADLParser(
				loadFromClasspath("adl-test-entry.archetype_uncommonkeys.test.adl"));
		Archetype archetype = parser.parse();
		ArchetypeTerm aterm = archetype.getOntology().termDefinition("at0000");
	
		assertEquals("key value wrong", "another key value", aterm.getItem("anotherkey"));
		assertEquals("key value wrong", "test text", aterm.getItem("text"));
		assertEquals("key value wrong", "test description", aterm.getItem("description"));
	}

	
}

