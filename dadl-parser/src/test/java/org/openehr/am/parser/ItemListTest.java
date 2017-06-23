package org.openehr.am.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemListTest extends ParserTestBase {

	@Test
	public void testParseBlockWithEmptyAttrList() throws Exception {
		DADLParser parser = new DADLParser(loadFromClasspath(
			"state_item_list.dadl"));
		ContentObject obj = parser.parse();
		
		AttributeValue av = obj.getAttributeValues().get(0);
		assertEquals("state", av.getId());
		
		assertTrue(av.getValue() instanceof SingleAttributeObjectBlock);
		
		SingleAttributeObjectBlock saob = (SingleAttributeObjectBlock) av.getValue();
		assertEquals(3, saob.getAttributeValues().size());
		
		av = saob.getAttributeValues().get(2);
		assertEquals("items", av.getId());
		
		ObjectBlock ob = av.getValue();
		assertTrue(ob instanceof SingleAttributeObjectBlock);
		
		saob = (SingleAttributeObjectBlock) ob;
		assertTrue(saob.getAttributeValues().isEmpty());		
	}
}