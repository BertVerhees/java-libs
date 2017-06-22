package org.openehr.am.validation;

import org.junit.Before;
import org.junit.Test;
import org.openehr.rm.common.generic.PartyProxy;
import org.openehr.rm.composition.content.entry.Evaluation;
import org.openehr.rm.datastructure.history.Event;
import org.openehr.rm.datastructure.itemstructure.representation.Element;
import org.openehr.rm.datatypes.quantity.DvQuantity;
import org.openehr.rm.datatypes.uri.DvURI;

import java.util.Set;

import static org.junit.Assert.*;

public class RMInspectorTest  {

	@Before
	public void setUp() throws Exception {
		inspector = new RMInspector();
	}

	@Test
	public void testRetrieveRMTypeWithElement() throws Exception {
		Class klass = inspector.retrieveRMType("ELEMENT");
		assertTrue("incorrect class for element", klass.equals(Element.class));
	}

	@Test
	public void testRetrieveRMTypeWithEvaluation() throws Exception {
		Class klass = inspector.retrieveRMType("EVALUATION");
		assertTrue("incorrect class for evaluation", klass.equals(Evaluation.class));
	}

	@Test
	public void testRetrieveRMTypeWithInteger() throws Exception {
		Class klass = inspector.retrieveRMType("INTEGER");
		assertTrue("incorrect class for integer", klass.equals(Integer.class));
	}

	@Test
	public void testRetrieveRMTypeWithDvQuantity() throws Exception {
		Class klass = inspector.retrieveRMType("DV_QUANTITY");
		assertTrue("incorrect class for dvQuantity", klass.equals(DvQuantity.class));
	}

	@Test
	public void testRetrieveRMTypeWithDvURI() throws Exception {
		Class klass = inspector.retrieveRMType("DV_URI");
		assertTrue("incorrect class for dvUri", klass.equals(DvURI.class));
	}

	@Test
	public void testRetrieveRMTypeWithAbstractClassEVENT() throws Exception {
		Class klass = inspector.retrieveRMType("EVENT");
		assertNotNull("failed to retrieve EVENT", klass);
		assertTrue("incorrect class for Event", klass.equals(Event.class));
	}

	@Test
	public void testRetrieveRMTypeWithAbstractClassPartyProxy() throws Exception {
		Class klass = inspector.retrieveRMType("PARTY_PROXY");
		assertNotNull("failed to retrieve PARTY_PROXY", klass);
		assertTrue("incorrect class for PARTY_PROXY", klass.equals(PartyProxy.class));
	}

	@Test
	public void testRetrieveRMAttributeNamesWithEVENT() throws Exception {
		assertFalse("failed to retrieve attribute names with EVENT",
				inspector.retrieveRMAttributeNames("EVENT").isEmpty());
	}

	@Test
	public void testRetrieveRMAttributeNamesWithObservation() throws Exception {
		Set<String> attrs = inspector.retrieveRMAttributeNames("OBSERVATION");
		assertTrue("data attribute missing", attrs.contains("data"));
		assertTrue("state attribute missing", attrs.contains("state"));
		assertTrue("protocol attribute missing", attrs.contains("protocol"));
		assertTrue("subject attribute missing", attrs.contains("subject"));
		assertTrue("provider attribute missing", attrs.contains("provider"));		
	}

	@Test
	public void testRetrieveRMAttributeNamesWithCodedText() throws Exception {
		Set<String> attrs = inspector.retrieveRMAttributeNames("DV_CODED_TEXT");
		assertTrue("defining_code attribute missing", 
				attrs.contains("defining_code"));				
	}
	
	private RMInspector inspector;
}
