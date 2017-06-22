package org.openehr.rm.datastructure.itemstructure.representation;

import org.openehr.rm.datatypes.text.DvCodedText;
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.rm.support.terminology.TerminologyService;
import org.openehr.rm.support.terminology.TestTerminologyAccess;
import org.openehr.rm.support.terminology.TestTerminologyService;

public class CreateEmptyElementTest  {

	public void testCreateElement() {
		TerminologyService ts = TestTerminologyService.getInstance();
		DvCodedText nullFlavor = new DvCodedText("no information", 
				TestTerminologyAccess.NULL_FLAVOUR); 
		
		new Element(null, "at0001", new DvText("name"), null, null, null, null, 
				null, nullFlavor, ts);
    }	
}
