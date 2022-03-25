package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.and;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BooleanExprTest {

    @Test
    void parseFalseTest() {
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f = BooleanExpr.parse();
        assertFalse(f.value);

    }

    @Test
    void parseTrueTest() {
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        BooleanExpr t = BooleanExpr.parse();
        assertTrue(t.value);

    }

    @Test
    void equalsTest() {
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f1 = BooleanExpr.parse();

        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f2 = BooleanExpr.parse();

        assertEquals(f1, f2);
    }

    @Test
    void notEqualsTest() {
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f = BooleanExpr.parse();

        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        BooleanExpr t = BooleanExpr.parse();

        assertNotEquals(f, t);
    }

    @Test
    void parseLowerCaseTrueTest() {
        Lexer.setLexString("t");
        Parser.t = Lexer.lex();
        BooleanExpr.parse();
        Assertions.assertTrue(Parser.error);
    }

    @Test
    void parseLowerCaseFalseTest() {
        Lexer.setLexString("f");
        Parser.t = Lexer.lex();
        BooleanExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}