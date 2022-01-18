package lexer.tokens;

public class LParenToken extends Token {

    public LParenToken() {
        super();
    }

    public static String getString() {
        return "(";
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
        return obj instanceof LParenToken || obj.toString().equals(this.toString());
    }
}
