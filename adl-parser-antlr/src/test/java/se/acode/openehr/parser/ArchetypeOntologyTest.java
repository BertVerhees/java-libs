package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.ontology.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArchetypeOntologyTest extends ParserTestBase {

	@Test
	public void testParseLanguages() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_bindings.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		assertNotNull(archetype);
		ArchetypeOntology ontology = archetype.getOntology();

		List<String> l = ontology.getLanguages();
		assertEquals("language wrong", "en", l.get(0));
		assertEquals("language wrong", "ge", l.get(1));
		assertEquals("Language wrong number", 2, l.size());
	}

	@Test
	public void testParseTerminologiesAvailable() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_bindings.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		assertNotNull(archetype);
		ArchetypeOntology ontology = archetype.getOntology();

		List<String> l = ontology.getTerminologies();
		assertEquals("Terminologies wrong", "SNOMED-CT", l.get(0));
		assertEquals("Terminologies wrong", "LOINC", l.get(1));
	}

	@Test
	public void testParseTermDefinition() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_bindings.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
        assertNotNull(archetype);
        ArchetypeOntology ontology = archetype.getOntology();
        
        ArchetypeTerm term = ontology.termDefinition("en", "at0000");
        assertEquals("text wrong", "test", term.getItem("text"));
        assertEquals("comment wrong", "test", term.getItem("description"));
        assertEquals("description wrong", "fantasy", term.getItem("fantasy"));

        term = ontology.termDefinition("en", "at0001");
		assertEquals("text wrong", "test", term.getItem("text"));
		assertEquals("description wrong", "test", term.getItem("description"));

		term = ontology.termDefinition("ge", "at0000");
		assertEquals("text wrong", "testge", term.getItem("text"));
		assertEquals("comment wrong", "testge", term.getItem("description"));
		assertEquals("description wrong", "fantasyge", term.getItem("fantasy"));

		term = ontology.termDefinition("ge", "at0001");
		assertEquals("text wrong", "testge", term.getItem("text"));
		assertEquals("description wrong", "testge", term.getItem("description"));
	}

	@Test
	public void testParseConstraintDefinition() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_bindings.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
		assertNotNull(archetype);
		ArchetypeOntology ontology = archetype.getOntology();

		ArchetypeTerm term = ontology.constraintDefinition("en", "ac0001");
		assertEquals("text wrong", "test constraint", term.getItem("text"));
		assertEquals("comment wrong", "*", term.getItem("description"));
		assertEquals("description wrong", "fantasy", term.getItem("fantasy"));

		term = ontology.constraintDefinition("en", "ac0002");
		assertEquals("text wrong", "test constraint", term.getItem("text"));
		assertEquals("description wrong", "*", term.getItem("description"));

		term = ontology.constraintDefinition("ge", "ac0001");
		assertEquals("text wrong", "test constraintge", term.getItem("text"));
		assertEquals("comment wrong", "*", term.getItem("description"));
		assertEquals("description wrong", "fantasyge", term.getItem("fantasy"));

		term = ontology.constraintDefinition("ge", "ac0002");
		assertEquals("text wrong", "test constraintge", term.getItem("text"));
		assertEquals("description wrong", "*", term.getItem("description"));
	}
	
	/** Tests that term_bindings and constraint_bindings can be written with a tailing s (see http://www.openehr.org/issues/browse/SPEC-284)
	*/
	@Test
	public void testBindings() throws Exception {


		ADLParser parser = new ADLParser(loadFromClasspath(
				"adl-test-entry.archetype_bindings.test.adl"));
		Archetype archetype = parser.parse();
		parser.generatedParserException();
        assertNotNull(archetype);
        ArchetypeOntology ontology = archetype.getOntology();
        List<OntologyBinding> termBindings = ontology.getTermBindingList();
		OntologyBinding termBinding = termBindings.get(0);
		assertEquals("term bindings wrong", "SNOMED-CT", termBinding.getTerminology());
		List<OntologyBindingItem> tbis = termBinding.getBindingList();
		TermBindingItem tbi = (TermBindingItem) tbis.get(0);
        assertEquals("term binding item wrong", "[SNOMED-CT::1234561]", tbi.getTerms().get(0));
		tbi = (TermBindingItem) tbis.get(1);
		assertEquals("term binding item wrong", "[SNOMED-CT::1234562]", tbi.getTerms().get(0));

		termBinding = termBindings.get(1);
		assertEquals("term bindings wrong", "LOINC", termBinding.getTerminology());
		tbis = termBinding.getBindingList();
		tbi = (TermBindingItem) tbis.get(0);
		assertEquals("term binding item wrong", "[LOINC::1234561]", tbi.getTerms().get(0));
		tbi = (TermBindingItem) tbis.get(1);
		assertEquals("term binding item wrong", "[LOINC::1234562]", tbi.getTerms().get(0));
		
		List<OntologyBinding> constrBindings = ontology.getConstraintBindingList();
		OntologyBinding constrBinding = constrBindings.get(0);
		assertEquals("binding ontology wrong", "SNOMED-CT", constrBinding.getTerminology());
		List<OntologyBindingItem> cbis = constrBinding.getBindingList();
        QueryBindingItem qbi = (QueryBindingItem) cbis.get(0);
		assertEquals("query binding item wrong", "http://openEHR.org/testconstraintbinding1", qbi.getQuery().getUrl());
		qbi = (QueryBindingItem) cbis.get(1);
		assertEquals("query binding item wrong", "http://openEHR.org/testconstraintbinding2", qbi.getQuery().getUrl());

		constrBinding = constrBindings.get(1);
		assertEquals("binding ontology wrong", "LOINC", constrBinding.getTerminology());
		cbis = constrBinding.getBindingList();
		qbi = (QueryBindingItem) cbis.get(0);
		assertEquals("query binding item wrong", "http://openEHR.org/testconstraintbindingLOINC1", qbi.getQuery().getUrl());
		qbi = (QueryBindingItem) cbis.get(1);
		assertEquals("query binding item wrong", "http://openEHR.org/testconstraintbindingLOINC2", qbi.getQuery().getUrl());

	}
	
}
