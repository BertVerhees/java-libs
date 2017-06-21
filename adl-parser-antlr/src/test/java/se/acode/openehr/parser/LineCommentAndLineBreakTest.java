package se.acode.openehr.parser;

import org.junit.Test;
import org.openehr.am.archetype.Archetype;

/**
 * Created by verhees on 12-6-17.
 */
public class LineCommentAndLineBreakTest extends ParserTestBase {
    //adl-test-entry.archetype_comment_line_breaks1.test
    @Test
    public void testParseNoExtraLineBreakNoComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks1.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

    @Test
    public void testParseExtraLineBreakNoComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks2.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\n" +
                        "\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

    @Test
    public void testParseNoExtraLineBreakComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks3.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\t--  empty definition test\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

    @Test
    public void testParseExtraLineBreakComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks4.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\t--  empty definition test\n" +
                        "\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

    @Test
    public void testParseExtraLineBreakNoCommentButExtraComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks5.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\n" +
                        "--  empty definition test\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

    @Test
    public void testParseExtraLineBreakCommentExtraComment() throws Exception {
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.archetype_comment_line_breaks6.test.adl"));
        try {
            Archetype archetype = parser.parse();
        }catch(Exception e){
            if("null originalLanguage".equals(e.getMessage())){
                throw new Exception("Error in parsing:\n"
                        +"concept\n" +
                        "\t[at0000]\t--  empty definition test\n" +
                        "--  empty definition test\n" +
                        "language\n" +
                        "\toriginal_language = <[ISO_639-1::en]>");
            }
        }
    }

}
