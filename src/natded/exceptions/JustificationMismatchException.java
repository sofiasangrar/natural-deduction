package natded.exceptions;

import natded.constants.Step;

public class JustificationMismatchException extends RuntimeException {

    public JustificationMismatchException(Step wrong, Step actual){
        super("Justification mismatch: you used " + wrong + ". Did you mean " + actual + "?");
    }
}
