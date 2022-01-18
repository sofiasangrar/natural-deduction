package lexer.tokens;

public class IdentToken extends Token {

    private String identifier;

    public IdentToken(String identifier) {
        super();
        this.identifier = identifier;

    }

    public String getAttr() {
        return identifier;
    }


    public static String getString() {
        return "an identifier";
    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public String expectedString() {
        return getString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(identifier);
    }
}
