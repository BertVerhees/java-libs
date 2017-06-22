package org.openehr.am.archetype.ontology;

import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class ArchetypeTermTest {
	@Before
	public void setUp() {
		term = new ArchetypeTerm("at0001", TEXT, DESC);
	}
	@Before
	public void tearDown() {
		term = null;		
	}
	
	public void testGetText() {
		assertEquals("text wrong", TEXT, term.getText());
	}
	
	public void testGetDescription() {
		assertEquals("description wrong", DESC, term.getDescription());
	}
	
	private static final String TEXT = "text";
	private static final String DESC = "description";
	
	private ArchetypeTerm term;

}
