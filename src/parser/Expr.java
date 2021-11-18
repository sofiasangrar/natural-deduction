package parser;

import lexer.Lexer;
import lexer.tokens.LParenToken;
import lexer.tokens.NotToken;
import lexer.tokens.OperatorToken;
import lexer.tokens.RParenToken;

public abstract class Expr {

	  public static Expr parse() {
//	  		if (Parser.t instanceof LParenToken) {
//	  			Parser.t = Lexer.lex();
//	  			Expr expr = Expr.parse();
//	  			if (!(Parser.t instanceof RParenToken)){
//	  				Parser.errorHandle(new RParenToken(0));
//				}
//	  			Parser.t = Lexer.lex();
//	  			return expr;
//			} else
//				if (Parser.t instanceof NotToken) {
//	  			return NotExpr.parse();
//			}
		    return fraserHanson(1);
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
				  left = PrimaryExpr.parse();
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
				  left = PrimaryExpr.parse();
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
