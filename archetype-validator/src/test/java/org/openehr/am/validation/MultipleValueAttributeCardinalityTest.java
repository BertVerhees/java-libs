package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultipleValueAttributeCardinalityTest
		extends ArchetypeValidationTestBase {

	@Test
	public void testCheckMultiAttributeCardinalityNotContainingOccurence() throws Exception {
		checkAttribute("adl-test-ITEM_TREE.attribute_cardinality.v1");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VACMC);
	}

	@Test
	public void testCheckMultiAttributeCardinalityNotConformingToReferenceModel() throws Exception {
		// testing that a cardinality constraint from the reference model is honoured in the actual archetype
		checkAttribute("adl-test-ITEM_TREE.attribute_cardinality.v2");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VCACA);
	}

	@Test
	public void testCheckMultiAttributeCardinalityCredentialsNotConformingToReferenceModel() throws Exception {
		// testing that a cardinality constraint from the reference model is honoured in the actual archetype
		checkAttribute("adl-test-ITEM_TREE.attribute_cardinality.v3");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VCACA);
	}

	@Test
	public void testCheckSumOfOccurrencesNotIntersectingWithCardinality () throws Exception {
		// testing whether sum of occurrences intersects with cardinality
		checkAttribute("openEHR-EHR-CLUSTER.cardinality_occurrences.v1.adl");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.VACMC);
	}
	
	
	// As per now it is not clear how this should be handled - no error, VACMC or maybe a WACMC warning
	// We use the warning for now
	@Test
	public void testCheckSumOfOccurrencesNotIntersectingWithCardinalityEdgeCase () throws Exception {
		checkAttribute("openEHR-EHR-CLUSTER.cardinality_occurrences2.v1.adl");
		assertEquals("expected validation error", 1, errors.size());	
		assertFirstErrorType(ErrorType.WACMC);
	}


	@Test
	public void testCheckSumOfOccurrencesIntersectingWithCardinality () throws Exception {
		// testing whether sum of occurrences intersects with cardinality
		checkAttribute("openEHR-EHR-CLUSTER.cardinality_occurrences3.v1.adl");
		assertEquals("expected no validation errors", 0, errors.size());	
	}
			
	
	private void checkAttribute(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}

