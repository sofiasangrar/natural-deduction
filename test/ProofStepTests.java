import lexer.tokens.*;
import parser.Proof;
import lexer.Lexer;
import natded.StepNode;
import org.junit.Test;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static natded.NatDedUtilities.*;
import static natded.constants.Step.*;
import static org.junit.Assert.assertFalse;

public class ProofStepTests {

    StepNode root;

    public void parseTree(StepNode node){
        Lexer.setLexString(node.getInput());
        Parser.t = Lexer.lex();
        node.setParsedInput(Clause.parse());
        for (int i = 0; i < node.getChildren().size(); i++) {
            parseTree(node.getChildren().get(i));
        }
    }

    @Test
    public void testProof(){
        StepNode l4 = new StepNode(
                not + "(P " + or + " Q), P " + nd + " P",
                ASSUMPTION);

        ArrayList<StepNode> l3 = new ArrayList<>();

        StepNode l31 = new StepNode(
                not + "(P " + or + " Q), P " + nd + " P " + or + " Q",
                OR_INTRO);

        l31.addChild(l4);
        l3.add(l31);
        l3.add(new StepNode(
                not + "(P " + or + " Q), P " + nd + not + "(P " + or + " Q)",
                ASSUMPTION));

        StepNode l6 = new StepNode(
                not + "(P " + or + " Q), Q " + nd + " Q",
                ASSUMPTION);

        ArrayList<StepNode> l5 = new ArrayList<>();
        StepNode l51 = new StepNode(
                not + "(P " + or + " Q), Q " + nd + "P " + or + " Q", OR_INTRO);
        l51.addChild(l6);
        l5.add(l51);
        l5.add(new StepNode(
                not + "(P " + or + " Q), Q " + nd + not + "(P " + or + " Q)",
                ASSUMPTION));

        StepNode l21 = new StepNode(
                not + "(P " + or + " Q) " + nd + not + "P",
                NEG_INTRO);
        StepNode l22 = new StepNode(
                not + "(P " + or + " Q) " + nd + not + "Q",
                NEG_INTRO);
        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(l21);
        l2.add(l22);
        l21.addChildren(l3);
        l22.addChildren(l5);

        StepNode l1 = new StepNode(
                not + "(P " + or + " Q) " + nd + not + "P" + and + not + "Q",
                AND_INTRO);
        l1.addChildren(l2);

        root = new StepNode(
                empty + nd + not + "(P " + or + " Q) " + impl + not + "P" + and + not + "Q",
                IMP_INTRO);
        root.addChild(l1);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void assumptionWorks(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P" + and + "Q", ASSUMPTION);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());}

    @Test
    public void trueIntroWorks(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " T", ASSUMPTION);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid()); }

    @Test
    public void excludedMiddleWorks1(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + not + "X " + or + not + "(" + not + "X)", EXCL_MIDDLE);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void excludedMiddleWorks2(){
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " X " + or + not + "X", EXCL_MIDDLE);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void andElim1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P", AND_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void andElim2Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " Q", AND_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void impIntroWorks(){
        StepNode premiss = new StepNode("P" + and + "Q, R, S" + nd + not + "(" + not + "P " + and + " " + not + "Q)", ASSUMPTION);
        root = new StepNode("S, R" + nd + " (P" + and + "Q) " + impl +  not + "((" + not + "P)" + and + "(" + not + "Q))", IMP_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void orIntro1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " (P" + and + "Q) " + or + " W", OR_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void orIntro2Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " P" + and + "Q", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(R " + and + " S)" + nd + " W " + or + " (P" + and + "Q)", OR_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void falseElimWorks(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + nd + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q, " + not + "(P " + and + " Q)" + nd + " W", FALSE_ELIM);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void negIntro1Works(){
        StepNode premiss = new StepNode("P" + and + "Q, " + not + "P" + nd + " F", ASSUMPTION);
        root = new StepNode("P" + and + "Q " + nd + not + "(" + not + "P)", NEG_INTRO);
        root.addChild(premiss);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void andIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q" + nd + " P", ASSUMPTION));
        premisses.add(new StepNode("P, R, Q" + nd + " Q", ASSUMPTION));
        root = new StepNode("P, R, Q" + nd + " P" + and + "Q", AND_INTRO);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void impElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W " + nd + " Q", ASSUMPTION));
        premisses.add(new StepNode("P" + and + "Q, Q" + impl + "W" + nd + " Q" + impl + "W", ASSUMPTION));
        root = new StepNode("P" + and + "Q, Q" + impl + "W " + nd + " W", IMP_ELIM);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void negIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("S, Q" + nd + " P", ASSUMPTION));
        premisses.add(new StepNode("R, Q" + nd + not + "P", ASSUMPTION));
        root = new StepNode("S, R" + nd +  not + "Q", NEG_INTRO);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void negElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P" + and + "(" + not + "P) " + nd + " P", ASSUMPTION));
        premisses.add(new StepNode("P" + and + "(" + not + "P) " + nd + not + "P", ASSUMPTION));
        root = new StepNode("P" + and + "(" + not + "P) " + nd + " Q", NEG_ELIM);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void orElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P " + or + " Q " + nd + " P " + or + " Q", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, P " + nd + " R", ASSUMPTION));
        premisses.add(new StepNode("P " + or + " Q, Q " + nd + " R", ASSUMPTION));
        root = new StepNode("P " + or + " Q " + nd + " R", OR_ELIM);
        root.addChildren(premisses);
        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }


}
