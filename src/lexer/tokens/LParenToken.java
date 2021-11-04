package lexer.tokens;

public class LParenToken extends Token {

    // constructor
    public LParenToken(int startingColumnOfToken) {
        super( startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "'('";
    }

    @Override
    public String toString() {
        return "(";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LParenToken || obj.toString().equals("(");
    }
}
