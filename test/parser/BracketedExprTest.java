package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class BracketedExprTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    void parseTest() {
        assertFalse(Parser.error);
        String s = "P " + or + " Q";
        Lexer.setLexString("(" + s + ")");
        Parser.t = Lexer.lex();
        BracketedExpr b = BracketedExpr.parse();
        assertTrue(b.expr instanceof Disj);
        assertEquals(s, b.expr.toString());
        assertFalse(Parser.error);
    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        BracketedExpr b1 = BracketedExpr.parse();

        Lexer.setLexString("(((P "+ and + "Q)))");
        Parser.t = Lexer.lex();
        BracketedExpr b2 = BracketedExpr.parse();

        assertEquals(b1, b2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        BracketedExpr b1 = BracketedExpr.parse();

        Lexer.setLexString("(Q "+ and + "P)");
        Parser.t = Lexer.lex();
        BracketedExpr b2 = BracketedExpr.parse();

        assertNotEquals(b1, b2);
        assertFalse(Parser.error);
    }

    @Test
    void parseNotBracketedExprTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P " +and + " Q");
        Parser.t = Lexer.lex();
        BracketedExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}