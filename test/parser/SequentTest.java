package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.*;
import static natded.NatDedUtilities.turnstile;
import static org.junit.jupiter.api.Assertions.*;

class SequentTest {

    @Test
    void getAntecedentsTest() {
        Sequent seq = Sequent.parse(modusPonens);
        assertEquals(2, seq.getAntecedents().size());
    }

    @Test
    void getAntecedentsObjectTest() {
        String s1 = "(P), ((Q)) " + turnstile + " P";
        String s2 = "Q, P " + turnstile + " (Q)";
        Sequent seq1 = Sequent.parse(s1);
        Sequent seq2 = Sequent.parse(s2);
        assertEquals(seq1.getAntecedentsObject(), seq2.getAntecedentsObject());
    }

    @Test
    void getConclusionTest() {
        String conc = "A " + impl + " B " + or + " A " + and + " B";
        String s = "P " + and + not + "P " + turnstile + conc;
        Sequent seq = Sequent.parse(s);
        Lexer.setLexString(conc);
        Parser.t = Lexer.lex();
        Expr c = Expr.parse();
        assertEquals(seq.getConclusion(), c);
    }

    @Test
    void toStringTest() {
        String s = "(P), ((Q)) " + turnstile + " P";
        Sequent seq = Sequent.parse(s);
        assertEquals(s, seq.toString());
    }


    @Test
    public void testWorkingSimple(){
        String s = "P " + turnstile + "P";
        Sequent c = Sequent.parse(s);
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testBrackets(){
        String s = "(P "+ and + not + "P) " + turnstile + " F";
        Sequent c = Sequent.parse(s);
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testPrecedence(){
        String s = "P " + and + not + "P " + turnstile + " A " + impl + " B " + or + " A " + and + " B";
        Sequent c = Sequent.parse(s);
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
        Sequent c = Sequent.parse(s);
        s = "(P) " + turnstile + " (((P)))";
        Sequent c2 = Sequent.parse(s);
        assertEquals(c.getConclusion(), c2.getConclusion());
        assertEquals(c, c2);
    }

    @Test
    public void orderAndBracketTest(){
        String s = "Q, P " + turnstile + " (P)";
        Sequent c = Sequent.parse(s);
        s = "(P), ((Q)) " + turnstile + " P";
        Sequent c2 = Sequent.parse(s);
        assertEquals(c.getConclusion(), c2.getConclusion());
        assertEquals(c, c2);
    }
}