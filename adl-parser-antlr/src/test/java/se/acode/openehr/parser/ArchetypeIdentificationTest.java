package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * MostMinimalADLTest
 *
 * @author Rong Chen
 * @version 1.0
 */
public class ArchetypeIdentificationTest extends ParserTestBase {

    /**
     * Create new test case
     *
     * @param test
     * @throws Exception
     */

    @Test
    public void testParse() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_identification.test.adl"));
        Archetype archetype = parser.parse();
        assertNotNull(archetype);
        assertEquals("adl_version wrong", "1.4", archetype.getAdlVersion());
        assertEquals("uid wrong", null, archetype.getUid());
    }

    @Test
    public void testWithUUIDUid() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "openEHR-EHR-ELEMENT.uid_test.v1.adl"));
        Archetype archetype = parser.parse();
        assertNotNull(archetype);
        assertEquals("adl_version wrong", "1.4", archetype.getAdlVersion());
        assertEquals("uid wrong", "89595ca5-ae63-4150-abe8-fd2a13061efd", archetype.getUid().toString());
        assertEquals("controlled flag wrong", false, archetype.isControlled());
    }

    @Test
    public void testWithHIEROBJECTIDUid() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "openEHR-EHR-ELEMENT.uid_test.v2.adl"));
        Archetype archetype = parser.parse();
        assertNotNull(archetype);
        assertEquals("adl_version wrong", "1.4", archetype.getAdlVersion());
        assertNull("uid must be null", archetype.getUid());
        assertEquals("controlled flag wrong", false, archetype.isControlled());
    }

}

