package se.acode.openehr.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.ArchetypeConstraint;
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test case tests parsing of domain type constraints extension.
 *
 * @author Rong Chen
 * @version 1.0
 */

public class CCodePhraseTest extends ParserTestBase {

	/**
	 * The fixture set up called before every test method.
	 */
	@Before
	public void setUp() throws Exception {
		ADLParser parser = new ADLParser(
				loadFromClasspath("adl-test-entry.c_code_phrase.test.adl"));
		archetype = parser.parse();
		parser.generatedParserException();
	}

	/**
	 * The fixture clean up called after every test method.
	 */
	@After
	public void tearDown() throws Exception {
		archetype = null;
		node = null;
	}

	/**
	 * Verifies parsing of a simple CCodePhrase
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParseExternalCodes() throws Exception {
		node = archetype.node("/types[at0001]/items[at10002]/value");
		String[] codes = { "F43.00", "F43.01", "F32.02" };
		assertCCodePhrase(node, "icd10", codes, null);
	}

	@Test
	public void testParseExternalCodesWithAssumedValue() throws Exception {
		node = archetype.node("/types[at0001]/items[at10005]/value");
		String[] codes = { "F43.00", "F43.01", "F32.02" };
		assertCCodePhrase(node, "icd10", codes, "F43.00");
	}
	
	/**
	 * Verifies parsing of a simple CCodePhrase with codes defined locally
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParseLocalCodes() throws Exception {
		node = archetype.node("/types[at0001]/items[at10003]/value");
		String[] codeList = { "at1311","at1312", "at1313", "at1314","at1315" }; 
		assertCCodePhrase(node, "local", codeList, null);
	}

	@Test
	public void testParseEmptyCodeList() throws Exception {
		node = archetype.node("/types[at0001]/items[at10004]/value");
		String[] codeList = null; 
		assertCCodePhrase(node, "icd10", codeList, null);
	}
	
	private void assertCCodePhrase(ArchetypeConstraint actual,
			String terminologyId, String[] codes, String assumedValue) {
		
		// check type
		assertTrue("CCodePhrase expected, got " + actual.getClass(), 
				actual instanceof CCodePhrase);
		CCodePhrase cCodePhrase = (CCodePhrase) actual;
		
		// check terminology
		assertEquals("terminology", terminologyId, 
				cCodePhrase.getTerminologyId().getValue());
		
		// check code list
		if(codes == null) {
			assertNull("codeList expected null", cCodePhrase.getCodeList());
		} else {
			List<String> codeList = cCodePhrase.getCodeList();
			assertEquals("codes.size wrong", codes.length, codeList.size());		
			for (int i = 0; i < codes.length; i++) {
				Object c = codeList.get(i);
				assertEquals("code wrong, got: " + c, codes[i], c);
			}
		}
		
		// check assumed value
		if(assumedValue == null) {
			assertFalse(cCodePhrase.hasAssumedValue());
		} else {
			assertTrue("expected assumedValue", cCodePhrase.hasAssumedValue());
			assertEquals("assumed value wrong", assumedValue, 
				cCodePhrase.getAssumedValue().getCodeString());
		}
	}

	private Archetype archetype;
	private ArchetypeConstraint node;
}
