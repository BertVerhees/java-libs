package org.openehr.am.serialize;

import org.junit.Test;
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.basic.Interval;
import org.openehr.rm.support.identification.TerminologyID;

import java.util.Arrays;
import java.util.List;

public class CCodePhraseTest extends SerializerTestBase {

	@Test
	public void testPrintCCodePhrase() throws Exception {
		String[] codes = { "at2001", "at2002", "at2003" };
		String terminology = "local";
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				new TerminologyID(terminology), Arrays.asList(codes), null,
				null);

		clean();
		outputter.printCCodePhrase(ccoded, 1, out);
		verify("\t[" + terminology + "::\r\n" + "\t" + codes[0] + ",\r\n"
				+ "\t" + codes[1] + ",\r\n" + "\t" + codes[2] + "]\r\n");
	}

	@Test
	public void testPrintCCodePhraseWithSingleCode() throws Exception {
		String[] codes = { "at3102.0" };
		String terminology = "local";
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				new TerminologyID(terminology), Arrays.asList(codes), null,
				null);
		clean();
		outputter.printCCodePhrase(ccoded, 0, out);
		verify("[local::at3102.0]\r\n");
	}

	@Test
	public void testPrintCCodePhraseWithSingleCodeAssumedValue()
			throws Exception {
		String[] codes = { "at3102.0" };
		String terminology = "local";
		CodePhrase assumed = new CodePhrase(terminology, codes[0]);
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				new TerminologyID(terminology), Arrays.asList(codes), null,
				assumed);
		clean();
		outputter.printCCodePhrase(ccoded, 0, out);
		verify("[local::at3102.0;at3102.0]\r\n");
	}

	@Test
	public void testPrintCCodePhraseWithAssumedValue() throws Exception {
		String[] codes = { "F43.00", "F43.01", "F32.02" };
		String terminology = "icd10";
		CodePhrase assumed = new CodePhrase(terminology, codes[1]);
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				new TerminologyID(terminology), Arrays.asList(codes), null,
				assumed);
		clean();
		outputter.printCCodePhrase(ccoded, 0, out);
		verifyByFile("c-code-phrase-test.adl");		
	}

	@Test
	public void testPrintCCodePhraseWithNoCode() throws Exception {
		String terminology = "local";
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				new TerminologyID(terminology), null, null,	null);
		clean();
		outputter.printCCodePhrase(ccoded, 0, out);
		verify("[local::]\r\n");
	}

	@Test
	public void testPrintEmptyCCodePhrase() throws Exception {
		List<String> codeList = null;
		TerminologyID terminology = null;
		CodePhrase defaultValue = null;
		CodePhrase assumedValue = null;
		Interval<Integer> occurrences = new Interval<Integer>(1, 1);
		CCodePhrase ccoded = new CCodePhrase("/path", occurrences, null, null,
				terminology, codeList, defaultValue, assumedValue);
		clean();
		outputter.printCCodePhrase(ccoded, 0, out);
		verifyByFile("c-code-phrase-test-empty.adl");		
	}
}
