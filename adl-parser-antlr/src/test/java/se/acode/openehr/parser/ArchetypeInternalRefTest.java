package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.archetype.constraintmodel.ArchetypeInternalRef;
import org.openehr.rm.support.basic.Interval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArchetypeInternalRefTest extends ParserTestBase {
	@Test
	public void testParseCheckIfNodeIsIsTakenIntoAccount() throws Exception{
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_internal_ref.test.adl"));
		Archetype archetype = parser.parse();
		assertNotNull(archetype);

		assertNotNull(archetype.node("/attribute3"));
		assertNotNull(archetype.node("/attribute4[at0005]"));
		assertNotNull(archetype.node("/attribute4[at0006]"));
	}


	@Test
	public void testParseInternalRefWithOverwrittingOccurrences()
			throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_internal_ref.test.adl"));
		Archetype archetype = parser.parse();
		assertNotNull(archetype);

		ArchetypeConstraint node = archetype.node("/attribute2");
		assertTrue("ArchetypeInternalRef expected, actual: " + node.getClass(),
				node instanceof ArchetypeInternalRef);

		ArchetypeInternalRef ref = (ArchetypeInternalRef) node;
		assertEquals("rmType wrong", "SECTION", ref.getRmTypeName());
		assertEquals("path wrong", "/attribute1", ref.getTargetPath());

		Interval<Integer> occurrences = new Interval<Integer>(1, 2);
		assertEquals("overwriting occurrences wrong", occurrences, ref
				.getOccurrences());
	}

	@Test
	public void testParseInternalRefWithGenerics() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-SOME_TYPE.generic_type_use_node.draft.adl"));
		Archetype archetype = parser.parse();
		assertNotNull(archetype);
		
		ArchetypeConstraint node = archetype.node("/interval_attr2");
		assertTrue("ArchetypeInternalRef expected, actual: " + node.getClass(),
				node instanceof ArchetypeInternalRef);

		ArchetypeInternalRef ref = (ArchetypeInternalRef) node;
		assertEquals("rmType wrong", "INTERVAL<QUANTITY>", 
				ref.getRmTypeName());
		assertEquals("path wrong", "/interval_attr[at0001]", 
				ref.getTargetPath());
	}

	@Test
	public void testParseInternalRefWithCommentWithSlashAfterOnlyOneSlashInTarget()
			throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_internal_ref2.test.adl"));
		Archetype archetype = parser.parse();
		assertNotNull(archetype);

	}
	
}
