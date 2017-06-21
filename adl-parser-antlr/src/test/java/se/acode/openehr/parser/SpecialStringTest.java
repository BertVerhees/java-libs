package se.acode.openehr.parser;

import org.junit.Before;
import org.junit.Test;
import org.openehr.am.archetype.constraintmodel.CAttribute;
import org.openehr.am.archetype.constraintmodel.CComplexObject;

import java.util.List;

public class SpecialStringTest extends ParserTestBase {

	@Before
	public void SpecialStringTest() throws Exception {
		ADLParser parser = new ADLParser(loadFromClasspath(
			"adl-test-entry.special_string.test.adl"));
		attributeList = parser.parse().getDefinition().getAttributes();
	}

	@Test
	public void testParseEscapedDoubleQuote() throws Exception {
		list = getConstraints(0);
		assertCString(list.get(0), null, new String[] { "some\"thing" }, null);
	}

	@Test
	public void testParseEscapedBackslash() throws Exception {
		list = getConstraints(0);
		assertCString(list.get(1), null, new String[] { "any\\thing" }, null);
	}
	
	private List getConstraints(int index) {
		CAttribute ca = (CAttribute) attributeList.get(index);
		return ((CComplexObject) ca.getChildren().get(0)).getAttributes();
	}
	
	private List attributeList;
	private List list;
}
