package lexer.tokens;

public class EmptyToken extends Token {

    // constructor
    public EmptyToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "%";
    }

    @Override
    public String toString() {
        return "%";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyToken || obj.toString().equals("%");
    }
}
