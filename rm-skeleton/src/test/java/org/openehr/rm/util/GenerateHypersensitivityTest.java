package org.openehr.rm.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class GenerateHypersensitivityTest extends SkeletonGeneratorTestBase {

	public GenerateHypersensitivityTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testGenerateSimpleHypersensitivity() throws Exception {
		Archetype archetype = loadArchetype("openEHR-EHR-EVALUATION.hypersensitivity.v1.adl");
		assertNotNull(archetype);
		
		Object obj = generator.create(archetype, null, archetypeMap,
				GenerationStrategy.MAXIMUM);
		
		List<String> lines = dadlBinding.toDADL(obj);
		
		FileUtils.writeLines(new File("hypersensitivity_max.dadl"), lines);
		
		generator.create(archetype, null, archetypeMap,
				GenerationStrategy.MINIMUM);
		
		lines = dadlBinding.toDADL(obj);		
		FileUtils.writeLines(new File("hypersensitivity_min.dadl"), lines);
	}
}
