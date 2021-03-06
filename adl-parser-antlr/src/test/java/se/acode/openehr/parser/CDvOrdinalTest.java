package se.acode.openehr.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.openehrprofile.datatypes.quantity.CDvOrdinal;
import org.openehr.am.openehrprofile.datatypes.quantity.Ordinal;
import org.openehr.rm.datatypes.text.CodePhrase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test case tests parsing of domain type constraints extension.
 *
 * @author Rong Chen
 * @version 1.0
 */

public class CDvOrdinalTest extends ParserTestBase {

    /**
     * The fixture set up called before every test method.
     */
    @Before
    public void setUp() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.c_dv_ordinal.test.adl"));
        archetype = parser.parse();
        parser.generatedParserException();
    }

    /**
     * The fixture clean up called after every test method.
     */
    @After
    public void tearDown() throws Exception {
        archetype = null;
        node = null;
    }

    @Test
    public void testCDvOrdinalWithoutAssumedValue() throws Exception {
        node = archetype.node("/types[at0001]/items[at10001]/value");
        String[] codes = {
            "at0003.0", "at0003.1", "at0003.2", "at0003.3", "at0003.4"
        };
        int[] values = { 0, 1, 2, 3, 4 };
        String terminology = "local";
        
        assertFalse("unexpected assumed value",((CDvOrdinal) node).hasAssumedValue());
        
        assertCDvOrdinal(node, terminology, codes, values, null);
    }

    @Test
    public void testCDvOrdinalWithoutDotInCode() throws Exception {
        node = archetype.node("/types[at0001]/items[at10005]/value");
        String[] codes = {
                "at0000", "at0001", "at0002", "at0003", "at0004"
        };
        int[] values = { 0, 1, 2, 3, 4 };
        String terminology = "local";

        assertFalse("unexpected assumed value",((CDvOrdinal) node).hasAssumedValue());

        assertCDvOrdinal(node, terminology, codes, values, null);
    }

    @Test
    public void testCDvOrdinalWithDuplicatedValues() throws Exception {
        node = archetype.node("/types[at0001]/items[at10004]/value");
        String[] codes = {
                "at0003.0", "at0003.1", "at0003.2", "at0003.3", "at0003.4"
        };
        int[] values = { 0, 1, 1, 3, 4 };
        String terminology = "local";

        assertFalse("unexpected assumed value",
                ((CDvOrdinal) node).hasAssumedValue());

        assertCDvOrdinal(node, terminology, codes, values, null);
    }
    
    @Test
    public void testCDvOrdinalWithAssumedValue() throws Exception {
        node = archetype.node("/types[at0001]/items[at10002]/value");
        String[] codes = {
            "at0003.0", "at0003.1", "at0003.2", "at0003.3", "at0003.4"
        };
        int[] values = { 0, 1, 2, 3, 4 };
        String terminology = "local";
        Ordinal assumed = new Ordinal(0, new CodePhrase(terminology, codes[0]));

        assertTrue("expected to have assumed value",
        		((CDvOrdinal) node).hasAssumedValue());

        assertCDvOrdinal(node, terminology, codes, values, assumed);
    }

    @Test
    public void testSNOMED() throws Exception {
        node = archetype.node("/types[at0001]/items[at10006]/value");
        String[] codes = {
                "1201000053901", "1201000053902", "1201000053903"
        };
        int[] values = { 1, 2, 3 };
        String terminology = "SNOMED-CT";

        assertFalse("expected not to have assumed value",
                ((CDvOrdinal) node).hasAssumedValue());

        assertCDvOrdinal(node, terminology, codes, values, null);
    }

    @Test
    public void testEmptyCDvOrdinal() throws Exception {
    	node = archetype.node("/types[at0001]/items[at10003]/value");
    	assertTrue("CDvOrdinal expected", node instanceof CDvOrdinal);
        CDvOrdinal cordinal = (CDvOrdinal) node;
        assertTrue(cordinal.isAnyAllowed());
    }
    
    private void assertCDvOrdinal(ArchetypeConstraint node, String terminoloy,
                                  String[] codes, int[] values, Ordinal assumedValue) {

        assertTrue("CDvOrdinal expected", node instanceof CDvOrdinal);
        CDvOrdinal cordinal = (CDvOrdinal) node;

        List<Ordinal> list = cordinal.getList();
        assertEquals("codes.size", codes.length, list.size());
        int i = 0;
        for(Ordinal o:list) {
            assertEquals("terminology", terminoloy,
                    o.getSymbol().getTerminologyId().getValue());
            assertEquals("code missing", codes[i], o.getSymbol().getCodeString());
            assertEquals("value wrong", values[i], o.getValue());
            i++;
        }
        assertEquals("assumedValue wrong", assumedValue,
                cordinal.getAssumedValue());
    }

    private Archetype archetype;
    private ArchetypeConstraint node;
}
