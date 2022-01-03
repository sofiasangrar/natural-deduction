package parser;

import lexer.Lexer;
import lexer.tokens.*;

public class Factor extends Conj {

  public static Factor parse() {
    if (Parser.t instanceof TrueToken || Parser.t instanceof FalseToken) {
      return BooleanExpr.parse();
    } else if (Parser.t instanceof IdentToken) {
      return IdentExpr.parse();
    } else if(Parser.t instanceof NotToken) {
      return NotExpr.parse();
    } else if (Parser.t instanceof LParenToken) {
      return BracketedExpr.parse();
    } else {
      // the reason for not using the error handle method of the parser is that there is no factor to pass through
      System.out.println("Error occurred at character " + Parser.t.getStartingColumnOfToken() + ":");
      System.out.println("Expected a factor but received " + Parser.t.getString() + ".");
      Parser.error = true;
      Parser.t = Lexer.lex();
    }

    return null;

  }


}
