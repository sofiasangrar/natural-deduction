package lexer.tokens;
public abstract class Token {

	// stores the column of the first character
	private int startingColumnOfToken;

	public static String getString(){
		return "";
	}

	public String expectedString(){
		return "'" + getString() + "'";
	}

	@Override
	public String toString() {
		return getString();
	}

	// allows for super() to be used for all of the tokens when creating their constructors
	public Token(int startingColumnOfToken) {
		this.startingColumnOfToken = startingColumnOfToken;
	}

    // returns the column that the token starts on
    public int getStartingColumnOfToken() {
		return startingColumnOfToken;
	}

}
