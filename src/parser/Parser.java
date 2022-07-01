package parser;
import lexer.tokens.Token;
import natded.exceptions.SyntaxError;

public final class Parser {
  public static Token t;
  public static boolean error = false;
  public static RuntimeException exception = null;

    /**
     * remove errors from parser
     */
  public static void clearError(){
      error = false;
      exception = null;
  }

    /**
     * when the wrong string is found, create a new error
     * @param expected the expected value to be found
     */
  static void errorHandle(String expected) {
        error = true;
        exception = new SyntaxError(expected, t.expectedString(), t.getStartingColumnOfToken());
  }


}
