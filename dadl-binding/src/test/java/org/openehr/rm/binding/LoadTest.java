package org.openehr.rm.binding;

import org.junit.Test;
import org.openehr.rm.composition.content.entry.Observation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoadTest extends DADLBindingTestBase {

	@Test
	public void testLoadHeight() throws Exception {
		rmObj = bind("observation2.dadl");
		assertNotNull("rmObject null", rmObj);
		assertTrue("rmObject not Observation: "	+ rmObj.getClass().getName(), 
				rmObj instanceof Observation);
	}

	@Test
	public void testLoadBodyWegith() throws Exception {
		rmObj = bind("body_weight.dadl");
		assertNotNull("rmObject null", rmObj);
		assertTrue("rmObject not Observation: "	+ rmObj.getClass().getName(), 
				rmObj instanceof Observation);
	}

	@Test
	public void testLoadDemographics() throws Exception {
		rmObj = bind("demographics.dadl");
		assertNotNull("rmObject null", rmObj);
		assertTrue("rmObject not Observation: "	+ rmObj.getClass().getName(), 
				rmObj instanceof Observation);
	}
}
