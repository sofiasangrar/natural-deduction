package parser;

import com.sun.tools.corba.se.idl.constExpr.Not;
import lexer.Lexer;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.and;
import static natded.NatDedUtilities.not;
import static org.junit.jupiter.api.Assertions.*;

class FactorTest {

    @Test
    void parseNotTest() {
        Lexer.setLexString(not + "(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        Factor n = Factor.parse();
        assertTrue(n instanceof NotExpr);
    }

    @Test
    void parseIdentTest() {
        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        Factor i = Factor.parse();
        assertTrue(i instanceof IdentExpr);
    }

    @Test
    void parseBooleanTest() {
        Lexer.setLexString("T");
        Parser.t = Lexer.lex();
        Factor t = Factor.parse();
        assertTrue(t instanceof BooleanExpr);
    }

    @Test
    void parseBracketsTest() {
        Lexer.setLexString("(P "+ and + "Q)");
        Parser.t = Lexer.lex();
        Factor b = Factor.parse();
        assertTrue(b instanceof BracketedExpr);
    }

    @Test
    void parseNonFactorTest() {
        Lexer.setLexString(and);
        Parser.t = Lexer.lex();
        Factor.parse();
        assertTrue(Parser.error);
    }
}