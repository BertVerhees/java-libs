package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.archetype.constraintmodel.ConstraintRef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConstraintRefTest extends ParserTestBase {

        @Test
        public void testParseConstraintRef() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.constraint_ref.test.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();

        assertNotNull(archetype);
        
        String path = "/content[at0001]/items[at0002]/value/defining_code";
        ArchetypeConstraint node = archetype.node(path);        
        assertNotNull("node not found at path" + path, node);
        
        assertTrue("ConstraintRef expected, instead got: " + node.getClass(), 
        		node instanceof ConstraintRef);
        
        ConstraintRef ref = (ConstraintRef) node;
        assertEquals("reference wrong", "ac0001", ref.getReference());
    }
}
