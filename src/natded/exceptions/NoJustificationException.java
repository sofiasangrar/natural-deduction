package natded.exceptions;


public class NoJustificationException extends RuntimeException {

    public NoJustificationException(){
        super("No justification given.");
    }
}
