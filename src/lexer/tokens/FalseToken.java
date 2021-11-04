package lexer.tokens;

public class FalseToken extends ExprToken {

    // constructor
    public FalseToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "F";
    }

    @Override
    public String toString() {
        return "F";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FalseToken || obj.toString().equals("F");
    }
}
