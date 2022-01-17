package lexer.tokens;

public class FalseToken extends Token {

    // constructor
    public FalseToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    public static String getString() {
        return "F";
    }

    /*
    @Override
    public String toString() {
        return "F";
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FalseToken || obj.toString().equals("F");
    }
}
