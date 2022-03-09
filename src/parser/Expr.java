package parser;

import lexer.Lexer;
import lexer.tokens.*;
import static natded.NatDedUtilities.impl;

public class Expr {

	public Disj left;
	public Expr right;
	public boolean hasright;

	Expr(){

	}

	@Override
	public String toString() {
		return left.toString() + " "+ impl +" " + right.toString();
	}

	Expr(Disj left, Expr right){
		this.left = left;
		this.right = right;
		hasright = true;
	}

	/**
	 * parse he next tokens as an implication expression (or a disjunction if there is no RHS)
	 * @return implication expression
	 */
	  public static Expr parse() {
	  	Disj left = Disj.parse();
	  	if (Parser.t instanceof ImpliesToken) {
			Parser.t = Lexer.lex();
			Expr right = Expr.parse();
			return new Expr(left, right);
		}
		return left;
	  }



	@Override
	public boolean equals(Object obj) {
		Expr compare;
		if (!(obj instanceof Expr)) {
			return false;
		}
		compare = ((Expr)obj).unbracket();

		return left.equals(compare.left) && right.equals(compare.right);
	}

	boolean isDisj(){
		return this.unbracket() instanceof Disj	&& !(this.unbracket() instanceof Conj);
	}

	boolean isConj(){
		return this.unbracket() instanceof Conj	&& !(this.unbracket() instanceof Factor);
	}

	boolean isImpl(){
		return !(this.unbracket() instanceof Disj);
	}

	/**
	 * remove brackets, since parse tree encapsulates precedence once sentences are parsed
	 * @return expression without brackets
	 */
	Expr unbracket(){
		Expr e = this;
		while(e instanceof BracketedExpr) {
			e = ((BracketedExpr) e).expr;
		}
		return e;
	}
}
