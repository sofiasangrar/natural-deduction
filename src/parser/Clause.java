package parser;

import lexer.Lexer;
import lexer.tokens.EOIToken;
import lexer.tokens.EmptyToken;
import lexer.tokens.NDToken;

import java.util.List;

public class Clause {

	// stores all of the parsed procedures.
	private Assumptions assumptions;
	private Expr expression;
	private int lineNumber;

	// Constructor
	private Clause(Assumptions assumptions, Expr expression, int lineNumber) {
		this.assumptions = assumptions;
		this.expression = expression;
		this.lineNumber = lineNumber;
	}
	
  public static Clause parse(int line) {
		Assumptions assumptions = new Assumptions();
		Expr expression = new IdentExpr();
	 // checks if the first token received is a PROC token, if not an error message is displayed
	 if(Parser.t instanceof EmptyToken) {
	 	Parser.t= Lexer.lex();

 	} else {
	 	assumptions = Assumptions.parse();
	 }

	  if(Parser.t instanceof NDToken) {
		  Parser.t = Lexer.lex();
		  expression = Expr.parse();
	  } else {
		  Parser.errorHandle(new NDToken(0));
		  Parser.t = Lexer.lex();
	  }

    // checks if the last token is an EOI Token
    if (!(Parser.t instanceof EOIToken)) {
      System.out.println("No End Of Input Token received.");
    }

    return new Clause(assumptions, expression, line);
  }

  public int getLine(){
		return lineNumber;
  }

	public List<Expr> getAssumptions() {
		return assumptions.getAssumptions();
	}

	public String getAssumptionsString(){
		return assumptions.toString();
	}

	public Assumptions getAssumptionsObject(){
		return assumptions;
	}

	public Expr getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return assumptions.toString() + " " + new NDToken(0).toString() + " " + expression.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Clause && ((Clause)obj).getAssumptionsObject().equals(this.assumptions) && ((Clause)obj).getExpression().equals(this.expression);
	}
}
