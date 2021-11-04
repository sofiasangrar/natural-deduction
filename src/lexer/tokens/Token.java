package lexer.tokens;
public abstract class Token {

	// stores the column of the first character
	private int startingColumnOfToken;

	// a method to return the precedence of a token
	public int precedence() {
		//set precedence of all non-operators to 0 - this is overridden for operators
	    return 0;
	  }

	// allows for super() to be used for all of the tokens when creating their constructors
	public Token(int startingColumnOfToken) {
		this.startingColumnOfToken = startingColumnOfToken;
	}

	// defaults the expected character/string to '' but can be overridden when required
    public String getString() {
	    return "";
    }

    // returns the column that the token starts on
    public int getStartingColumnOfToken() {
		return startingColumnOfToken;
	}

}
