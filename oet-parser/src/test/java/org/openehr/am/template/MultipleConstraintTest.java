package org.openehr.am.template;

import openEHR.v1.template.MultipleConstraint;
import openEHR.v1.template.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.archetype.constraintmodel.CComplexObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultipleConstraintTest extends TemplateTestBase {
	
	@Before
	public void setUp() throws Exception {
		
		super.setUp();
		
		archetype = loadArchetype(
			"openEHR-EHR-ITEM_TREE.medication_multiple_constraint_test.v1.adl");
		
		constraint = archetype.node(PATH);
		
		rule = Statement.Factory.newInstance();
		rule.setPath(PATH);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		constraint = null;
	}
	
	/**
	 * Test multiple constraint in choosing specific data types
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSetMultipleConstraintIncludedTypes() throws Exception {
		MultipleConstraint mc = MultipleConstraint.Factory.newInstance();
		mc.addIncludedTypes("Count");
		rule.setConstraint(mc);
		
		ccobj = (CComplexObject) constraint;
		flattener.applyValueConstraint(archetype, ccobj, rule);
		
		archetype.reloadNodeMaps();
		
		constraint = archetype.node(PATH);
		assertTrue("expected CComplexObject", constraint instanceof CComplexObject);
		CComplexObject ccobj = (CComplexObject) constraint;
		assertEquals(1, ccobj.getAttributes().size());
		
		constraint = archetype.node(PATH + "/value");
		assertTrue("expected CComplexObject", constraint instanceof CComplexObject);
		ccobj = (CComplexObject) constraint;
		assertEquals(1, ccobj.getAttributes().size());
		assertEquals("DV_COUNT", ccobj.getRmTypeName());
		
	}

	@Test
	public void testMultipleConstraintWithinTemplate() throws Exception {
		flattenTemplate("test_multiple_constraint.oet");		
		constraint = flattened.node("/items[at0033]/items[at0034]/items[at0005]/value");
		assertDvCountConstraint(constraint);
	}

	@Test
	public void testMultipleConstraintWithinNestedTemplate() throws Exception {
		flattenTemplate("test_multiple_constraint2.oet");
		//printADL(flattened);
		constraint = flattened.node("/description[openEHR-EHR-ITEM_TREE.medication.v1]/items[at0033]/items[at0034]/items[at0005]/value");
		assertDvCountConstraint(constraint);
	}

	@Test
	public void testMultipleConstraintWithoutValueAttribute() throws Exception {
		flattenTemplate("test_multiple_constraint3.oet");		
		constraint = flattened.node(PATH + "/value");
		assertDvCountConstraint(constraint);
	}
	
	private static final String PATH = "/items[at0001]";
	private Statement rule;
	private ArchetypeConstraint constraint;
	private CComplexObject ccobj;
}
