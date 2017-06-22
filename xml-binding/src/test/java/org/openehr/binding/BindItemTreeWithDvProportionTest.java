package org.openehr.binding;

import org.junit.Test;
import org.openehr.schemas.v1.DVPROPORTION;
import org.openehr.schemas.v1.ELEMENT;
import org.openehr.schemas.v1.ITEMTREE;
import org.openehr.schemas.v1.ItemsDocument;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BindItemTreeWithDvProportionTest extends XMLBindingTestBase {
	private static final double DELTA = 1e-15;

	@Test
	public void testBindXMLDvProportionToRM() throws Exception {
		ItemsDocument xobj = ItemsDocument.Factory.parse(fromClasspath(
				"item_tree_003.xml"));
		Object obj = xobj.getItems();
		ITEMTREE tree = (ITEMTREE) obj;
		ELEMENT element = (ELEMENT) tree.getItemsArray(0);
		Object value = element.getValue();
		
		assertTrue("expected dv_proportion, but got: " 
				+ (xobj == null ? null : value.getClass()),
				value instanceof DVPROPORTION);
		
		DVPROPORTION prop = (DVPROPORTION) value;		
		assertEquals("unexpected proportion.numerator", 0.5f, 
				prop.getNumerator(), DELTA);
		assertEquals("unexpected proportion.denominator", 1.0f, 
				prop.getDenominator(), DELTA);
		assertEquals("unexpected proportion.type", BigInteger.valueOf(0), 
				prop.getType());
	}
}
