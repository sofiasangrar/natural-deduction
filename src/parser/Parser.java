package parser;
import lexer.tokens.Token;

public final class Parser {
  public static Token t;
  public static boolean error = false;

  static void errorHandle() {
      error = true;
  }
  
}
