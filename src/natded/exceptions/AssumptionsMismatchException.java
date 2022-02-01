package natded.exceptions;

import natded.constants.Step;

public class AssumptionsMismatchException extends RuntimeException {

    public AssumptionsMismatchException(){
        super("Assumptions in antecedents and conclusion do not match");
    }
}
