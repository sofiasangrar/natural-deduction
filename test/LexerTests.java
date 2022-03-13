import lexer.Lexer;
import org.junit.Test;
import parser.*;

import static junit.framework.TestCase.*;
import static natded.NatDedUtilities.*;

public class LexerTests {

    @Test
    public void testWorkingSimple(){
        String s = "P " + turnstile + "P";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Sequent c = Sequent.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testBrackets(){
        String s = "(P "+ and + not + "P) " + turnstile + " F";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Sequent c = Sequent.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testPrecedence(){
        String s = "P " + and + not + "P " + turnstile + " A " + impl + " B " + or + " A " + and + " B";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Sequent c = Sequent.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
        assertEquals(1, c.getAntecedents().size());
        assertTrue(c.getAntecedents().get(0) instanceof Conj);
        assertTrue(((Conj)(c.getAntecedents().get(0))).right instanceof NotExpr);
        assertTrue(c.getConclusion().hasright);
        assertTrue(c.getConclusion().right instanceof Disj);
        assertTrue(((Disj) c.getConclusion().right).right instanceof Conj);

    }

    @Test
    public void unbracketTest(){
        String s = "P " + turnstile + " P";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Sequent c = Sequent.parse();
        s = "P " + turnstile + " (P)";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Sequent c2 = Sequent.parse();
        assertEquals(c.getConclusion(), c.getConclusion());
        assertEquals(c.getConclusion(), c2.getConclusion());
    }
}
