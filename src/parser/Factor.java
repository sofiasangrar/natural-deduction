package parser;

import lexer.tokens.*;

public class Factor extends Conj {

  /**
   * parse a factor, which is a boolean, an identifier,  abracketed expression or a negated expression
   * @return parsed factor
   */
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
      Parser.error = true;
    }
    return new Factor();

  }

}
