package se.acode.openehr.parser.errors;

import org.openehr.am.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pieter.bos on 19/10/15.
 */
public class ArchetypeADLParserErrors {

    private static final Logger logger = LoggerFactory.getLogger(ArchetypeADLParserErrors.class);

    private List<ArchetypeADLParserMessage> errors = new ArrayList<>();
    private List<ValidationError> validationErrors = new ArrayList<>();
    private List<ArchetypeADLParserMessage> warnings = new ArrayList<>();

    public void addError(String error) {
        errors.add(new ArchetypeADLParserMessage(error));
    }
    public void addValidationError(ValidationError error) {
        validationErrors.add(error);
    }

    public void addWarning(String error) {
        warnings.add(new ArchetypeADLParserMessage(error));
    }

    public void logToLogger() {
        for(ArchetypeADLParserMessage message:warnings) {
            logger.warn(message.getMessage());
        }
        for(ArchetypeADLParserMessage message:errors) {
            logger.error(message.getMessage());
        }
    }

    public List<ArchetypeADLParserMessage> getErrors() {
        return errors;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setErrors(List<ArchetypeADLParserMessage> errors) {
        this.errors = errors;
    }

    public List<ArchetypeADLParserMessage> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<ArchetypeADLParserMessage> warnings) {
        this.warnings = warnings;
    }

    public boolean hasNoMessages() {
        return this.getErrors().isEmpty() && this.getWarnings().isEmpty();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        append(result, "Warning", warnings);
        append(result, "Error", errors);
        return result.toString();
    }

    private void append(StringBuilder result, String level, List<ArchetypeADLParserMessage> messages) {
        for(ArchetypeADLParserMessage message:messages) {
            result.append(level);
            result.append(": ");
            result.append(message.getMessage());
            result.append("\n");
        }
    }
}
