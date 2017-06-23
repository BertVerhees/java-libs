package se.acode.openehr.parser.errors;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by verhees on 16-6-17.
 */
public class ArchetypeBuilderError {

    public static String buildMessage(ParserRuleContext parserRuleContext, String message){
        String exception = message;
        if(parserRuleContext!=null) {
            String start = "line: " + parserRuleContext.getStart().getLine() + ", " + "char-position: " +  parserRuleContext.getStart().getCharPositionInLine();
            String term =  parserRuleContext.getStart().getText();
            if(term.length()>25) term = term.substring(0,24);
            exception = exception + ". \nParser-class: \"" + parserRuleContext.getClass().getSimpleName()+"\". \nParser-related-error location:" + start + ", token: \"" + term + "\"";
        }
        return exception;
    }

}
