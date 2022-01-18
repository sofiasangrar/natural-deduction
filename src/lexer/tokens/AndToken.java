package lexer.tokens;

public class AndToken extends Token {

    private static final String[] keys = new String[]{"^", "&", "+"};

    public static final char code = '\u2227';

    public AndToken() {
        super();
    }

    public static String getString() {
        return code + "";
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
        return obj instanceof AndToken || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }
}
