package lexer.tokens;

public class IllegalStringToken extends Token {

	// Stores the illegal string
	private String illegalString;

	// constructor
	public IllegalStringToken(String illegalString, int startingColumnOfToken) {
		super(startingColumnOfToken);
		this.illegalString = illegalString;
	}

	public static String getString(){
		return "an illegal string";
	}

	@Override
	public String expectedString() {
		return "an illegal string: " + "\"" + illegalString + "\"";
	}

	@Override
	public String toString() {
		return illegalString;
	}

}
