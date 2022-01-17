package lexer.tokens;

public class TrueToken extends Token {

    // constructor
    public TrueToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    public static String getString() {
        return "T";
    }

    /*
    @Override
    public String toString() {
        return "T";
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrueToken || obj.toString().equals("T");
    }
}
