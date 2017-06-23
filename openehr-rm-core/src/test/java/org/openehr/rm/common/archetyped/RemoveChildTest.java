package org.openehr.rm.common.archetyped;

import org.junit.Test;
import org.openehr.rm.datastructure.itemstructure.ItemList;
import org.openehr.rm.datastructure.itemstructure.representation.Element;
import org.openehr.rm.datatypes.text.DvText;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RemoveChildTest  {

	@Test
	public void testRemove1stChildFromList() throws Exception {
		ItemList list = itemList();
		int count = list.itemCount();
		String path = "/items[at0001]";
		
		list.removeChild(path);
		assertTrue("child not removed", list.itemCount() == count - 1);
		assertNull("child not removed", list.itemAtPath(path));
	}

	@Test
	public void testRemove1stChildFromListWithNamePredicate() throws Exception {
		ItemList list = itemList();
		int count = list.itemCount();
		String path = "/items[at0001 and name/value='element 1']";
		
		list.removeChild(path);
		assertTrue("child not removed", list.itemCount() == count - 1);
		assertNull("child not removed", list.itemAtPath(path));
	}

	@Test
	public void testRemove2ndChildFromList() throws Exception {
		ItemList list = itemList();
		int count = list.itemCount();
		String path = "/items[at0002]";
		
		list.removeChild(path);
		assertTrue("child not removed", list.itemCount() == count - 1);
		assertNull("child not removed", list.itemAtPath(path));
	}

	@Test
	public void testRemove3rdChildFromList() throws Exception {
		ItemList list = itemList();
		int count = list.itemCount();
		String path = "/items[at0003]";
		
		list.removeChild(path);
		assertTrue("child not removed", list.itemCount() == count - 1);
		assertNull("child not removed", list.itemAtPath(path));
	}

	@Test
	public void testRemoveUnknownChildFromList() throws Exception {
		ItemList list = itemList();
		int count = list.itemCount();
		String path = "/items[at0099]";
		try {
			list.removeChild(path);
			fail("exception should be thrown on unknonw child");
		} catch(Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		assertTrue("child should not be removed", 
				list.itemCount() == count);		
	}
	
	ItemList itemList() {
		List<Element> items = new ArrayList<Element>();
		for(int i = 1; i <= 3; i++) {
			DvText text = new DvText("text " + i);
			Element element = new Element("at000" + i, "element " + i, text);		
			items.add(element);
		}		
		ItemList list = new ItemList("at0000", "list", items);
		return list;
	}
}
