package org.openehr.rm.util;

import org.junit.Test;

public class TestObservations extends SkeletonGeneratorTestBase {

	public TestObservations() throws Exception {
		super();
	}

	@Test
	public void testWithBloodPressureArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.blood_pressure.v2.adl");
		instance = generator.create(archetype);
		
	}

	@Test
	public void testWithHeightArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.height.v2.adl");
		instance = generator.create(archetype);
	}

	@Test
	public void testWithWeightArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.body_weight.v2.adl");
		instance = generator.create(archetype);		
	}

	@Test
	public void testWithWaistArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.waist_hip.v2.adl");
		instance = generator.create(archetype);
	}

	@Test
	public void testWithHeartRateArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.heart_rate.v2.adl");
		instance = generator.create(archetype);
	}

	@Test
	public void testWithLabArchetype() throws Exception {
		archetype = loadArchetype("openEHR-EHR-OBSERVATION.lab_test.v1.adl");
		instance = generator.create(archetype, GenerationStrategy.MAXIMUM_EMPTY);
	}
}
