package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DvCodedTextTest extends ParserTestBase {

    @Test
    public void testParse() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-composition.dv_coded_text.test.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();
        assertNotNull(archetype);
        
        ArchetypeConstraint node = archetype.node("/category/defining_code");
        assertTrue("CCodePhrase expected, but got " + node.getClass(),
        		node instanceof CCodePhrase);
        CCodePhrase ccp = (CCodePhrase) node;
        assertEquals("terminologyId wrong", "openehr", 
        		ccp.getTerminologyId().toString());
        assertEquals("codeString wrong", "431", ccp.getCodeList().get(0));
	}
}
