package clauseParser;
import lexer.tokens.Token;
import parser.Clause;

public final class ClauseParser {
  public static Clause c;
  public static boolean error = false;

  public static void errorHandle(Token expectedToken) {
      System.out.println("Error occurred at clause " + c.getLine());
      error = true;

  }
  
}
