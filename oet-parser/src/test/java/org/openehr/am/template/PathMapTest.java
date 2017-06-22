package org.openehr.am.template;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathMapTest extends TemplateTestBase {

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		map = null;
	}

	@Test
	public void testCreateNewAndWriteToFile() throws Exception {
		map = new PathMap();
		map.addPath("key1", "path1");
		map.addPath("key2", "path2");
		map.addPath("key2", "path2");
		map.writeToFile("test_paths.txt");
	}

	@Test
	public void testLoadPathMapAndGetPath() throws Exception {
		map= PathMap.load(fromClasspath("test_path_map.txt"));
		
		assertEquals("unexpected total paths", 2, 
				map.countPaths());
		
		assertEquals("/path1", map.getPath("key1"));
		assertEquals("/path2[at0001 and name/value='text']", map.getPath("key2"));	
	}
	
	private PathMap map;
}
