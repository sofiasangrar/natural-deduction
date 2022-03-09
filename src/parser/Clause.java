package parser;

import lexer.Lexer;
import lexer.tokens.EOIToken;
import lexer.tokens.EmptyToken;
import lexer.tokens.NDToken;

import java.util.List;

import static natded.NatDedUtilities.nd;

public class Clause {

	private Assumptions assumptions;
	private Expr expression;

	private Clause(Assumptions assumptions, Expr expression) {
		this.assumptions = assumptions;
		this.expression = expression;
	}

	/**
	 * parse the next tokens as a valid sentence in natural deduction, i.e. <assumptions> <turnstile> <conclusion>
	 * @return sentence of natural deduction
	 */
  	public static Clause parse() {
  		Assumptions assumptions = new Assumptions();
		Expr expression = new Expr();

		//parse assumption, if there are any
		if(Parser.t instanceof EmptyToken) {
			Parser.t= Lexer.lex();
		} else {
			assumptions = Assumptions.parse();
		}

		//assumptions musy be followed by turnstile
		if(Parser.t instanceof NDToken) {
			Parser.t = Lexer.lex();
			expression = Expr.parse();
		} else {
			Parser.errorHandle();
		}

		//if the sentence is not yet finished after parsing the conclusion, there is an error
		if (!(Parser.t instanceof EOIToken)) {
			Parser.errorHandle();
		}

    	return new Clause(assumptions, expression);
  	}

	/**
	 * get list of assumptions
	 * @return list of expressions in assumptions object
	 */
	public List<Expr> getAssumptions() {
		return assumptions.getAssumptions();
	}

	/**
	 * get assumptions as an entire object
	 * @return assumptions object
	 */
	Assumptions getAssumptionsObject(){
		return assumptions;
	}

	/**
	 * get the conclusion of the logical sentence with extraneous brackets removed
	 * @return unbracketed conclusion
	 */
	public Expr getExpression() {
		return expression.unbracket();
	}

	@Override
	public String toString() {
		return assumptions.toString() + " " + nd + " " + expression.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Clause && ((Clause)obj).getAssumptionsObject().equals(this.assumptions) && ((Clause)obj).getExpression().equals(this.expression);
	}
}
