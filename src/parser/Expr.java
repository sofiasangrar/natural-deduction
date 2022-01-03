package parser;

import lexer.Lexer;
import lexer.tokens.*;

public class Expr {

	public Disj left;
	public Expr right;
	public boolean hasright;

	Expr(Disj left){
		this.left = left;
		this.right = null;
		hasright = false;
	}

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

	  //Binary Expressions precedence handler from Fraser and Hanson C Compiler book
	  private static Expr fraserHanson(int k) {
	  	boolean end = false;
		  int   i;
		  Expr left;
		  OperatorToken oper;
		  Expr right;

		  if (Parser.t instanceof LParenToken) {
			  Parser.t = Lexer.lex();
			  if (Parser.t instanceof NotToken) {
				  left = NotExpr.parse();
			  } else {
				  left = Factor.parse();
			  }
			  if (!(Parser.t instanceof RParenToken)){
				  end = true;
			  } else {
				  Parser.t = Lexer.lex();
			  }
		  } else {
			  if (Parser.t instanceof NotToken) {
				  left = NotExpr.parse();
			  } else {
				  left = Factor.parse();
			  }
		  }



		  for (i = Parser.t.precedence(); i >= k; i--) {
		    while (Parser.t.precedence() == i) {
		      oper = (OperatorToken) Parser.t;

		      Parser.t = Lexer.lex();
		      right = fraserHanson(i+1);
		      left = new BinaryExpr(left, oper, right);
		    }
		  }
		  if (end) {
			  if (!(Parser.t instanceof RParenToken)) {
				  Parser.errorHandle(new RParenToken(0));
			  }
			  Parser.t = Lexer.lex();
		  }

		  return left;
	  }

	@Override
	public boolean equals(Object obj) {
		return obj.toString().equals(this.toString());
	}
}
