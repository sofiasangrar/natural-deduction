package lexer.tokens;

public class AndToken extends OperatorToken {

    // returns the precedence of the operator
    @Override
    public int precedence() {
        return 1;
    }

    // constructor
    public AndToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "'^'";
    }

    @Override
    public String toString() {
        char land = 8743;
        return land + "";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AndToken || obj.toString().equals("^") || obj.toString().equals(this.toString());
    }
}
