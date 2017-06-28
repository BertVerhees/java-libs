package se.acode.openehr.parser.builder;

import org.openehr.rm.datatypes.text.CodePhrase;
import se.acode.openehr.parser.v1_4.ArchetypeParser;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;

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

    static void addEntry(ArchetypeParser.String_valueContext s, ArchetypeParser.Object_value_blockContext o, Map<String,String> map, ArchetypeADLErrorListener errorListener) {
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

}
