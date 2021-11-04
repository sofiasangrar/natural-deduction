package lexer.tokens;

public class NDToken extends Token {

    // constructor
    public NDToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "\"|-\"";
    }

    @Override
    public String toString() {
        char nd = 8870;
        return nd + "";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NDToken || obj.toString().equals("|-") || obj.toString().equals(this.toString());
    }
}
