package lexer.tokens;

public class BiconditionalToken extends OperatorToken {

    @Override
    public int precedence() {
        return 2;
    }

    // constructor
    public BiconditionalToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "'<=>'";
    }

    @Override
    public String toString() {
        char biconditional = 8660;
        return biconditional + "";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BiconditionalToken || obj.toString().equals("<=>") || obj.toString().equals(this.toString());
    }
}
