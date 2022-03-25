package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.and;
import static natded.NatDedUtilities.not;
import static natded.NatDedUtilities.turnstile;
import static org.junit.jupiter.api.Assertions.*;

class NotExprTest {

    @Test
    void parseTest() {
        String s = "identifier";
        Lexer.setLexString(not + s);
        Parser.t = Lexer.lex();
        NotExpr n = NotExpr.parse();
        assertTrue(n.right instanceof IdentExpr);
        assertEquals(s, n.right.toString());
    }

    @Test
    void equalsTest() {
        Lexer.setLexString(not + "(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        NotExpr n1 = NotExpr.parse();

        Lexer.setLexString(not + "(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        NotExpr n2 = NotExpr.parse();

        assertEquals(n1, n2);
    }

    @Test
    void notEqualsTest() {
        Lexer.setLexString(not + "P");
        Parser.t = Lexer.lex();
        NotExpr i1 = NotExpr.parse();

        Lexer.setLexString(not + "Q");
        Parser.t = Lexer.lex();
        NotExpr i2 = NotExpr.parse();

        assertNotEquals(i1, i2);
    }

    @Test
    void parseNotNotTest() {
        Lexer.setLexString(turnstile);
        Parser.t = Lexer.lex();
        IdentExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}