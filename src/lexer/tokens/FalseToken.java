package lexer.tokens;

public class FalseToken extends BooleanToken {

    public FalseToken() {
        super();
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public String expectedString(){
        return "'" + getString() + "'";
    }

    public static String getString() {
        return "F";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FalseToken || obj.toString().equals(this.toString());
    }
}
