package se.acode.openehr.parser.builder;

import org.openehr.rm.datatypes.text.CodePhrase;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;
import se.acode.openehr.parser.v1_4.ArchetypeParser;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by verhees on 1-6-17.
 */
public class BuilderUtils {
    static CodePhrase returnCodePhraseFromTermCodeRefString(String termCodeRef, ArchetypeADLErrorListener errorListener) {
        if(termCodeRef.startsWith("[")){
            termCodeRef =  termCodeRef.substring(1, termCodeRef.length()-1);
        }
        String[] parts = termCodeRef.split("::");
        if (parts.length != 2) {
            errorListener.getParserErrors().addError("Term code:\"" + termCodeRef + "\" wrong formatted.");
            return null;
        }
        return new CodePhrase(parts[0], parts[1]);
    }

    static void addEntry(ArchetypeParser.String_valueContext s, ArchetypeParser.Object_blockContext o, Map<String,String> map, ArchetypeADLErrorListener errorListener) {
        String keyString = s.STRING().getText();
        keyString = keyString.substring(1, keyString.length() - 1);
        if((o==null)||(o.primitive_object()==null)||(o.primitive_object().primitive_value()==null)||(o.primitive_object().primitive_value().string_value()==null)){
            errorListener.getParserErrors().addError("Expected a single/simple string as value for \""+keyString+"\".");
            return;
        }
        String value = o.primitive_object().primitive_value().string_value().STRING().getText();
        value = value.substring(1, value.length() - 1);
        map.put(keyString, value);
    }

    static String handleSingleStringItem(String string, String item, ArchetypeParser.Attr_valContext attrValContext, String itemDesciption, ArchetypeADLErrorListener errorListener ){
        if (string == null) {
            string = item;
            if(string.startsWith("\"")&&string.endsWith("\""))
                string = string.substring(1, string.length() - 1);
        } else {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one "+itemDesciption+"-section per description."));
        }
        return string;
    }

    static List<String> handleSingleStringItemList(List<String> strings, String item, ArchetypeParser.Attr_valContext attrValContext, String itemDesciption, ArchetypeADLErrorListener errorListener ){
        if (strings == null) {
            strings = new ArrayList<>();
            String keyword = item;
            strings.add(keyword);
        } else {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one "+itemDesciption+"-section per resource-description-item."));
        }
        return strings;
    }

    static Map<String, String> handleStringStringMap(Map<String, String> map, ArchetypeParser.Attr_valContext attrValContext, String itemDesciption, ArchetypeADLErrorListener errorListener ){
        if (map == null) {
            map = new HashMap<>();
        } else {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one "+itemDesciption+"-section per resource-description-item."));
        }
        for (ArchetypeParser.Keyed_objectContext keyedObjectContext : attrValContext.object_block().keyed_object()) {
            ArchetypeParser.Object_blockContext o = keyedObjectContext.object_block();
            ArchetypeParser.String_valueContext sa = keyedObjectContext.primitive_value().string_value();
            addEntry(sa, o, map, errorListener);
        }
        return map;
    }

    static List<String> handleStringItemList(List<String> strings, ArchetypeParser.Attr_valContext attrValContext, String itemDesciption, ArchetypeADLErrorListener errorListener ){
        if (strings == null) {
            strings = new ArrayList<>();
        } else {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one "+itemDesciption+"-section per resource-description-item."));
        }

        for (ArchetypeParser.Primitive_valueContext primitiveValueContext : attrValContext.object_block().primitive_object().primitive_list_value().primitive_value()) {
            String string = primitiveValueContext.string_value().STRING().getText();
            string = string.substring(1, string.length() - 1);
            strings.add(string);
        }
        return strings;
    }

    static CodePhrase handleSingleCodePhraseItem(CodePhrase codePhrase, String item, ArchetypeParser.Attr_valContext attrValContext, String itemDesciption, ArchetypeADLErrorListener errorListener ){
        if (codePhrase == null) {
            codePhrase = BuilderUtils.returnCodePhraseFromTermCodeRefString(attrValContext.object_block().primitive_object().primitive_value().term_code_value().TERM_CODE_REF().getText(), errorListener);;
        } else {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one "+itemDesciption+"-section per description."));
        }
        return codePhrase;
    }


}
