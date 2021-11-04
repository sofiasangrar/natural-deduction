package lexer.tokens;

public class ExprToken extends Token {

    // constructor
    public ExprToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "an expression";
    }
}
