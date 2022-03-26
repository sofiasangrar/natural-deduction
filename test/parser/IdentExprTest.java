package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.and;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class IdentExprTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }
    

    @Test
    void parseTest() {
        assertFalse(Parser.error);
        String s = "identifier";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        IdentExpr i = IdentExpr.parse();
        assertEquals(s, i.toString());
        assertFalse(Parser.error);
    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);

        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i1 = IdentExpr.parse();

        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i2 = IdentExpr.parse();

        assertEquals(i1, i2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i1 = IdentExpr.parse();

        Lexer.setLexString("Q");
        Parser.t = Lexer.lex();
        IdentExpr i2 = IdentExpr.parse();

        assertNotEquals(i1, i2);
        assertFalse(Parser.error);
    }

    @Test
    void parseTrueTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        IdentExpr.parse();
        Assertions.assertTrue(Parser.error);
    }

    @Test
    void parseNonAlphabeticTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(and);
        Parser.t = Lexer.lex();
        IdentExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}