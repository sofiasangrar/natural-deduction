package lexer.tokens;

import lexer.Lexer;

public abstract class Token {

	private int startingColumnOfToken;

	public static String getString(){
		return "input";
	}

	public String expectedString(){
		return getString();
	}

	@Override
	public String toString() {
		return getString();
	}

	public Token() {
		this.startingColumnOfToken = Lexer.tokenColumn;
	}

    public int getStartingColumnOfToken() {
		return startingColumnOfToken;
	}

}
