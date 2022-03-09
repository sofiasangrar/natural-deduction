package natded.exceptions;


public class SyntaxError extends RuntimeException {

    public SyntaxError(){
        super("Check syntax.");
    }

    public SyntaxError(String expected, String actual, int charNo){
        super("Check syntax at char " + charNo + ". Expected " + expected + ", but received " + actual + ".");
    }
}
