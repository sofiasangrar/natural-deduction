package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BooleanExprTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    void parseFalseTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f = BooleanExpr.parse();
        assertFalse(f.value);
        assertFalse(Parser.error);

    }

    @Test
    void parseTrueTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        BooleanExpr t = BooleanExpr.parse();
        assertTrue(t.value);
        assertFalse(Parser.error);

    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f1 = BooleanExpr.parse();

        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f2 = BooleanExpr.parse();

        assertEquals(f1, f2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("F");
        Parser.t = Lexer.lex();
        BooleanExpr f = BooleanExpr.parse();

        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        BooleanExpr t = BooleanExpr.parse();

        assertNotEquals(f, t);
        assertFalse(Parser.error);
    }

    @Test
    void parseLowerCaseTrueTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("t");
        Parser.t = Lexer.lex();
        BooleanExpr.parse();
        Assertions.assertTrue(Parser.error);
    }

    @Test
    void parseLowerCaseFalseTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("f");
        Parser.t = Lexer.lex();
        BooleanExpr.parse();
        Assertions.assertTrue(Parser.error);
    }
}