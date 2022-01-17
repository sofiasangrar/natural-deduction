package lexer.tokens;

public class IdentToken extends Token {

    // Stores the identifier of the token
    private String identifier;

    // constructor
    public IdentToken(String identifier, int startingColumnOfToken) {
        super(startingColumnOfToken);
        this.identifier = identifier;

    }

    // returns the identifier
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
