package org.openehr.rm.datatypes.quantity;

import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvCodedText;

import static junit.framework.Assert.*;

public class DvOrdinalTest  {
	
	public void testCreateDvOrdinalWithNegativeValue() {
        CodePhrase definingCode = new CodePhrase("test", "123");
        DvCodedText coded = new DvCodedText("coded text", definingCode);        
        
        try {
        	new DvOrdinal(-1, coded);
        	
        } catch (IllegalArgumentException e) {
        	fail("failed to create dvOrdinal with negative value");
        }
    } 
    
    public void testParseDvOrdinal() throws Exception {
    	String value = "DV_ORDINAL,1|SNOMED-CT::313267000|Stroke|";
    	DataValue dv = DataValue.parseValue(value);
    	assertTrue(dv instanceof DvOrdinal);
    }

	public void testValueOf() throws Exception {
		DvOrdinal dvOrdinal = DvOrdinal.valueOf("1|SNOMED-CT::313267000|Stroke|");
		assertEquals(1, dvOrdinal.getValue());
		assertEquals("SNOMED-CT", dvOrdinal.getTerminologyId());
		assertEquals("313267000", dvOrdinal.getCode());
		assertEquals("Stroke", dvOrdinal.getSymbolValue());
	}
    
    public void testEquals() {
    	DvOrdinal ord1 = new DvOrdinal(1, new DvCodedText("text",
    			new CodePhrase("local", "at0002")));
    	
    	DvOrdinal ord2 = new DvOrdinal(1, new DvCodedText("text",
    			new CodePhrase("local", "at0002")));
    	
    	assertTrue(ord1.equals(ord2));
    	assertTrue(ord2.equals(ord1));
    }
}
