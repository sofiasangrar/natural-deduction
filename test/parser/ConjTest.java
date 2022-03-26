package parser;

import lexer.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class ConjTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    void parseFactorTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(not + "(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        Conj f = Conj.parse();
        assertTrue(f instanceof Factor);
        assertFalse(Parser.error);
    }

    @Test
    void parseAndTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P " + and + " Q");
        Parser.t = Lexer.lex();
        Conj c = Conj.parse();
        assertTrue(c.left instanceof IdentExpr);
        assertTrue(c.right instanceof IdentExpr);
        assertEquals("P", c.left.toString());
        assertEquals("Q", c.right.toString());
        assertFalse(Parser.error);


    }

    @Test
    void parseNotAndTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(turnstile);
        Parser.t = Lexer.lex();
        Conj.parse();
        assertTrue(Parser.error);
    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ and + "Q");
        Parser.t = Lexer.lex();
        Conj c1 = Conj.parse();

        Lexer.setLexString("P "+ and + "Q");
        Parser.t = Lexer.lex();
        Conj c2 = Conj.parse();

        assertEquals(c1, c2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ and + "Q");
        Parser.t = Lexer.lex();
        Conj c1 = Conj.parse();

        Lexer.setLexString("Q "+ and + "P");
        Parser.t = Lexer.lex();
        Conj c2 = Conj.parse();

        assertNotEquals(c1, c2);
        assertFalse(Parser.error);
    }




}