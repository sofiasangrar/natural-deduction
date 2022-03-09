package natded.exceptions;

import natded.constants.Step;

public class RuleException extends RuntimeException {

    public RuleException(String text) {
        super(text);
    }

    public RuleException(Step step, String text) {
        super(step + " provided as justification, but " + text + ".");
    }
}
