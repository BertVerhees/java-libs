package org.openehr.rm.datatypes.quantity.datetime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.fail;

public class DvDateTimeParserTimeZoneTest {
	public DvDateTimeParserTimeZoneTest() {
		defaultTimeZone = TimeZone.getDefault();		
	}

	@Before
	public void setUp() throws Exception {
		System.setProperty("user.timezone", "GMT-3:00");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3:00"));    	    	
	}	

	@After
	public void tearDown() throws Exception {
		TimeZone.setDefault(defaultTimeZone);
	}

	@Test
	public void testParseTime() {
		try {
			DvDateTimeParser.parseTime("010000");
		} catch(Exception e) {
			fail("failed to parse 010000 in GMT-3 timezone");
		} 
	}
	
	private TimeZone defaultTimeZone;
}
