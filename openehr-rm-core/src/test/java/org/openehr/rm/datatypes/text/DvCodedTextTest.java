/**
 * DvCodedTextTest
 *
 * @author Rong Chen
 * @version 1.0 
 */
package org.openehr.rm.datatypes.text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DvCodedTextTest {

    /**
     * Tests creating a dvCodedText with minimum set of parameters
     *
     * @throws Exception
     */
    @Test
    public void testCreateDvCodedTextWithMinimumParam() throws Exception {
        CodePhrase definingCode = new CodePhrase("test terms", "12345");
        new DvCodedText("coded text", definingCode);
    }

    @Test
    public void testEquals() throws Exception {
    	DvCodedText t1 = new DvCodedText("some text", "icd10", "123");
    	DvCodedText t2 = new DvCodedText("some text", "icd10", "123");
    	assertEquals(t1, t2);
    }

    @Test
    public void testValueOf() {
        DvCodedText dvCodedText = DvCodedText.valueOf("ATC::B01AC05|ticlopidine|");
        assertEquals("ATC", dvCodedText.getTerminologyId());
        assertEquals("B01AC05", dvCodedText.getCode());
        assertEquals("ticlopidine", dvCodedText.getValue());
    }
}