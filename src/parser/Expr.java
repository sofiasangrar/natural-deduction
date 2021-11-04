package parser;

import lexer.Lexer;
import lexer.tokens.LParenToken;
import lexer.tokens.NotToken;
import lexer.tokens.OperatorToken;
import lexer.tokens.RParenToken;

public abstract class Expr {

	  public static Expr parse() {
	  		if (Parser.t instanceof LParenToken) {
	  			Parser.t = Lexer.lex();
	  			Expr expr = Expr.parse();
	  			if (!(Parser.t instanceof RParenToken)){
	  				Parser.errorHandle(new RParenToken(0));
				}
	  			Parser.t = Lexer.lex();
	  			return expr;
			} else if (Parser.t instanceof NotToken) {
	  			Parser.t = Lexer.lex();
	  			return new NotExpr(Expr.parse());
			}
		    return fraserHanson(1);
	  }

	  //Binary Expressions precedence handler from Fraser and Hanson C Compiler book
	  private static Expr fraserHanson(int k) {
		  int   i;
		  Expr left;
		  OperatorToken oper;
		  Expr right;

		  left = PrimaryExpr.parse();

		  for (i = Parser.t.precedence(); i >= k; i--) {
		    while (Parser.t.precedence() == i) {
		      oper = (OperatorToken) Parser.t;

		      Parser.t = Lexer.lex();
		      right = fraserHanson(i+1);
		      left = new BinaryExpr(left, oper, right);
		    }
		  }
		  return left;
	  }
}
