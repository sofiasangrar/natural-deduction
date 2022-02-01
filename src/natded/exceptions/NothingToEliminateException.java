package natded.exceptions;

import natded.constants.Step;

public class NothingToEliminateException extends RuntimeException {

    public NothingToEliminateException(String expected){
        super("No " + expected + " to eliminate.");
    }
}
