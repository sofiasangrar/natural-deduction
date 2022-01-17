package lexer.tokens;

public class OrToken extends Token {
    private static final String[] keys = new String[]{"V", "v"};
    public static final char code = 8744;

    // constructor
    public OrToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    public static String getString() {
        return code + "";
    }

    /*
    @Override
    public String toString() {
        char or = 8744;
        return or + "";
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrToken || obj.toString().equals("V") || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }

}
