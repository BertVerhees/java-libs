package org.openehr.ckm_rounddtrip;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehr.CKMRoundtripTestBase;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.serialize.ADLSerializer;

import java.io.BufferedInputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by verhees on 28-6-17.
 */
public class RoundTripCKMTest extends CKMRoundtripTestBase {

    @Test
    public void testRoundTrips() throws Exception {
        //first load en parse archetypes
        loadArchetypeMap();
        for(String s: archetypeMap.keySet()){
            Archetype archetype = archetypeMap.get(s);
            outputter = new ADLSerializer();
            out = new StringWriter();
            outputter.output(archetype, out);
            adlParser.ReInit(new BufferedInputStream(IOUtils.toInputStream(out.toString(), "UTF-8")));
            Archetype roundTrippedArchetype = adlParser.parse();

            try {
                assertEquals(s + ":adlVersion diff", archetype.getAdlVersion(),
                        roundTrippedArchetype.getAdlVersion());
                assertEquals(s + ":archetypeId diff", archetype.getArchetypeId(),
                        roundTrippedArchetype.getArchetypeId());
                assertEquals(s + ":concept diff", archetype.getConcept(),
                        roundTrippedArchetype.getConcept());
                assertEquals(s + ":definition diff", archetype.getDefinition(),
                        roundTrippedArchetype.getDefinition());

                // TODO skipped problematic description comparison
                // assertEquals("description diff", archetype.getDescription(),
                // roundTripedArchetype.getDescription());

                assertEquals(s + ":ontology diff", archetype.getOntology(),
                        roundTrippedArchetype.getOntology());
                assertEquals(s + ":original language diff", archetype.getOriginalLanguage(),
                        roundTrippedArchetype.getOriginalLanguage());
            }catch(Exception e){
                throw new Exception(s,e);
            }
        }
    }

    @Test
    public void testArchetype() throws Exception {
        String s = "openEHR-EHR-CLUSTER.move.v1.adl";
        Archetype archetype = loadArchetype(s);
        outputter = new ADLSerializer();
        out = new StringWriter();
        outputter.output(archetype, out);
        adlParser.ReInit(new BufferedInputStream(IOUtils.toInputStream(out.toString(), "UTF-8")));
        Archetype roundTrippedArchetype = adlParser.parse();

        assertEquals(s+":adlVersion diff", archetype.getAdlVersion(),
                roundTrippedArchetype.getAdlVersion());
        assertEquals(s+":archetypeId diff", archetype.getArchetypeId(),
                roundTrippedArchetype.getArchetypeId());
        assertEquals(s+":concept diff", archetype.getConcept(),
                roundTrippedArchetype.getConcept());
        assertEquals(s+":definition diff", archetype.getDefinition(),
                roundTrippedArchetype.getDefinition());

        // TODO skipped problematic description comparison
        // assertEquals("description diff", archetype.getDescription(),
        // roundTripedArchetype.getDescription());

        assertEquals(s+":ontology diff", archetype.getOntology(),
                roundTrippedArchetype.getOntology());
        assertEquals(s+":original language diff", archetype.getOriginalLanguage(),
                roundTrippedArchetype.getOriginalLanguage());
    }

}
