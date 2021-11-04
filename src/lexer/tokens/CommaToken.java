package lexer.tokens;

public class CommaToken extends Token {

    // constructor
    public CommaToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "','";
    }

    @Override
    public String toString() {
        return ",";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommaToken || obj.toString().equals(",");
    }
}
