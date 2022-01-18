package lexer.tokens;

public class ImpliesToken extends Token  {

    private static final String[] keys = new String[]{"=>", "->"};
    public static final char code = 8658;

    public ImpliesToken() {
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
        return obj instanceof ImpliesToken || obj.toString().equals(this.toString());
    }

    public static String[] getKeys(){
        return keys;
    }

}
