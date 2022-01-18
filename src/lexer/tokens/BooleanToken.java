package lexer.tokens;

public class BooleanToken extends Token {

    public BooleanToken() {
        super();
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public String expectedString(){
        return "'" + TrueToken.getString() + "' or '" + FalseToken.getString() + "'";
    }

    public static String getString() {
        return "a boolean expression";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FalseToken || obj.toString().equals(this.toString());
    }

}
