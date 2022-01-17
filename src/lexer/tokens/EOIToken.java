package lexer.tokens;

//End of Input Token
public final class EOIToken extends Token {

    // constructor
    // sets the line number of the end of input token to 0
    public EOIToken() {
        super(0);
    }

    public static String getString() {
        return "End of Input";
    }

    /*
    @Override
    public String expectedString(){
        return getString();
    }

    @Override
    public String toString() {
        return getString();
    }
    */
}
