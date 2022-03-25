package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.and;
import static org.junit.jupiter.api.Assertions.*;

class IdentExprTest {
    

    @Test
    void parseTest() {
        String s = "identifier";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        IdentExpr i = IdentExpr.parse();
        assertEquals(s, i.toString());
    }

    @Test
    void equalsTest() {
        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i1 = IdentExpr.parse();

        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i2 = IdentExpr.parse();

        assertEquals(i1, i2);
    }

    @Test
    void notEqualsTest() {
        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        IdentExpr i1 = IdentExpr.parse();

        Lexer.setLexString("Q");
        Parser.t = Lexer.lex();
        IdentExpr i2 = IdentExpr.parse();

        assertNotEquals(i1, i2);
    }

    @Test
    void parseTrueTest() {
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        IdentExpr.parse();
        Assertions.assertTrue(Parser.error);
    }

    @Test
    void parseNonAlphabeticTest() {
        Lexer.setLexString(and);
        Parser.t = Lexer.lex();
        IdentExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}