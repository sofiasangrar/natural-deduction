package parser;

import com.sun.tools.corba.se.idl.constExpr.Not;
import lexer.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.and;
import static natded.NatDedUtilities.not;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class FactorTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    void parseNotTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(not + "(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        Factor n = Factor.parse();
        assertTrue(n instanceof NotExpr);
        assertFalse(Parser.error);
    }

    @Test
    void parseIdentTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        Factor i = Factor.parse();
        assertTrue(i instanceof IdentExpr);
        assertFalse(Parser.error);
    }

    @Test
    void parseBooleanTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        Factor t = Factor.parse();
        assertTrue(t instanceof BooleanExpr);
        assertFalse(Parser.error);
    }

    @Test
    void parseBracketsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        Factor b = Factor.parse();
        assertTrue(b instanceof BracketedExpr);
        assertFalse(Parser.error);
    }

    @Test
    void parseNonFactorTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(and);
        Parser.t = Lexer.lex();
        Factor.parse();
        assertTrue(Parser.error);
    }
}