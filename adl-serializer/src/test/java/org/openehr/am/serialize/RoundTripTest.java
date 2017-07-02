package org.openehr.am.serialize;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class RoundTripTest extends SerializerTestBase {

	@Test
	public void testRoundTripOnSimpleEvaluation() throws Exception {
		adlFile = "openEHR-EHR-EVALUATION.test_concept.v1.adl";
		ADLParser parser = new ADLParser(loadFromClasspath(adlFile));
		Archetype archetype = parser.parse();
		parser.generatedParserException();

		outputter.output(archetype, out);

		parser.ReInit(new BufferedInputStream(IOUtils.toInputStream(out.toString(), "UTF-8")));

		Archetype roundTripedArchetype = parser.parse();
		parser.generatedParserException();

		assertEquals("adlVersion diff", archetype.getAdlVersion(),
				roundTripedArchetype.getAdlVersion());
		assertEquals("archetypeId diff", archetype.getArchetypeId(),
				roundTripedArchetype.getArchetypeId());
		assertEquals("concept diff", archetype.getConcept(),
				roundTripedArchetype.getConcept());
		assertEquals("definition diff", archetype.getDefinition(),
				roundTripedArchetype.getDefinition());

		// TODO skipped problematic description comparison
		// assertEquals("description diff", archetype.getDescription(),
		// roundTripedArchetype.getDescription());
		
		assertEquals("ontology diff", archetype.getOntology(),
				roundTripedArchetype.getOntology());
		assertEquals("original language diff", archetype.getOriginalLanguage(),
				roundTripedArchetype.getOriginalLanguage());

	}

	private InputStream loadFromClasspath(String adl) throws Exception {
		return this.getClass().getClassLoader().getResourceAsStream(adl);
	}
}
