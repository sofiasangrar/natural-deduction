package natded;

import natded.constants.Step;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.Sequent;

import java.util.ArrayList;

import static natded.NatDedUtilities.*;
import static natded.NatDedUtilities.not;
import static natded.NatDedUtilities.or;
import static natded.constants.Step.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class StepNodeTest {

    @Test
    void incorrectSyntaxTest() {
        StepNode s = new StepNode("P " + and + "Q", Step. ASSUMPTION);
        assertFalse(s.hasIncorrectSyntax());
        s.setIncorrectSyntax(true);
        assertTrue(s.hasIncorrectSyntax());
    }

    @Test
    void getInput() {
        String string = "P " + and + "Q" + turnstile + "P";
        StepNode s = new StepNode(string, Step. AND_ELIM);
        assertEquals(string, s.getInput());
    }

    @Test
    void parseTest() {
        assertFalse(Parser.error);
        String string = "P " + and + "Q" + turnstile + "P";
        StepNode s = new StepNode(string, Step. AND_ELIM);
        s.parse();
        Sequent seq = Sequent.parse(string);
        assertEquals(seq, s.getParsedInput());
        assertFalse(Parser.error);
    }

    @Test
    void incorrectParseTest() {
        assertFalse(Parser.error);
        String string = "P " + and + "Q";
        StepNode s = new StepNode(string, Step. AND_ELIM);
        s.parse();
        assertTrue(Parser.error);
    }

    @Test
    void getChildren() {

        String s1 = not + "(P " + or + " Q) " + turnstile + not + "P";
        String s2 = not + "(P " + or + " Q) " + turnstile + not + "Q";
        StepNode l21 = new StepNode(s1, NEG_INTRO);
        StepNode l22 = new StepNode(s2, NEG_INTRO);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);

        StepNode l1 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "P" + and + not + "Q",
                AND_INTRO);
        l1.addChildren(l2);

        assertEquals(2, l1.getChildren().size());

    }

    @Test
    void getPremisses() {
        String s1 = not + "(P " + or + " Q) " + turnstile + not + "P";
        String s2 = not + "(P " + or + " Q) " + turnstile + not + "Q";
        StepNode l21 = new StepNode(s1, NEG_INTRO);
        StepNode l22 = new StepNode(s2, NEG_INTRO);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l21.parse();
        l22.parse();

        StepNode l1 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "P" + and + not + "Q",
                AND_INTRO);
        l1.addChildren(l2);

        assertEquals(2, l1.getPremisses().size());
        assertEquals(Sequent.parse(s1), l1.getPremisses().get(0));
        assertEquals(Sequent.parse(s2), l1.getPremisses().get(1));
    }

    @Test
    void addChild() {
        String s1 = not + "(P " + or + " Q) " + turnstile + not + "P";
        String s2 = not + "(P " + or + " Q) " + turnstile + not + "Q";
        StepNode l21 = new StepNode(s1, NEG_INTRO);
        StepNode l22 = new StepNode(s2, NEG_INTRO);
        l21.parse();
        l22.parse();

        StepNode l1 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "P" + and + not + "Q",
                AND_INTRO);
        l1.addChild(l21);
        l1.addChild(l22);

        assertEquals(2, l1.getPremisses().size());
        assertEquals(Sequent.parse(s1), l1.getPremisses().get(0));
        assertEquals(Sequent.parse(s2), l1.getPremisses().get(1));

    }

    @Test
    void getStepAssigned() {
        String string = "P " + and + "Q" + turnstile + "P";
        Step step = AND_ELIM;
        StepNode s = new StepNode(string, step);
        assertEquals(step, s.getStep());
    }

    @Test
    void getStepUnassigned() {
        String string = "P " + and + "Q" + turnstile + "P";
        Step step = UNASSIGNED;
        StepNode s = new StepNode(string, step);
        assertEquals(step, s.getStep());
    }

}