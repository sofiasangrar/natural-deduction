package lexer.tokens;

public class IllegalStringToken extends Token {

	private String illegalString;

	public IllegalStringToken(String illegalString) {
		super();
		this.illegalString = illegalString;
	}

	public static String getString(){
		return "an illegal string";
	}

	@Override
	public String expectedString() {
		return getString() + ": '"+ toString() + "'";
	}

	@Override
	public String toString() {
		return illegalString;
	}

}
