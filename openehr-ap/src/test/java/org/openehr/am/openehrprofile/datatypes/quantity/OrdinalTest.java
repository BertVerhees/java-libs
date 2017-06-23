package org.openehr.am.openehrprofile.datatypes.quantity;

import org.junit.Test;
import org.openehr.rm.datatypes.text.CodePhrase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OrdinalTest  {
	@Test
	public void testEquals() {
		Ordinal o1 = new Ordinal(1, new CodePhrase("local", "at0001"));
		Ordinal o2 = new Ordinal(1, new CodePhrase("local", "at0001"));
		assertTrue("equals expected", o1.equals(o2));
		assertTrue("equals expected", o2.equals(o1));
	}

	@Test
	public void testCreateOrdinalWithNegativeValue() {
		try {
			new Ordinal(-1, new CodePhrase("local", "at0001"));
		} catch(Exception e) {
			fail("failed to create ordinal with negative value");
		}
	}
}
