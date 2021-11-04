package lexer.tokens;

public abstract class OperatorToken extends Token {

	// a method to return the precedence of the operators
	public abstract int precedence();

	// constructor
	public OperatorToken(int startingColumnOfToken) {
		super(startingColumnOfToken);
	}


}
