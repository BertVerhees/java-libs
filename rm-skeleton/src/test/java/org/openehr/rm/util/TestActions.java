package org.openehr.rm.util;

import org.junit.Test;
import org.openehr.rm.composition.content.entry.Action;

import static org.junit.Assert.assertTrue;

public class TestActions extends SkeletonGeneratorTestBase {

	public TestActions() throws Exception {
		super();
	}

	@Test
	public void testWithMedicationAction() throws Exception {
		archetype = loadArchetype("openEHR-EHR-ACTION.medication.v1.adl");
		instance = generator.create(archetype);
		assertTrue("failed to create Action instance", instance instanceof Action);
	}
}
