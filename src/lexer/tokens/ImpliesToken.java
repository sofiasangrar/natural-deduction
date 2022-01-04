package lexer.tokens;

public class ImpliesToken extends Token  {

    // constructor
    public ImpliesToken(int startingColumnOfToken) {
        super(startingColumnOfToken);
    }

    // returns the expected input of the token
    @Override
    public String getString() {
        return "'=>'";
    }

    @Override
    public String toString() {
        char implies = 8658;
        return implies+"";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImpliesToken || obj.toString().equals("=>") || obj.toString().equals(this.toString());
    }
}
