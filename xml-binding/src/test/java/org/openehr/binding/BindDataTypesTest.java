package org.openehr.binding;

import org.junit.Test;
import org.openehr.rm.datatypes.quantity.DvProportion;
import org.openehr.rm.datatypes.quantity.ProportionKind;
import org.openehr.schemas.v1.DVPROPORTION;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BindDataTypesTest extends XMLBindingTestBase {

	private static final double DELTA = 1e-15;
	
	@Test
	public void testBindDvProportionToXML() throws Exception {
		DvProportion bp = new DvProportion(0.5, 1.0, ProportionKind.RATIO, null);
		Object obj = binding.bindToXML(bp);
		
		assertTrue("XML class wrong", obj instanceof DVPROPORTION);	
	}

	@Test
	public void testBindtoXMLMultipleClassAtribute() throws Exception {
		
		//Validates it uses the map to when calling getClassAttributes(final String className) in XMLBinding
		DvProportion bp = new DvProportion(0.5, 1.0, ProportionKind.RATIO, null);
		Object obj = binding.bindToXML(bp);
		assertTrue("XML class wrong", obj instanceof DVPROPORTION);	
		Object obj1 = binding.bindToXML(bp);
		assertTrue("XML class wrong", obj instanceof DVPROPORTION);	
	}



	@Test
	public void testBindXMLDvProportionToRM() throws Exception {
		DVPROPORTION xobj = DVPROPORTION.Factory.parse(
				fromClasspath("dv_proportion.xml"));
		
		assertTrue("expected dv_proportion, but got: " 
				+ (xobj == null ? null : xobj.getClass()),
				xobj instanceof DVPROPORTION);
		
		DVPROPORTION prop = (DVPROPORTION) xobj;		
		assertEquals("unexpected proportion.numerator", 0.5f, 
				prop.getNumerator(), DELTA);
		assertEquals("unexpected proportion.denominator", 1.0f, 
				prop.getDenominator(), DELTA);
		assertEquals("unexpected proportion.type", BigInteger.valueOf(0), 
				prop.getType());
		assertEquals("unexpected proportion.precision", 1, 
				prop.getPrecision());
		
		Object rmObj = binding.bindToRM(prop);
		assertTrue("expected dv_proportion", rmObj instanceof DvProportion);
	}
}