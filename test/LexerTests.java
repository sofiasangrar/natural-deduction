import lexer.Lexer;
import org.junit.Test;
import parser.*;

import static junit.framework.TestCase.*;

public class LexerTests {

    @Test
    public void testWorkingSimple(){
        String s = "P |- P";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testBrackets(){
        String s = "(P ^ !P) |- F";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
    }

    @Test
    public void testPrecedence(){
        String s = "P ^ !P |- A => B V A ^ B";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse();
        assertNotNull(c);
        assertFalse(Parser.error);
        assertEquals(1, c.getAssumptions().size());
        assertTrue(c.getAssumptions().get(0) instanceof Conj);
        assertTrue(((Conj)(c.getAssumptions().get(0))).right instanceof NotExpr);
        assertTrue(c.getExpression().hasright);
        assertTrue(c.getExpression().right instanceof Disj);
        assertTrue(((Disj) c.getExpression().right).right instanceof Conj);

    }

    @Test
    public void unbracketTest(){
        String s = "P |- P";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse();
        s = "P |- (P)";
        Lexer.setLexString(s);
        Parser.t = Lexer.lex();
        Clause c2 = Clause.parse();
        assertEquals(c.getExpression(), c.getExpression());
        assertEquals(c.getExpression(), c2.getExpression());
    }
}
