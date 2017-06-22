package org.openehr.rm.util;

import org.junit.Test;
import org.openehr.rm.composition.content.entry.Observation;

import static org.junit.Assert.assertTrue;

public class TestEvents extends SkeletonGeneratorTestBase {

	public TestEvents() throws Exception {
		super();
	}

	@Test
	public void testWithApgarScoreArchetype_PointEvent() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.apgar.v1.adl");
		instance = generator.create(archetype, GenerationStrategy.MAXIMUM);
		assertTrue("failed to create Action instance", instance instanceof Observation);
	}
	
	/*
	public void testWithBloodPressureArchetype_Interval() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.blood_pressure.v1.adl");
		instance = generator.create(archetype, GenerationStrategy.MAXIMUM);
		assertTrue("failed to create Action instance", instance instanceof Observation);
	}
	*/

}