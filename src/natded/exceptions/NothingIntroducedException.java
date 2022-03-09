package natded.exceptions;

import natded.constants.Step;

public class NothingIntroducedException extends RuntimeException {

    public NothingIntroducedException(Step justification, String expected){
        super(justification + " used as justification, but "  + expected + " not introduced.");
    }
}
