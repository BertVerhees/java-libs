package org.openehr.am.template;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TermMapTest extends TemplateTestBase {

	@Test
	public void testWriteTermMap() throws Exception {
		flattenTemplate("test_text_constraints.oet");
		flattener.getTermMap().writeTermMap("term_map.txt");
	}

	@Test
	public void testLoadTermMapAndGetTerms() throws Exception {
		TermMap termMap = TermMap.load(fromClasspath("terms_path.txt"));
		
		assertEquals("unexpected total terminologies", 2, 
				termMap.countTerminologies());
		
		String path = "/path1";
		assertEquals("Sotalol", termMap.getText("ATC::C07AA07", path));
		assertEquals("Thiazider", termMap.getText("SNOMED-CT::10019", path));
	}

	@Test
	public void testLoadTermMapAndGetTermsWithoutPath() throws Exception {
		TermMap termMap = TermMap.load(fromClasspath("terms.txt"));
		
		assertEquals("unexpected total terminologies", 2, 
				termMap.countTerminologies());
		
		String path = "/any/path";
		assertEquals("Sotalol", termMap.getText("ATC::C07AA07", path));
		assertEquals("Thiazider", termMap.getText("SNOMED-CT::10019", path));
	}
}
