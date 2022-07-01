package lexer.tokens;

public class OrToken extends Token {
    private static final String[] keys = new String[]{"V", "v"};
    public static final char code = 8744;

    public OrToken() {
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
    public String expectedString() {
        return getString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrToken || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }

}
