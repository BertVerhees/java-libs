package org.openehr.am.validation;

import org.junit.After;
import org.junit.Before;
import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpecialisedArchetypeValidationTestBase  {

	@Before
    public void setUp() {
		validator = new SpecialisedArchetypeValidator();	
		errors = new ArrayList<ValidationError>();
	}
	
	@After
    public void tearDown() {		
		validator = null;
		archetype = null;
		parentArchetype=null;
		errors = null;
	}
	
	/**
	 * Loads archetype from given filename
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	protected Archetype loadArchetype(String name) throws Exception {
    	InputStream input = 
    		this.getClass().getClassLoader().getResourceAsStream(name);
    	ADLParser parser = new ADLParser(input, "UTF-8");
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		return archetype;
    }
	
	/**
	 * Asserts the first validation error is the same as the given error type
	 * 
	 * @param type
	 */
	protected void assertFirstErrorType(ErrorType type) {
		assertTrue("expected at least 1 error", errors.size() >= 1);
		assertEquals("unexpected error(s): " + errors, type, 
				errors.get(0).getType());
	}
	
	/**
	 * Asserts the second validation error is the same as the given error type
	 * 
	 * @param type
	 */
	protected void assertSecondErrorType(ErrorType type) {
		assertTrue("expected at least 2 error", errors.size() >= 2);
		assertEquals("errorType wrong",  type, errors.get(1).getType());
	}	
	
	protected SpecialisedArchetypeValidator validator;
	protected Archetype archetype;
	protected Archetype parentArchetype;
	protected List<ValidationError> errors;
}
