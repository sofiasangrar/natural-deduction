package parser;
import lexer.tokens.Token;

public final class Parser {
  public static Token t;
  public static boolean error = false;

  public static void errorHandle(Token expectedToken) {
      System.out.println("Error occurred at character " + t.getStartingColumnOfToken() + ":");
      System.out.println("Expected " + expectedToken.getString() + " but received " + t.getString() + ".");
      error = true;

  }
  
}
