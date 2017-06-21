package se.acode.openehr.parser.errors;

/**
 * An error, info or warning message from the archetype parsing
 *
 * Created by pieter.bos on 19/10/15.
 */
public class ArchetypeADLParserMessage {

    private String message;

    public ArchetypeADLParserMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
