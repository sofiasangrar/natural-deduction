package lexer.tokens;

public class IdentToken extends FactorToken {

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

    // returns the expected input of the token
    @Override
    public String getString() {
        return "an identifier";
    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(identifier);
    }
}
