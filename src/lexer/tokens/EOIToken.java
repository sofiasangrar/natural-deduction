package lexer.tokens;

public final class EOIToken extends Token {

    public EOIToken() {
        super();
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public String expectedString() {
        return getString();
    }

    public static String getString() {
        return "End of Input";
    }

}
