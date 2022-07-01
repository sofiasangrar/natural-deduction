package natded.exceptions;

public class AntecedentsMismatchException extends RuntimeException {

    public AntecedentsMismatchException(){
        super("Antecedents in premises and conclusion do not match");
    }
}
