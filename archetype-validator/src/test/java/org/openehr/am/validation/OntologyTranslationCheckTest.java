package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OntologyTranslationCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testTranslationCheckWithNoTranslation() throws Exception {
		checkTranslation("adl-test-ENTRY.ontology_translation.v1");		
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testTranslationCheckWithoutConstraintDef() throws Exception {
		checkTranslation("adl-test-ENTRY.ontology_translation.v7");		
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testTranslationCheckWithMissingTermAndConstraintTranslation()
			throws Exception {		
		checkTranslation("adl-test-ENTRY.ontology_translation.v2");		
		assertEquals("expected validation error", 2, errors.size());
		assertFirstErrorType(ErrorType.VOTM);
		assertSecondErrorType(ErrorType.VOTM);		
	}

	@Test
	public void testTranslationCheckWithMissingTermTranslation()
			throws Exception {		
		checkTranslation("adl-test-ENTRY.ontology_translation.v3");		
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VOTM);
	}

	@Test
	public void testTranslationCheckWithMissingConstraintTranslation()
			throws Exception {		
		checkTranslation("adl-test-ENTRY.ontology_translation.v4");		
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VOTM);
	}

	@Test
	public void testTranslationCheckWithCompleteTranslation() throws Exception {
		checkTranslation("adl-test-ENTRY.ontology_translation.v5");		
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testTranslationCheckWithTwoMissingTranslation() throws Exception {
		checkTranslation("adl-test-ENTRY.ontology_translation.v6");		
		assertEquals("expected validation error", 2, errors.size());
		assertFirstErrorType(ErrorType.VOTM);
		assertSecondErrorType(ErrorType.VOTM);
	}
	
	private void checkTranslation(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkOntologyTranslation(archetype, errors);
	}
}
