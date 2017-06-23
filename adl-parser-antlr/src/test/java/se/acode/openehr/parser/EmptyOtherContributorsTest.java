package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Testcase verifies parsing of abnormal archetype structures 
 * to ensure backwards compatibility on 'old' archetypes
 * 
 * @author Rong Chen
 *
 */
public class EmptyOtherContributorsTest extends ParserTestBase {

    @Test
    public void testParseEmptyOtherContributors() throws Exception {
    	ADLParser parser = new ADLParser(loadFromClasspath(
    	"adl-test-entry.empty_other_contributors.test.adl"));
    	Archetype archetype = parser.parse();
		parser.generatedParserException();
    	assertNotNull(archetype);
    	assertNull("other_contributors not null", 
    			archetype.getDescription().getOtherContributors());
    }

    private Archetype archetype;
}