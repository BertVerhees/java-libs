package org.openehr.am.validation;

import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ValidatorUtilityTest extends ArchetypeValidationTestBase {

	@Test
	public void testFetchAllATCodes() throws Exception {
		archetype = loadArchetype("openEHR-EHR-EVALUATION.problem-diagnosis.v1.adl");
		
		Set<String> codes = validator.fetchAllATCodes(archetype);
		assertNotNull("fetched AT codes null", codes);
		int expectedNumOfATCodes = archetype.getOntology()
					.getTermDefinitionsList().get(1).getDefinitions().size();
		
		assertTrue("main concept at0000.1 missing", codes.contains("at0000.1"));
		
		assertTrue("archetype node at0001 missing", codes.contains("at0001"));
		
		assertTrue("at0.33 in CCodedText missing", codes.contains("at0.33"));
		
		assertTrue("at0006 in CDvOrdinal missing", codes.contains("at0006"));
		
		// two codes are not used in the actual archetype
		assertTrue("total number of fetched AT codes wrong", 
				expectedNumOfATCodes >= codes.size());		
	}

	@Test
	public void testFetchAllACCodes() throws Exception {
		archetype = loadArchetype("openEHR-EHR-EVALUATION.problem-diagnosis.v1.adl");
		
		Set<String> codes = validator.fetchAllACCodes(archetype);
		assertNotNull("fetched AT codes null", codes);
		
		assertEquals("total number of fetched AC codes wrong", 2, codes.size());
		
		assertTrue("ac0000 missing", codes.contains("ac0000"));
		
		assertTrue("ac0.1 missing", codes.contains("ac0.1"));				
	}

	@Test
	public void testCheckErrorDescriptionExists() throws Exception {
		checkTerm("adl-test-ENTRY.term_definition.v3");			
		for(ValidationError error : errors) {
			assertNotNull("Error description loaded", error.getDescription());
		}
	}
	
	private void checkTerm(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkArchetypeTermValidity(archetype, errors);
	}
}
