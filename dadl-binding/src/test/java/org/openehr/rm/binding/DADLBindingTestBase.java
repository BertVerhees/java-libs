package org.openehr.rm.binding;

import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.DADLParser;

import java.io.InputStream;

public class DADLBindingTestBase  {

	public void setUp() throws Exception {
		binding = new DADLBinding();
	}
	
	public void tearDown() throws Exception {
		rmObj = null;
	}
	
	/*
	 * Loads dadl from file and binds data to RM object
	 */
	Object bind(String filename) throws Exception {
		DADLParser parser = new DADLParser(fromClasspath(filename));
		ContentObject contentObj = parser.parse();
		return binding.bind(contentObj);
	}
	
	Object bindString(String dadl) throws Exception {
		DADLParser parser = new DADLParser(dadl);
		ContentObject contentObj = parser.parse();
		return binding.bind(contentObj);
	}
	
	/* 
	 * Loads given resource from classpath
	 * 
	 * @param adl
	 * @return
	 * @throws Exception
	 */
	InputStream fromClasspath(String res) throws Exception {
    	return this.getClass().getClassLoader().getResourceAsStream(res);
    }
	
	protected Object rmObj;
	protected DADLBinding binding;
}
