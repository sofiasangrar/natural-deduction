package lexer.tokens;

public class IllegalStringToken extends Token {

	// Stores the illegal string
	private String illegalString;

	// constructor
	public IllegalStringToken(String illegalString, int startingColumnOfToken) {
		super(startingColumnOfToken);
		this.illegalString = illegalString;
	}

	// gets the error message specific to the token
	@Override
	public String getString() {
		return "an illegal string: " + "\"" + illegalString + "\"";
	}
}
