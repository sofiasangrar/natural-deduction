package lexer.tokens;

public class EmptyToken extends Token {

    private static final String[] keys = new String[]{"%", "0"};
    public static final char code = '\u2205';

    public EmptyToken() {
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
        return obj instanceof EmptyToken || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }

}
