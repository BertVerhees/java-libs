package se.acode.openehr.parser.exception;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by verhees on 16-6-17.
 */
public class ArchetypeBuilderException extends Exception{

    public ArchetypeBuilderException(ParserRuleContext parserRuleContext, String message){
        super(buildMessage(parserRuleContext, message));
    }

    public ArchetypeBuilderException(String message){
        super(message);
    }

    public ArchetypeBuilderException(ParserRuleContext parserRuleContext, String message, Exception e){
        super(buildMessage(parserRuleContext, message), e);
    }

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
