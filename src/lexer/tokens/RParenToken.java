package lexer.tokens;

public class RParenToken extends Token {

    public RParenToken() {
        super();
    }

    public static String getString() {
        return ")";
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public String expectedString(){
        return "'" + getString() + "'";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RParenToken || obj.toString().equals(this.toString());
    }
}
