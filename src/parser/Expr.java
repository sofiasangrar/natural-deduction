package parser;

import lexer.Lexer;
import lexer.tokens.*;

public class Expr {

	public Disj left;
	public Expr right;
	public boolean hasright;

	Expr(){

	}

	@Override
	public String toString() {
		return left.toString() + " => " + right.toString();
	}

	Expr(Disj left, Expr right){
		this.left = left;
		this.right = right;
		hasright = true;
	}

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

	public boolean isDisj(){
		return this.unbracket() instanceof Disj	&& !(this.unbracket() instanceof Conj);
	}

	public boolean isConj(){
		return this.unbracket() instanceof Conj	&& !(this.unbracket() instanceof Factor);
	}

	public boolean isImpl(){
		return !(this.unbracket() instanceof Disj);
	}

	public Expr unbracket(){
		Expr e = this;
		while(e instanceof BracketedExpr) {
			e = ((BracketedExpr) e).expr;
		}
		return e;
	}
}
