package lexer.tokens;

public class TrueToken extends FactorToken {

    // constructor
    public TrueToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "T";
    }

    @Override
    public String toString() {
        return "T";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrueToken || obj.toString().equals("T");
    }
}
