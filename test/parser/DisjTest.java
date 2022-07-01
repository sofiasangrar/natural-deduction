package parser;

import lexer.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static natded.NatDedUtilities.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class DisjTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }
    
    @Test
    void parseConjunctionTest() {
        assertFalse(Parser.error);
        Lexer.setLexString( "(P "+ or + " Q) " + and  + " R");
        Parser.t = Lexer.lex();
        Disj f = Disj.parse();
        assertTrue(f instanceof Conj);
        assertFalse(Parser.error);
    }

    @Test
    void parseDisjTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P " + or + " Q");
        Parser.t = Lexer.lex();
        Disj d = Disj.parse();
        assertTrue(d.left instanceof IdentExpr);
        assertTrue(d.right instanceof IdentExpr);
        assertEquals("P", d.left.toString());
        assertEquals("Q", d.right.toString());
        assertFalse(Parser.error);

    }

    @Test
    void parseNotOrTest() {
        assertFalse(Parser.error);
        Lexer.setLexString(turnstile);
        Parser.t = Lexer.lex();
        Disj.parse();
        assertTrue(Parser.error);

    }

    @Test
    void equalsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ or + " Q");
        Parser.t = Lexer.lex();
        Disj d1 = Disj.parse();

        Lexer.setLexString("P "+ or + "Q");
        Parser.t = Lexer.lex();
        Disj d2 = Disj.parse();

        assertEquals(d1, d2);
        assertFalse(Parser.error);
    }

    @Test
    void notEqualsTest() {
        assertFalse(Parser.error);
        Lexer.setLexString("P "+ or + "Q");
        Parser.t = Lexer.lex();
        Disj d1 = Disj.parse();

        Lexer.setLexString("Q "+ or + "P");
        Parser.t = Lexer.lex();
        Disj d2 = Disj.parse();
        
        assertNotEquals(d1, d2);
        assertFalse(Parser.error);
    }
    
    
    

}