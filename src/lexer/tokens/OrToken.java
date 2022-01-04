package lexer.tokens;

public class OrToken extends Token {

    // returns the precedence
    @Override
    public int precedence() {
        return 2;
    }

    // constructor
    public OrToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected character for the error handling
    @Override
    public String getString() {
        return "'V'";
    }

    @Override
    public String toString() {
        char or = 8744;
        return or + "";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrToken || obj.toString().equals("V") || obj.toString().equals(this.toString());
    }
}
