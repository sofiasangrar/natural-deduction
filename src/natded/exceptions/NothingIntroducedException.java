package natded.exceptions;

public class NothingIntroducedException extends RuntimeException {

    public NothingIntroducedException(String expected){
        super(expected.substring(0, 1).toUpperCase() + expected.substring(1) + " not introduced.");
    }
}
