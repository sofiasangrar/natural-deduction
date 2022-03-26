import lexer.Lexer;
import natded.exceptions.AntecedentsMismatchException;
import org.junit.jupiter.api.BeforeEach;
import parser.Parser;
import parser.Proof;
import natded.StepNode;
import org.junit.Test;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.*;
import static natded.constants.Step.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProofStepTests {

    StepNode root;

    @BeforeEach
    void setUp() {
        Lexer.setLexString("");
        Parser.clearError();
    }

    @Test
    public void testDeMorganProof(){
        StepNode l4 = new StepNode(
                not + "(P " + or + " Q), P " + turnstile + " P",
                ASSUMPTION);

        ArrayList<StepNode> l3 = new ArrayList<>();

        StepNode l31 = new StepNode(
                not + "(P " + or + " Q), P " + turnstile + " P " + or + " Q",
                OR_INTRO);

        l31.addChild(l4);
        l3.add(l31);
        l3.add(new StepNode(
                not + "(P " + or + " Q), P " + turnstile + not + "(P " + or + " Q)",
                ASSUMPTION));

        StepNode l6 = new StepNode(
                not + "(P " + or + " Q), Q " + turnstile + " Q",
                ASSUMPTION);

        ArrayList<StepNode> l5 = new ArrayList<>();
        StepNode l51 = new StepNode(
                not + "(P " + or + " Q), Q " + turnstile + "P " + or + " Q", OR_INTRO);
        l51.addChild(l6);
        l5.add(l51);
        l5.add(new StepNode(
                not + "(P " + or + " Q), Q " + turnstile + not + "(P " + or + " Q)",
                ASSUMPTION));

        StepNode l21 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "P",
                NEG_INTRO);
        StepNode l22 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "Q",
                NEG_INTRO);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l21.addChildren(l3);
        l22.addChildren(l5);

        StepNode l1 = new StepNode(
                not + "(P " + or + " Q) " + turnstile + not + "P" + and + not + "Q",
                AND_INTRO);
        l1.addChildren(l2);

        root = new StepNode(
                deMorgan,
                IMP_INTRO);
        root.addChild(l1);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void testModusPonensProof(){

        StepNode l21 = new StepNode(
                "P " + impl + " Q, P, "+ not + "Q" + turnstile + "P" + impl + "Q",
                ASSUMPTION);
        StepNode l22 = new StepNode(
                "P " + impl + " Q, P, "+ not + "Q" + turnstile + "P",
                ASSUMPTION);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);

        StepNode l1 = new StepNode(
                "P " + impl + " Q, P, "+ not + "Q" + turnstile +  "Q",
                IMP_ELIM);
        l1.addChildren(l2);

        StepNode l12 = new StepNode(
                "P " + impl + " Q, P, "+ not + "Q" + turnstile + not + "Q",
                ASSUMPTION);

        root = new StepNode(
                modusPonens,
                NEG_INTRO);
        root.addChild(l1);
        root.addChild(l12);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void testNotPOrQProof(){
        StepNode l31 = new StepNode(not + "P " + or + " Q, P, " + not + "P" + turnstile +  "P", ASSUMPTION);
        StepNode l32 = new StepNode(not + "P " + or + " Q, P, " + not + "P" + turnstile + not +  "P", ASSUMPTION);

        StepNode l21 = new StepNode(
                not + "P " + or + " Q, P" + turnstile +  not + "P" + or + "Q",
                ASSUMPTION);
        StepNode l22 = new StepNode(
                not + "P " + or + " Q, P, " + not + "P" + turnstile +  "Q",
                NEG_ELIM);
        l22.addChild(l31);
        l22.addChild(l32);
        StepNode l23 = new StepNode(
                not + "P " + or + " Q, P, Q" + turnstile + "Q",
                ASSUMPTION);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l2.add(l23);

        StepNode l1 = new StepNode(
                not + "P " + or + " Q, P" + turnstile +  "Q",
                OR_ELIM);
        l1.addChildren(l2);

        root = new StepNode(
                notPorQ,
                IMP_INTRO);
        root.addChild(l1);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void testFalseIsTrueProof(){

        StepNode l21 = new StepNode(
                not + "P, P" + turnstile + "P",
                ASSUMPTION);
        StepNode l22 = new StepNode(
                not + "P, P" + turnstile + not + "P",
                ASSUMPTION);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);

        StepNode l1 = new StepNode(
                not + "P, P" + turnstile +  "Q",
                NEG_ELIM);
        l1.addChildren(l2);

        root = new StepNode(
                falseIsTrue,
                IMP_INTRO);
        root.addChild(l1);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }
    @Test
    public void testHalfOfOrProof(){
        StepNode l31 = new StepNode("P " + or + " Q, P, " + not + "P" + turnstile +  "P", ASSUMPTION);
        StepNode l32 = new StepNode("P " + or + " Q, P, " + not + "P" + turnstile + not +  "P", ASSUMPTION);

        StepNode l21 = new StepNode(
                "P " + or + " Q, " + not + "P" + turnstile + "P" + or + "Q",
                ASSUMPTION);
        StepNode l22 = new StepNode(
                "P " + or + " Q, " + not + "P, P" + turnstile + "Q",
                NEG_ELIM);
        l22.addChild(l31);
        l22.addChild(l32);
        StepNode l23 = new StepNode(
                "P " + or + " Q, " + not + "P, Q" + turnstile + "Q",
                ASSUMPTION);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l2.add(l23);

        root = new StepNode(
                halfOfOr,
                OR_ELIM);
        root.addChildren(l2);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }
    @Test
    public void testOrAndAndProof(){
        StepNode l4 = new StepNode(
                "P " + or + " (Q " + and+  " R), Q " + and + " R" + turnstile +  "Q" + and + "R",
                ASSUMPTION);


        StepNode l32 = new StepNode("P " + or + " (Q " + and+  " R), Q " + and + " R" + turnstile +  "Q",
                AND_ELIM);
        StepNode l31 = new StepNode("P " + or + " (Q " + and+  " R), P " + turnstile +  "P"
                , ASSUMPTION);
        l32.addChild(l4);

        StepNode l21 = new StepNode(
                "P " + or + " (Q " + and+  " R)" + turnstile +  "P" + or + "(Q "+ and + "R)",
                ASSUMPTION);
        StepNode l22 = new StepNode(
                "P " + or + " (Q " + and+  " R), P" + turnstile +  "P" + or + "Q",
                OR_INTRO);
        l22.addChild(l31);
        StepNode l23 = new StepNode(
                "P " + or + " (Q " + and+  " R), Q " + and + " R" + turnstile +  "P" + or + "Q",
                OR_INTRO);
        l23.addChild(l32);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l2.add(l23);


        root = new StepNode(
                orAndAnd,
                OR_ELIM);
        root.addChildren(l2);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void assumptionWorks(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void trueIntroWorks(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " T", TRUE_INTRO);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleWorks1(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + not + "X " + or + not + "(" + not + "X)", EXCL_MIDDLE);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleWorks2(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " X " + or + not + "X", EXCL_MIDDLE);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void andElim1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P", AND_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void andElim2Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void impIntroWorks(){
        StepNode premiss = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void orIntro1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void orIntro2Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " W " + or + " (P" + and + "Q)", OR_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void falseElimWorks(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void negIntro1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "P" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q " + turnstile + not + "(" + not + "P)", NEG_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void andIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void impElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void negIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("S, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("R, Q" + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("S, R" + turnstile +  not + "Q", NEG_INTRO);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void negElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        StepNode p1 = new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", AND_ELIM);
        StepNode p2 = new StepNode("P" + and + "(" + not + "P) " + turnstile + not + "P", AND_ELIM);
        p1.addChild(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P " + and + not + "P", ASSUMPTION));
        p2.addChild(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P " + and + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChild(p1);
        root.addChild(p2);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void orElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, P " + turnstile + " R", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q " + turnstile + " R", OR_ELIM);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void identifyAssumption() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        Proof.parse(root);
        assertEquals(ASSUMPTION, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyTrueIntro() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " T", TRUE_INTRO);
        Proof.parse(root);
        assertEquals(TRUE_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

        @Test
        public void identifyExcludedMiddle1(){
            root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + not + "X " + or + not + "(" + not + "X)", EXCL_MIDDLE);
            Proof.parse(root);
            assertEquals(EXCL_MIDDLE, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
        }

    @Test
    public void identifyExcludedMiddle2(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " X " + or + not + "X", EXCL_MIDDLE);
        Proof.parse(root);
        assertEquals(EXCL_MIDDLE, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyAndElim1(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(AND_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyAndElim2(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(AND_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyImpIntro(){
        StepNode premiss = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(IMP_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyOrIntro1(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(OR_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyOrIntro2(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " W " + or + " (P" + and + "Q)", OR_INTRO);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(OR_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void IdentifyFalseElim(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(FALSE_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyNegIntro1(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "P" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q " + turnstile + not + "(" + not + "P)", NEG_INTRO);
        root.addChild(premiss);
        Proof.parse(root);
        assertEquals(NEG_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyAndIntro(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premisses);
        Proof.parse(root);
        assertEquals(AND_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyImpElim(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premisses);
        Proof.parse(root);
        assertEquals(IMP_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyNegIntro(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("S, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("R, Q" + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("S, R" + turnstile +  not + "Q", NEG_INTRO);
        root.addChildren(premisses);
        Proof.parse(root);
        assertEquals(NEG_INTRO, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyNegElim2(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premisses);
        Proof.parse(root);
        assertEquals(NEG_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void identifyOrElim(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, P " + turnstile + " R", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q " + turnstile + " R", OR_ELIM);
        root.addChildren(premisses);
        Proof.parse(root);
        assertEquals(OR_ELIM, Proof.determineStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void assumptionWithPremises() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        StepNode child = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root.addChild(child);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void assumptionWithoutAssuming() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P", ASSUMPTION);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void trueIntroWithPremises() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " T", TRUE_INTRO);
        StepNode child = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root.addChild(child);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void trueIntroWithFalse() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " F", TRUE_INTRO);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void trueIntroWithoutTrue() {
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P " + and + " R", TRUE_INTRO);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleWithPremises(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + not + "X " + or + not + "(" + not + "X)", EXCL_MIDDLE);
        StepNode child = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root.addChild(child);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleWithoutOr(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + not + "X " + and + not + "(" + not + "X)", EXCL_MIDDLE);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleNotNegation1(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " X " + or + not + "Y", EXCL_MIDDLE);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void excludedMiddleNotNegation2(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + not + not + " X " + or + "X", EXCL_MIDDLE);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andElimNoPremises(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P", AND_ELIM);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andElimTwoPremises(){
        StepNode premiss1 = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        StepNode premiss2 = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " Q", AND_ELIM);
        root.addChild(premiss1);
        root.addChild(premiss2);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andElimDifferentAssumptions(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S) , X " + turnstile + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andElimSwappedAssumptions(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode(not + "(R " + and + " S), " + "P" + and + "Q"  + turnstile + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertTrue(Proof.checkStep(root));
    }

    @Test
    public void andElimWithoutAnd(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + or + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andElimNotFromPremise(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " R", AND_ELIM);
        root.addChild(premiss);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroWithNoPremises(){
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroWithTwoPremises(){
        StepNode premiss1 = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        StepNode premiss2 = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premiss1);
        root.addChild(premiss2);

        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroWithoutImp(){
        StepNode premiss = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premiss);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroWrongRHS(){
        StepNode premise = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P) " + and + " Q)", IMP_INTRO);
        root.addChild(premise);

        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroNoAssumptions(){
        StepNode premise = new StepNode(empty + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode(empty + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroTooFewAssumptions(){
        StepNode premise = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroTooManyAssumptions(){
        StepNode premise = new StepNode("P" + and + "Q, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R, T" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroClauseNotDischarged(){
        StepNode premise = new StepNode("P, R, S" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impIntroMismatchingAssumptions(){
        StepNode premise = new StepNode("P" + and + "Q, R, T" + turnstile + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + turnstile + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroNotOr(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + and + " W", OR_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroTwoPremises(){
        StepNode premise1 = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        StepNode premise2 = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premise1);
        root.addChild(premise2);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroNoPremises(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroMismatchingAssumptions1(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(R " + and + " T)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroMismatchingAssumptions2(){
        StepNode premise = new StepNode("P" + and + "Q" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orIntroDoesNotMatchPremise(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + turnstile + " (P" + and + "S) " + or + " W", OR_INTRO);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void falseElimNoPremises(){
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void falseElimTwoPremises(){
        StepNode premise1 = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " F", ASSUMPTION);
        StepNode premise2 = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        root.addChild(premise1);
        root.addChild(premise2);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void falseElimMismatchingAssumptions(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q), R" + turnstile + " W", FALSE_ELIM);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void falseElimRHSisT(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " T", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void falseElimRHSisNotBoolean(){
        StepNode premise = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " P " + or + " Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + turnstile + " W", FALSE_ELIM);
        root.addChild(premise);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroWithoutAnd(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P", AND_INTRO);
        root.addChildren(premisses);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroOnePremise(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premisses);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroThreePremises(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + turnstile + " R", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premisses);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroAssumptionsInPremisesMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P, R, Q, S" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroPremise1AssumptionsAndConclusionMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P, R, Q, S" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q, S" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroPremise2AndConclusionMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("R, P, Q, S" + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q, S" + turnstile + " P" + and + "Q", AND_INTRO);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void andIntroConclusionDoesNotMatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("R, P, Q" + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P, R, Q" + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + turnstile + " P" + and + "R", AND_INTRO);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimOnePremise(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimThreePremises(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " P" + and + "Q", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimAssumptionsMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W, X " + turnstile + " Q", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimNoImpl(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + or + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimConclusionMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " P" + and + "Q", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimLHSImplicationMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " W", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void impElimConclusionMismatchRHS(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " Q", ASSUMPTION));
        premises.add(new StepNode("P" + and + "Q, Q" + impl + "W" + turnstile + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + turnstile + " X", IMP_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimOnePremise(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimThreePremises(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimPremisesAssumptionsMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P), Q " + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimAssumptionConclusionMismatch(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P), Q" + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimNoNegation(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void negElimNotNegationOFTheOther(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + " P", ASSUMPTION));
        premises.add(new StepNode("P" + and + "(" + not + "P) " + turnstile + not + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + not + "(" + not + "P) " + turnstile + " Q", NEG_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }


    @Test
    public void orElimTwoPremises(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + or + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q " + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orElimFourPremises(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + or + " Q, P " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + or + " Q, P " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + or + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q " + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orElimNoOr(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + and + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + and + " Q, P " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + impl + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q, P" + and + "Q, P" + impl + "Q"  + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orElimNoResolution1(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + and + " Q, S " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + impl + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q, P" + and + "Q, P" + impl + "Q"  + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }

    @Test
    public void orElimNoResolution2(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + and + " Q, P " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + impl + " Q, S " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q, P" + and + "Q, P" + impl + "Q"  + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }


    @Test
    public void orElimMismatchingAssumptions(){
        ArrayList<StepNode> premises = new ArrayList<>();
        premises.add(new StepNode("P " + or + " Q " + turnstile + " P " + or + " Q", ASSUMPTION));
        premises.add(new StepNode("P " + and + " Q, P " + turnstile + " R", ASSUMPTION));
        premises.add(new StepNode("P " + impl + " Q, Q " + turnstile + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q, P" + and + "Q, P" + impl + "Q, S"  + turnstile + " R", OR_ELIM);
        root.addChildren(premises);
        Proof.parse(root);
        assertFalse(Proof.checkStep(root));
    }


}
