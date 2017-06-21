package se.acode.openehr.parser.errors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.acode.openehr.parser.ArchetypeLexer;
import se.acode.openehr.parser.ArchetypeParser;

import java.util.BitSet;

/**
 * Created by pieter.bos on 19/10/15.
 */
public class ArchetypeADLErrorListener implements ANTLRErrorListener {

    private static final Logger logger = LoggerFactory.getLogger(ArchetypeADLErrorListener.class);
    private final ArchetypeADLParserErrors errors;

    public ArchetypeADLErrorListener(ArchetypeADLParserErrors errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String token = "";
        if(offendingSymbol instanceof CommonToken){
            if(recognizer instanceof ArchetypeLexer) {
                try {
                    token = String.format("(token (%d): %s)", ((CommonToken) offendingSymbol).getType(), recognizer.getVocabulary().getDisplayName(((CommonToken) offendingSymbol).getType()));
                } catch (Exception f) {
                }
            }
            if(recognizer instanceof ArchetypeParser) {
                try {
                    token = String.format("(token (%d): %s)", ((CommonToken) offendingSymbol).getType(), recognizer.getVocabulary().getDisplayName(((CommonToken) offendingSymbol).getType()));
                } catch (Exception f) {
                }
            }
        }
        String error = String.format("syntax error at %d:%d: %s %s.  msg: %s", line, charPositionInLine, offendingSymbol, token, msg);
        logger.warn(error);
        errors.addError(error);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        String input = recognizer.getInputStream().getText(new Interval(startIndex, stopIndex));
        String warning = String.format("FULL AMBIGUITY: %d-%d, exact: %b, input: %s", startIndex, stopIndex, exact, input);
        logger.warn(warning);
        errors.addWarning(warning);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        String input = recognizer.getInputStream().getText(new Interval(startIndex, stopIndex));
        logger.warn("FULL CONTEXT: {}-{}, alts: {}, {}", startIndex, stopIndex, conflictingAlts, input);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        logger.warn("CONTEXT SENSITIVITY: {}-{}, prediction: {}", startIndex, stopIndex, prediction);
    }

    public ArchetypeADLParserErrors getErrors() {
        return errors;
    }
}
