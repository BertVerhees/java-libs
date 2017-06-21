package org.openehr.am.serialize;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author markopi
 */
public class StringEscapeTest extends SerializerTestBase {

    @Test
    public void testEscapeMapValues() throws IOException {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("at0002", "Characters to escape: \"");

        StringWriter writer = new StringWriter();
        outputter.printMap(map, writer, 0);
        String serialized = writer.toString().trim();

        assertEquals("[\"at0002\"] = <\"Characters to escape: \\\"\">", serialized);
    }

    @Test
    public void testEscapeMapKeys() throws IOException {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("term\"inology", "Terminology");

        StringWriter writer = new StringWriter();
        outputter.printMap(map, writer, 0);
        String serialized = writer.toString().trim();

        assertEquals("[\"term\\\"inology\"] = <\"Terminology\">", serialized);
    }

    @Test
    public void testEscapeStringList() throws IOException {
        List<String> list = new ArrayList<String>();
        list.add("Escape: \"");
        list.add("Other");

        StringWriter writer = new StringWriter();
        outputter.printList(list, writer, true);
        String serialized = writer.toString().trim();

        assertEquals("\"Escape: \\\"\",\"Other\"", serialized);
    }

    @Test
    public void testKeepNonStringList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);

        StringWriter writer = new StringWriter();
        outputter.printList(list, writer, false);
        String serialized = writer.toString().trim();

        assertEquals("1,2", serialized);
    }

}
