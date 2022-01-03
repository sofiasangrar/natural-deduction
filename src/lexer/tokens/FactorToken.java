package lexer.tokens;

public abstract class FactorToken extends Token {

    // constructor
    public FactorToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "an expression";
    }
}
