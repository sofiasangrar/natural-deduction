package parser;

import lexer.Lexer;
import lexer.tokens.FalseToken;
import lexer.tokens.IdentToken;
import lexer.tokens.TrueToken;

public abstract class PrimaryExpr extends Expr {
  public static Expr parse() {
    if (Parser.t instanceof TrueToken || Parser.t instanceof FalseToken) {
      return BooleanExpr.parse();
    } else if (Parser.t instanceof IdentToken) {
      return IdentExpr.parse();
    }else {
      // the reason for not using the error handle method of the parser is that there is no primary expression token to pass through
      System.out.println("Error occurred at character " + Parser.t.getStartingColumnOfToken() + ":");
      System.out.println("Expected a primary expression but received " + Parser.t.getString() + ".");
      Parser.error = true;
      Parser.t = Lexer.lex();
    }

    return null;

  }
}
