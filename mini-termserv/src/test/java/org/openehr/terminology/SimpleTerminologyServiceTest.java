/*
 * component:   "openEHR Reference Implementation"
 * description: "Class SimpleTerminologyServiceTest"
 * keywords:    "terminology"
 *
 * author:      "Rong Chen <rong.acode@gmail.com>"
 * copyright:   "Copyright (c) 2007 Rong Chen"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL$"
 * revision:    "$LastChangedRevision$"
 * last_change: "$LastChangedDate$"
 */
package org.openehr.terminology;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehr.rm.support.terminology.CodeSetAccess;
import org.openehr.rm.support.terminology.OpenEHRCodeSetIdentifiers;
import org.openehr.rm.support.terminology.TerminologyAccess;
import org.openehr.rm.support.terminology.TerminologyService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case for SimpleTerminologyService
 * 
 * @author rong.chen
 */
public class SimpleTerminologyServiceTest  {

	@Before
	public void setUp() throws Exception {
		instance = SimpleTerminologyService.getInstance();
	}
	@After
	public void tearDown() throws Exception {
		instance = null;
	}

	@Test
	public void testGetTerminology() {
		TerminologyAccess terminology =	
			instance.terminology(TerminologyService.OPENEHR);
		assertNotNull("failed to get openehr terminology", terminology);
	}

	@Test
	public void testGetCodeSetWithAllValidIds() {
		for(OpenEHRCodeSetIdentifiers id : OpenEHRCodeSetIdentifiers.values()) {
			CodeSetAccess codeSet = instance.codeSetForId(id);
			assertNotNull("code set " + id + " should exist", codeSet);
		}
	}

	@Test
	public void testHasTerminologyWithOpenEHR() {
		String name = TerminologyService.OPENEHR;
		assertTrue("terminology " + name + " should exist", 
				instance.hasTerminology(name));
	}

	@Test
	public void testHasCodeSet() {
		for(OpenEHRCodeSetIdentifiers id : OpenEHRCodeSetIdentifiers.values()) {
			assertNotNull("code set " + id + " should exist", 
					instance.hasCodeSet(id.toString()));			
		}
	}

	@Test
	public void testGetTerminologyIdentifiers() {
		List<String> ids = instance.terminologyIdentifiers();
		assertNotNull("terminology ids should not be null", ids);
		assertTrue("terminology ids should not be empty", ids.size() > 0);
	}

	@Test
	public void testGetCodeSetIdentifiers() {
		List<String> ids = instance.codeSetIdentifiers();
		assertNotNull("code set ids should not be null", ids);
		assertTrue("code set ids should not be empty", ids.size() > 0);
	}

	@Test
	public void testGetOpenehrCodeSets() {
		Map<String, String> codeSets = instance.openehrCodeSets();
		assertNotNull("code set ids should not be null", codeSets);
		assertTrue("code set ids should not be empty", codeSets.size() > 0);
	}

	@Test
	public void testGetCountryCodeSetByExternalName() {
		String[] externalNames = { 
				"ISO_3166-1", "IANA_character-sets", 
				"openehr_compression_algorithms", 
				"openehr_integrity_check_algorithms",
				"ISO_639-1", "IANA_media-types",
				"openehr_normal_statuses" 
		};
		for(String name : externalNames) {
			CodeSetAccess codeSet = instance.codeSet(name);
			assertNotNull("Code set of external name: " + name + " missing", 
					codeSet);
		}
	}	
	
	/* test instance */
	private TerminologyService instance;
}
