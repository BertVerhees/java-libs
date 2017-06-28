package org.openehr.ckm_rounddtrip;

import org.junit.Test;
import org.openehr.CKMRoundtripTestBase;

/**
 * Created by verhees on 28-6-17.
 */
public class RoundTripCKMTest extends CKMRoundtripTestBase {

    @Test
    public void testRoundTrips() throws Exception {
        //first load en parse archetypes
        loadArchetypeMap();
    }

    @Test
    public void testArchetype() throws Exception {
        loadArchetype("openEHR-DEMOGRAPHIC-PARTY_IDENTITY.person_name.v1.adl");
    }

}
