package natded.exceptions;

import natded.constants.Step;

public class PremiseNumberException extends RuntimeException {

    public PremiseNumberException(Step justification, int expected, int actual){
        super("Incorrect number of premises for " + justification + ": you used " + actual + ". Expected " + expected + ".");
    }
}
