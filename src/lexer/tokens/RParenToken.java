package lexer.tokens;

public class RParenToken extends Token {

    // constructor
    public RParenToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    public static String getString() {
        return ")";
    }


    /*
    @Override
    public String toString() {
        return ")";
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RParenToken || obj.toString().equals(")");
    }
}
