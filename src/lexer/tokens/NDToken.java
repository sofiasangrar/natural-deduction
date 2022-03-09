package lexer.tokens;

public class NDToken extends Token {

    private static final String[] keys = new String[]{"|-", ":"};
    public static final char code = '\u22A2';

    public NDToken() {
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
        return obj instanceof NDToken || obj.toString().equals(this.toString());
    }


    public static String[] getKeys(){
        return keys;
    }

}
