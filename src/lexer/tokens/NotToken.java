package lexer.tokens;

public class NotToken extends Token {

    // constructor
    public NotToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "'!'";
    }

    @Override
    public String toString() {
        char not = 172;
        return not + "";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotToken || obj.toString().equals("!") || obj.toString().equals(this.toString());
    }
}
