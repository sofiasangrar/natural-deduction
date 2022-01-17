package lexer.tokens;

public class CommaToken extends Token {

    // constructor
    public CommaToken(int startingColumnOfToken) {
        super(startingColumnOfToken);

    }

    public static String getString() {
        return ",";
    }

    /*
    @Override
    public String toString() {
        return "'"+ getString() + "'";
    }

    @Override
    public String expectedString(){
        return getString();
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommaToken || obj.toString().equals(this.toString());
    }
}
