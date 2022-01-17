package lexer.tokens;

public class NotToken extends Token {

    private static final String[] keys = new String[]{"!", "~"};
    public static final char code = 172;

    // constructor
    public NotToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    public static String getString() {
        return code + "";
    }

    /*
    @Override
    public String toString() {
        char not = 172;
        return not + "";
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotToken || obj.toString().equals("!") || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }

}
