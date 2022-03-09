package natded.exceptions;

import natded.constants.Step;

public class NothingToEliminateException extends RuntimeException {

    public NothingToEliminateException(Step justification, String expected){
        super(justification + " used as justification, but no " + expected + " to eliminate.");
    }
}
