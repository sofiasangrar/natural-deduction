package lexer.tokens;

public class TrueToken extends BooleanToken {

    public TrueToken() {
        super();
    }

    public static String getString() {
        return "T";
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
        return obj instanceof TrueToken || obj.toString().equals(this.toString());
    }
}
