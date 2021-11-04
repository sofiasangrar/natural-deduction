package lexer.tokens;

public class RParenToken extends Token {

    // constructor
    public RParenToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "')'";
    }

    @Override
    public String toString() {
        return ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RParenToken || obj.toString().equals(")");
    }
}
