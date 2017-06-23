package org.openehr.rm.common.archetyped;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PathUtilTest  {

	@Test
	public void testDividePathIntoSegments() throws Exception {
		String path = "/content[openEHR-EHR-SECTION.ad_hoc_heading.v1 and " +
				"name/value='Biokemiska data']/items[openEHR-EHR-OBSERVATION." +
				"lab_test.v1 and name/value='B-HB']";
		
		List<String> list = Locatable.dividePathIntoSegments(path);
		
		assertEquals(2, list.size());		
	}

	@Test
	public void testComputeParentPath() throws Exception {
		String path = "/data[at0001]/events[at0002]/data[at0003]/items" +
				"[at0004 and name/value='Buk']";
		
		String expected = "/data[at0001]/events[at0002]/data[at0003]";
		
		assertEquals(expected, Locatable.parentPath(path));
	}

	@Test
	public void testComputeParentPathToRoot() throws Exception {
		String path = "/items";		
		String expected = "/";
		assertEquals(expected, Locatable.parentPath(path));
	}
}
