package parser;
import lexer.tokens.Token;
import natded.exceptions.SyntaxError;

public final class Parser {
  public static Token t;
  public static boolean error = false;
  public static RuntimeException exception = null;

  public static void clearError(){
      error = false;
      exception = null;
  }

  static void errorHandle() {
      error = true;
      exception = new SyntaxError();
  }

    static void errorHandle(String expected) {
        error = true;
        exception = new SyntaxError(expected, t.expectedString(), t.getStartingColumnOfToken());
    }

  static void errorHandle(String expected, String actual, int charNo) {
        error = true;
        exception = new SyntaxError(expected, actual, charNo);
    }


}
