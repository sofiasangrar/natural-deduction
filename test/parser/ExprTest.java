package parser;

import lexer.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class ExprTest {
    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }
    

    @Test
    void parseDisjunctionTest() {
        assertFalse(Parser.error);
        Lexer.setLexString( "(P "+ impl + " Q) " + or  + " R");
        Parser.t = Lexer.lex();
        Expr f = Expr.parse();
        assertTrue(f instanceof Disj);
        assertFalse(Parser.error);
    }

    @Test
    void parseExprTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P " + impl + " Q");
        Parser.t = Lexer.lex();
        Expr d = Expr.parse();
        assertTrue(d.left instanceof IdentExpr);
        assertTrue(d.right instanceof IdentExpr);
        assertEquals("P", d.left.toString());
        assertEquals("Q", d.right.toString());
        assertFalse(Parser.error);

    }

    @Test
    void parseNotExprTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(turnstile);
        Parser.t = Lexer.lex();
        Expr.parse();
        assertTrue(Parser.error);

    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ impl + " Q");
        Parser.t = Lexer.lex();
        Expr e1 = Expr.parse();

        Lexer.setLexString("P "+ impl + "Q");
        Parser.t = Lexer.lex();
        Expr e2 = Expr.parse();

        assertEquals(e1, e2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ impl + "Q");
        Parser.t = Lexer.lex();
        Expr e1 = Expr.parse();

        Lexer.setLexString("Q "+ impl + "P");
        Parser.t = Lexer.lex();
        Expr e2 = Expr.parse();

        assertNotEquals(e1, e2);
        assertFalse(Parser.error);
    }

    @Test
    void isDisjTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ or + "Q");
        Parser.t = Lexer.lex();
        Expr e = Expr.parse();
        assertFalse(Parser.error);
        assertTrue(e.isDisj());
        assertFalse(e.isConj());
        assertFalse(e.isImpl());
    }

    @Test
    void isConjTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ and + "Q");
        Parser.t = Lexer.lex();
        Expr e = Expr.parse();
        assertFalse(Parser.error);
        assertFalse(e.isDisj());
        assertTrue(e.isConj());
        assertFalse(e.isImpl());
    }

    @Test
    void isImpl() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ impl + "Q");
        Parser.t = Lexer.lex();
        Expr e = Expr.parse();
        assertFalse(Parser.error);
        assertFalse(e.isDisj());
        assertFalse(e.isConj());
        assertTrue(e.isImpl());
    }

    @Test
    void unbracket() {
        assertFalse(Parser.error);
        String s = "(P) "+ impl + "(Q)";
        Lexer.setLexString("((("+s+")))");
        Parser.t = Lexer.lex();
        Expr e1 = Expr.parse();
        assertFalse(Parser.error);

        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Expr e2 = Expr.parse();

        assertFalse(Parser.error);
        Expr unbracketed = e1.unbracket();

        assertEquals(e2, unbracketed);
    }
}