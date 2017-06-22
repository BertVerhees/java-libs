package org.openehr.am.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeNameCheckTest extends ArchetypeValidationTestBase {

	@Test
	public void testCheckTypeNameWithRightName() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_name.v1");
		assertEquals("expected no validation error", 0,	errors.size());	
	}

	@Test
	public void testCheckTypeNameWithEvent() throws Exception {
		CheckTypeName("openEHR-EHR-OBSERVATION.type_name.v4.adl");
		assertEquals("unexpected validation error: " + errors, 0,	
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithExplicitDvCount() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_count.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithExplicitDvDuration() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_duration.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithUnknownName() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_name.v2");
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VCORM);
	}

	@Test
	public void testCheckTypeNameWithExplicitPartyProxy() throws Exception {
		CheckTypeName("adl-test-PARTY_PROXY.type_name.v1");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithUnassignableName() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_name.v3");
		assertEquals("expected validation error", 1, errors.size());
		assertFirstErrorType(ErrorType.VCORMT);
	}

	@Test
	public void testCheckTypeNameExplicitDvDate() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_date.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvDateTime() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_datetime.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvBoolean() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_boolean.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvURI() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_uri.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvEHRURI() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_ehr_uri.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvMultimedia() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_multimedia.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameExplicitDvInterval() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameDvIntervalWithGenerics() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v2.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameDvIntervalWithUnassignableGenericType() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v4.adl");
		assertFirstErrorType(ErrorType.VCORMT);
	}

	@Test
	public void testCheckTypeNameDvIntervalWithUnassignableGenericTypeSub() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v3.adl");
		assertFirstErrorType(ErrorType.VCORMT);
	}


	@Test
	public void testCheckTypeNameDvIntervalWithUnknownGenericType() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v5.adl");
		assertFirstErrorType(ErrorType.VCORM);
	}

	@Test
	public void testCheckTypeNameDvIntervalWithUnknownGenericTypeSub() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_interval.v6.adl");
		assertFirstErrorType(ErrorType.VCORM);
	}

	@Test
	public void testCheckTypeNameExplicitDvProportion() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_proportion.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithInlinedDvDate() throws Exception {
		CheckTypeName("adl-test-ELEMENT.type_date.v2.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}

	@Test
	public void testCheckTypeNameWithISMTransition() throws Exception {
		CheckTypeName("openEHR-EHR-ACTION.follow_up.v1.adl");
		assertEquals("unexpected validation error(s): " + errors, 0,
				errors.size());	
	}
	
	private void CheckTypeName(String name) throws Exception {
		archetype = loadArchetype(name);
		validator.checkObjectConstraints(archetype, errors);
	}
}
