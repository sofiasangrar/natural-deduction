package parser;

import lexer.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static natded.NatDedUtilities.and;
import static natded.NatDedUtilities.not;
import static natded.NatDedUtilities.turnstile;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class AntecedentsTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    void parseTest() {
        assertFalse(Parser.error);
        String s1 = "P, " + not + "(P " + and + "Q), R";
        Lexer.setLexString(s1);
        Parser.t = Lexer.lex();
        Antecedents a = Antecedents.parse();
        assertEquals(3, a.getAntecedents().size());

        Lexer.setLexString("P");
        Parser.t = Lexer.lex();
        Expr e1 = Expr.parse();

        Lexer.setLexString(not + "(P " + and + "Q)");
        Parser.t = Lexer.lex();
        Expr e2 = Expr.parse();

        Lexer.setLexString("R");
        Parser.t = Lexer.lex();
        Expr e3 = Expr.parse();

        assertEquals(e1, a.getAntecedents().get(0));
        assertEquals(e2, a.getAntecedents().get(1));
        assertEquals(e3, a.getAntecedents().get(2));
        assertFalse(Parser.error);
    }


    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        String s1 = "(P), R, ((Q)) ";
        String s2 = "Q, P, (R) ";
        Lexer.setLexString(s1);
        Parser.t = Lexer.lex();
        Antecedents a1 = Antecedents.parse();

        Lexer.setLexString(s2);
        Parser.t = Lexer.lex();
        Antecedents a2 = Antecedents.parse();

        assertEquals(a1, a2);
        assertFalse(Parser.error);
    }

    @Test
    void notEquals() {
        assertFalse(Parser.error);
        String s1 = "(P), R ";
        String s2 = "Q, P, (R) ";
        Lexer.setLexString(s1);
        Parser.t = Lexer.lex();
        Antecedents a1 = Antecedents.parse();

        Lexer.setLexString(s2);
        Parser.t = Lexer.lex();
        Antecedents a2 = Antecedents.parse();

        assertNotEquals(a1, a2);
        assertFalse(Parser.error);
    }
}