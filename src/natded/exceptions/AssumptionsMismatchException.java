package natded.exceptions;

public class AssumptionsMismatchException extends RuntimeException {

    public AssumptionsMismatchException(){
        super("Assumptions in premises and conclusion do not match");
    }
}
