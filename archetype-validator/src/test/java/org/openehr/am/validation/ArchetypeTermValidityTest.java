package org.openehr.am.validation;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ArchetypeTermValidityTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckTermValidityWithMissingTermDef() throws Exception {
		checkTerm("adl-test-ENTRY.term_definition.v1");
		assertEquals("wrong number of validation error in term def checking", 2, 
				errors.size());		
		for(ValidationError error : errors) {
			assertEquals("validation error type wrong", ErrorType.VATDF, 
					error.getType());
		}
	}

	@Test
	public void testCheckTermValidityWithCompleteTermDef() throws Exception {
		checkTerm("adl-test-ENTRY.term_definition.v2");		
		assertEquals("expected no validation error in term def checking", 0, 
				errors.size());
	}

	@Test
	public void testCheckTermValidityWithMissingLanguage() throws Exception {
		checkTerm("adl-test-ENTRY.term_definition.v3");		
		assertEquals("expected validation error in term def checking", 1, 
			errors.size());		
		for(ValidationError error : errors) {
			assertEquals("validation error type wrong", ErrorType.VATDF, 
					error.getType());
		}
	}

	@Test
	public void testCheckTermValidityWithMissingTermInSecondaryLanguage() throws Exception {
		checkTerm("adl-test-ENTRY.term_definition.v4");		
		assertEquals("expected validation error in term def checking", 1, 
			errors.size());		
		for(ValidationError error : errors) {
			assertEquals("validation error type wrong", ErrorType.VONLC, 
					error.getType());
		}
	}

	@Test
	public void testCheckWithNoUnusedCodes() throws Exception {
		checkTerm("adl-test-ENTRY.ontology-unusedcodes.v1.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckWithUnusedCodes() throws Exception {
		checkTerm("adl-test-ENTRY.ontology-unusedcodes.v2.adl");
			assertEquals("unexpected amount of validation errors: " + errors, 1,	
				errors.size());	
		assertFirstErrorType(ErrorType.WOUC);
	}

	@Test
	public void testCheckTermWithDoubleEntryInOntology() throws Exception {
		checkTerm("openEHR-EHR-ELEMENT.doubleontologycode.v1.adl");		
		assertEquals("expected 1 validation error in term def checking", 1, 
				errors.size());
				for(ValidationError error : errors) {
			assertEquals("validation error type wrong", ErrorType.VOKU, 
					error.getType());
		}
	}

	@Test
	public void testCheckDoubleLanguage_Ontology() throws Exception {
		Archetype archetype = loadArchetype("adl-test-ENTRY.term_definition.v6");
		errors = ArchetypeValidator.filterVDLErrors(archetype);

		assertEquals("expected validation error in term def checking", 1, errors.size());
		for(ValidationError error : errors) {
			assertEquals("validation error type wrong", ErrorType.VDL, error.getType());
		}
	}
	
	private void checkTerm(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkArchetypeTermValidity(archetype, errors);
	}
}
