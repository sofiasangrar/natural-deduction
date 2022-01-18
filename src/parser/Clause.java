package parser;

import lexer.Lexer;
import lexer.tokens.EOIToken;
import lexer.tokens.EmptyToken;
import lexer.tokens.NDToken;

import java.util.List;

public class Clause {

	private Assumptions assumptions;
	private Expr expression;

	private Clause(Assumptions assumptions, Expr expression) {
		this.assumptions = assumptions;
		this.expression = expression;
	}
	
  public static Clause parse() {
		Assumptions assumptions = new Assumptions();
		Expr expression = new Expr();

	 if(Parser.t instanceof EmptyToken) {
	 	Parser.t= Lexer.lex();
	 } else {
	 	assumptions = Assumptions.parse();
	 }

	  if(Parser.t instanceof NDToken) {
		  Parser.t = Lexer.lex();
		  expression = Expr.parse();
	  } else {
		  Parser.errorHandle(new NDToken());
		  //Parser.t = Lexer.lex();
	  }

    if (!(Parser.t instanceof EOIToken)) {
		Parser.errorHandle(new EOIToken());
    }

    return new Clause(assumptions, expression);
  }

	public List<Expr> getAssumptions() {
		return assumptions.getAssumptions();
	}

	public Assumptions getAssumptionsObject(){
		return assumptions;
	}

	public Expr getExpression() {
		return expression.unbracket();
	}

	@Override
	public String toString() {
		return assumptions.toString() + " " + NDToken.getString() + " " + expression.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Clause && ((Clause)obj).getAssumptionsObject().equals(this.assumptions) && ((Clause)obj).getExpression().equals(this.expression);
	}
}
