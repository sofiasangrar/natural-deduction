package lexer;

import lexer.tokens.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.*;
import static natded.NatDedUtilities.turnstile;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LexerTest {

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
    }

    @Test
    void setLexString() {
        String s = "testString";
        Lexer.setLexString(s);
        assertEquals(0,Lexer.tokenColumn);
    }

    @Test
    void andEquals() {
        AndToken a1 = new AndToken();
        AndToken a2 = new AndToken();
        assertEquals(a1, a2);
    }

    @Test
    void trueEquals() {
        TrueToken a1 = new TrueToken();
        TrueToken a2 = new TrueToken();
        assertEquals(a1, a2);
    }


    @Test
    void falseEquals() {
        FalseToken a1 = new FalseToken();
        FalseToken a2 = new FalseToken();
        assertEquals(a1, a2);
    }

    @Test
    void commaEquals() {
        CommaToken a1 = new CommaToken();
        CommaToken a2 = new CommaToken();
        assertEquals(a1, a2);
    }


    @Test
    void emptyEquals() {
        EmptyToken a1 = new EmptyToken();
        EmptyToken a2 = new EmptyToken();
        assertEquals(a1, a2);
    }

    @Test
    void EOIEquals() {
        EOIToken a1 = new EOIToken();
        EOIToken a2 = new EOIToken();
        assertEquals(a1, a2);
    }

    @Test
    void IdentEquals() {
        IdentToken a1 = new IdentToken("P");
        IdentToken a2 = new IdentToken("P");
        assertEquals(a1, a2);
    }

    @Test
    void identNotEquals() {
        IdentToken a1 = new IdentToken("p");
        IdentToken a2 = new IdentToken("P");
        assertNotEquals(a1, a2);
    }

    @Test
    void implEquals() {
        ImpliesToken a1 = new ImpliesToken();
        ImpliesToken a2 = new ImpliesToken();
        assertEquals(a1, a2);
    }


    @Test
    void lParenEquals() {
        LParenToken a1 = new LParenToken();
        LParenToken a2 = new LParenToken();
        assertEquals(a1, a2);
    }


    @Test
    void rParenEquals() {
        RParenToken a1 = new RParenToken();
        RParenToken a2 = new RParenToken();
        assertEquals(a1, a2);
    }


    @Test
    void notEquals() {
        NotToken a1 = new NotToken();
        NotToken a2 = new NotToken();
        assertEquals(a1, a2);
    }


    @Test
    void orEquals() {
        OrToken a1 = new OrToken();
        OrToken a2 = new OrToken();
        assertEquals(a1, a2);
    }


    @Test
    void turnstileEquals() {
        TurnstileToken a1 = new TurnstileToken();
        TurnstileToken a2 = new TurnstileToken();
        assertEquals(a1, a2);
    }


    @Test
    void lexAnd() {
        String s = and;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof AndToken);
    }

    @Test
    void lexTrue() {
        String s = "T";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof TrueToken);
    }

    @Test
    void lexFalse() {
        String s = "F";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof FalseToken);
    }

    @Test
    void lexComma() {
        String s = ",";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof CommaToken);
    }


    @Test
    void lexEmpty() {
        String s = empty;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof EmptyToken);
    }

    @Test
    void lexEoi() {
        String s ="";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof EOIToken);
    }

    @Test
    void lexIdentLong() {
        String s = "Premise";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentShortCapital() {
        String s = "P";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentShortLowerCase() {
        String s = "x";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentLongCapital() {
        String s = "XYZ";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentReverseCapital() {
        String s = "xX";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentContainsTrue() {
        String s = "tT";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexIdentStartsWithTrue() {
        String s = "Tt";
        Lexer.setLexString(s);

        Token t = Lexer.lex();
        assertTrue(t instanceof TrueToken);

        t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals("t", t.toString());
    }

    @Test
    void lexIllegalString() {
        String s = "%";
        Lexer.setLexString(s);
        Token t = Lexer.lex();
        assertTrue(t instanceof IllegalStringToken);
        assertEquals(s, t.toString());
    }

    @Test
    void lexImplies() {
        String s = impl;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof ImpliesToken);
    }

    @Test
    void lexLParen() {
        String s = "(";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof LParenToken);
    }


    @Test
    void lexNot() {
        String s = not;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof NotToken);
    }

    @Test
    void lexOr() {
        String s = or;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof OrToken);
    }

    @Test
    void lexRParen() {
        String s = ")";
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof RParenToken);
    }

    @Test
    void lexTurnstile() {
        String s = turnstile;
        Lexer.setLexString(s);
        assertTrue(Lexer.lex() instanceof TurnstileToken);
    }

    @Test
    void testFromReportSimple() {
        String s = "P "+ turnstile + " P";
        Lexer.setLexString(s);
        Token t = Lexer.lex();

        assertTrue(t instanceof IdentToken);
        assertEquals("P", t.toString());
        assertEquals(1, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof TurnstileToken);
        assertEquals(3, t.getStartingColumnOfToken());


        t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals("P", t.toString());
        assertEquals(5, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof EOIToken);
    }

    @Test
    void testFromReportRandomTokens() {
        String s = impl + not + and + "P";
        Lexer.setLexString(s);
        Token t = Lexer.lex();

        assertTrue(t instanceof ImpliesToken);
        assertEquals(1, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof NotToken);
        assertEquals(2, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof AndToken);
        assertEquals(3, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals("P", t.toString());
        assertEquals(4, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof EOIToken);
    }

    @Test
    void testFromReportIllegal() {
        String s = "A" + and + "$%ab" + turnstile + "Q";
        Lexer.setLexString(s);
        Token t = Lexer.lex();

        assertTrue(t instanceof IdentToken);
        assertEquals("A", t.toString());
        assertEquals(1, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof AndToken);
        assertEquals(2, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof IllegalStringToken);
        assertEquals("$%ab", t.toString());
        assertEquals(3, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof TurnstileToken);
        assertEquals(7, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof IdentToken);
        assertEquals("Q", t.toString());
        assertEquals(8, t.getStartingColumnOfToken());

        t = Lexer.lex();
        assertTrue(t instanceof EOIToken);
    }


}