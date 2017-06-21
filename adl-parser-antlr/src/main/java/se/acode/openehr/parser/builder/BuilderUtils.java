package se.acode.openehr.parser.builder;

import org.openehr.rm.datatypes.text.CodePhrase;
import se.acode.openehr.parser.ArchetypeParser;

import java.util.Map;

/**
 * Created by verhees on 1-6-17.
 */
public class BuilderUtils {
    static CodePhrase returnCodePhraseFromTermCodeRefString(String termCodeRef) throws Exception {
        if(termCodeRef.startsWith("[")){
            termCodeRef =  termCodeRef.substring(1, termCodeRef.length()-1);
        }
        String[] parts = termCodeRef.split("::");
        if (parts.length != 2) {
            throw new Exception("Term code:\"" + termCodeRef + "\" wrong formatted.");
        }
        return new CodePhrase(parts[0], parts[1]);
    }

    static void addEntry(ArchetypeParser.String_valueContext s, ArchetypeParser.Object_blockContext o, Map<String,String> map) throws Exception {
        String keyString = s.STRING().getText();
        keyString = keyString.substring(1, keyString.length() - 1);
        if(
                (o.object_value_block()==null)&&
                        (o.object_value_block().primitive_object()!=null)&&
                        (o.object_value_block().primitive_object().primitive_value()!=null)&&
                        (o.object_value_block().primitive_object().primitive_value().string_value()!=null)){
            throw new Exception("Expected a single/simple string as value for \""+keyString+"\".");
        }
        String value = o.object_value_block().primitive_object().primitive_value().string_value().STRING().getText();
        value = value.substring(1, value.length() - 1);
        map.put(keyString, value);
    }

}
