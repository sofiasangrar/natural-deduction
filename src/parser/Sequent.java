package parser;

import lexer.Lexer;
import lexer.tokens.EOIToken;
import lexer.tokens.EmptyToken;
import lexer.tokens.TurnstileToken;

import java.util.List;

import static natded.NatDedUtilities.turnstile;

public class Sequent {

	private Antecedents antecedents;
	private Expr conclusion;

	private Sequent(Antecedents antecedents, Expr expression) {
		this.antecedents = antecedents;
		this.conclusion = expression;
	}

	/**
	 * parse the next tokens as a valid sentence in natural deduction, i.e. <antecedents> <turnstile> <conclusion>
	 * @return sentence of natural deduction
	 */
  	public static Sequent parse(String sequentString) {
  		Lexer.setLexString(sequentString);
  		Parser.clearError();
  		Parser.t = Lexer.lex();
  		Antecedents antecedents = new Antecedents();
		Expr expression = new Expr();

		//parse assumption, if there are any
		if(Parser.t instanceof EmptyToken) {
			Parser.t= Lexer.lex();
		} else {
			antecedents = Antecedents.parse();
			if (antecedents.getAntecedents() == null || antecedents.getAntecedents().size() == 0){
				Parser.errorHandle("antecedents");
			}

		}

		//antecedents must be followed by turnstile
		if(Parser.t instanceof TurnstileToken) {
			Parser.t = Lexer.lex();
		} else {
			Parser.errorHandle(TurnstileToken.getString());
		}

		expression = Expr.parse();
		if (expression==null) {
			Parser.errorHandle("an expression");
		}

		//if the sentence is not yet finished after parsing the conclusion, there is an error
		if (!(Parser.t instanceof EOIToken)) {
			Parser.errorHandle(EOIToken.getString());
		}

    	return new Sequent(antecedents, expression);
  	}

	/**
	 * get list of antecedents
	 * @return list of expressions in antecedents object
	 */
	public List<Expr> getAntecedents() {
		return antecedents.getAntecedents();
	}

	/**
	 * get antecedents as an entire object
	 * @return antecedents object
	 */
	Antecedents getAntecedentsObject(){
		return antecedents;
	}

	/**
	 * get the conclusion of the logical sentence with extraneous brackets removed
	 * @return unbracketed conclusion
	 */
	public Expr getConclusion() {
		return conclusion.unbracket();
	}

	@Override
	public String toString() {
		return antecedents.toString() + " " + turnstile + " " + conclusion.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Sequent && ((Sequent)obj).getAntecedentsObject().equals(this.antecedents) && ((Sequent)obj).getConclusion().equals(this.conclusion);
	}
}
