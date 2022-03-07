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
      if (Parser.t == null) {
        System.out.println("Error occurred at character " + Lexer.tokenColumn + ":");
        System.out.println("Expected a factor.");
        Parser.error = true;
        //Parser.t = Lexer.lex();
      } else {
        System.out.println("Error occurred at character " + Lexer.tokenColumn + ":");
        System.out.println("Expected a factor but received " + Parser.t.expectedString() + ".");
        Parser.error = true;
        //Parser.t = Lexer.lex();
      }
    }

    return new Factor();

  }

}
