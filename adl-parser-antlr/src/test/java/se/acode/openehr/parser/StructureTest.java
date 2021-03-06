package se.acode.openehr.parser;

import org.junit.Before;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.CAttribute;
import org.openehr.am.archetype.constraintmodel.CComplexObject;
import org.openehr.am.archetype.constraintmodel.CPrimitiveObject;
import org.openehr.am.archetype.constraintmodel.Cardinality;
import org.openehr.am.archetype.constraintmodel.primitive.CString;
import org.openehr.rm.support.basic.Interval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case tests parsing objet structures with archetypes.
 *
 * @author Rong Chen
 * @version 1.0
 */
public class StructureTest extends ParserTestBase {

    @Before
    public void setUp() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.structure_test1.test.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();
        definition = archetype.getDefinition();
    }

    public void tearDown() throws Exception {
        definition = null;

    }

    @Test
    public void testStructure() throws Exception {

        // root object
        CComplexObject obj = definition;
        Interval<Integer> occurrences = new Interval<Integer>(1, 1);
        assertCComplexObject(obj, "ENTRY", "at0000", occurrences, 2);

        // first attribute of root object
        CAttribute attr = (CAttribute) obj.getAttributes().get(0);
        assertCAttribute(attr, "subject_relationship", 1);

        // 2nd level object
        obj = (CComplexObject) attr.getChildren().get(0);
        assertCComplexObject(obj, "RELATED_PARTY", null, occurrences, 1);

        // attribute of 2nd level object
        attr = (CAttribute) obj.getAttributes().get(0);
        assertCAttribute(attr, "relationship", 1);

        // leaf object
        obj = (CComplexObject) attr.getChildren().get(0);
        assertCComplexObject(obj, "TEXT", null, occurrences, 1);

        // attribute of leaf object
        attr = (CAttribute) obj.getAttributes().get(0);
        assertCAttribute(attr, "value", 1);

        // primitive constraint of leaf object
        CString str = (CString) ((CPrimitiveObject) attr.getChildren().get(0)).getItem();
        assertEquals("pattern", null, str.getPattern());
        assertEquals("set.size", 1, str.getList().size());
        assertTrue("set has", str.getList().contains("self"));
    }

    @Test
    public void testExistenceCardinalityAndOccurrences() throws Exception {
        // second attribute of root object
        CAttribute attr = (CAttribute) definition.getAttributes().get(1);
        Cardinality card = new Cardinality(true, false, interval(0, 8));
        assertCAttribute(attr, "members", CAttribute.Existence.OPTIONAL, card, 2);

        // 1st PERSON
        CComplexObject obj = (CComplexObject) attr.getChildren().get(0);
        assertCComplexObject(obj, "PERSON", null, interval(1, 1), 1);

        // 2nd PERSON
        obj = (CComplexObject) attr.getChildren().get(1);
        assertCComplexObject(obj, "PERSON", null,
                new Interval(new Integer(0), null, true, false), 1);
    }

    @Test
    public void testParseCommentWithSlashChar() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.structure_test2.test.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();
        assertNotNull(archetype);        
    }

    @Test
    public void testParseCommentWithSlashCharAfterSlot() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "openEHR-EHR-CLUSTER.auscultation.v1.adl"));
        Archetype archetype = parser.parse();
        parser.generatedParserException();
        assertNotNull(archetype);        
    }

    private CComplexObject definition;
}
