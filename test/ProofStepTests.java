import lexer.tokens.*;
import parser.Proof;
import lexer.Lexer;
import natded.StepNode;
import org.junit.Test;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
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
        ArrayList<StepNode> l4 = new ArrayList<>();
        l4.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), P " + NDToken.getString() + " P",
                ASSUMPTION));

        ArrayList<StepNode> l3 = new ArrayList<>();
        l3.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), P " + NDToken.getString() + " P " + OrToken.getString() + " Q",
                OR_INTRO));
        l3.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), P " + NDToken.getString() + NotToken.getString() + "(P " + OrToken.getString() + " Q)",
                ASSUMPTION));

        ArrayList<StepNode> l6 = new ArrayList<>();
        l6.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), Q " + NDToken.getString() + " Q",
                ASSUMPTION));
        ArrayList<StepNode> l5 = new ArrayList<>();
        l5.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), Q " + NDToken.getString() + "P " + OrToken.getString() + " Q", OR_INTRO));
        l5.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q), Q " + NDToken.getString() + NotToken.getString() + "(P " + OrToken.getString() + " Q)",
                ASSUMPTION));

        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q) " + NDToken.getString() + NotToken.getString() + "P",
                NEG_INTRO));
        l2.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q) " + NDToken.getString() + NotToken.getString() + "Q",
                NEG_INTRO));

        ArrayList<StepNode> l1 = new ArrayList<>();
        l1.add(new StepNode(
                NotToken.getString() + "(P " + OrToken.getString() + " Q) " + NDToken.getString() + NotToken.getString() + "P" + AndToken.getString() + NotToken.getString() + "Q",
                AND_INTRO));

        root = new StepNode(
                EmptyToken.getString()  +NDToken.getString() + NotToken.getString() + "(P " + OrToken.getString() + " Q) " + ImpliesToken.getString() + NotToken.getString() + "P" + AndToken.getString() + NotToken.getString() + "Q",
                IMP_INTRO);
        root.addChildren(l1);

        Proof p = Proof.parse(root);
        assertTrue(p.isValid());
    }

    @Test
    public void assumptionWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode("P^Q, !(R ^ S)|- P^Q", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), ASSUMPTION));
    }

    @Test
    public void trueIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode("P^Q, !(R ^ S)|- T", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), TRUE_INTRO));
    }

    @Test
    public void excludedMiddleWorks1(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode("P^Q, !(R ^ S)|- !X V !(!X)", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), EXCL_MIDDLE));
    }

    @Test
    public void excludedMiddleWorks2(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode("P^Q, !(R ^ S)|- X V !X", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), EXCL_MIDDLE));
    }

    @Test
    public void andElim1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !(R ^ S)|- P^Q", null));
        root = new StepNode("P^Q, !(R ^ S)|- P", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), AND_ELIM));
    }

    @Test
    public void andElim2Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !(R ^ S)|- P^Q", null));
        root = new StepNode("P^Q, !(R ^ S)|- Q", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), AND_ELIM));
    }

    @Test
    public void impIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, R, S|- !(!P ^ !Q)", null));
        root = new StepNode("S, R|- (P^Q) => !((!P)^(!Q))", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), IMP_INTRO));
    }

    @Test
    public void orIntro1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !(R ^ S)|- P^Q", null));
        root = new StepNode("P^Q, !(R ^ S)|- (P^Q) V W", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), OR_INTRO));
    }

    @Test
    public void orIntro2Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !(R ^ S)|- P^Q", null));
        root = new StepNode("P^Q, !(R ^ S)|- W V (P^Q)", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), OR_INTRO));
    }

    @Test
    public void falseElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !(P ^ Q)|- F", null));
        root = new StepNode("P^Q, !(P ^ Q)|- W", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), FALSE_ELIM));
    }

    @Test
    public void negIntro1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, !P|- F", null));
        root = new StepNode("P^Q |- !(!P)", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), NEG_INTRO));
    }

    @Test
    public void andIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("R, P, Q|- P", null));
        premisses.add(new StepNode("P, R, Q|- Q", null));
        root = new StepNode("P, R, Q|- P^Q", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), AND_INTRO));
    }

    @Test
    public void impElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^Q, Q=>W |- Q", null));
        premisses.add(new StepNode("P^Q, Q=>W|- Q=>W", null));
        root = new StepNode("P^Q, Q=>W |- W", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), IMP_ELIM));
    }

    @Test
    public void negIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("S, Q|- P", null));
        premisses.add(new StepNode("R, Q|- !P", null));
        root = new StepNode("S, R|- !Q", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), NEG_INTRO));
    }

    @Test
    public void negElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P^(!P) |- P", null));
        premisses.add(new StepNode("P^(!P) |- !P", null));
        root = new StepNode("P^(!P) |- Q", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), NEG_ELIM));
    }

    @Test
    public void orElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode("P V Q |- P V Q", null));
        premisses.add(new StepNode("P V Q, P |- R", null));
        premisses.add(new StepNode("P V Q, Q |- R", null));
        root = new StepNode("P V Q |- R", null);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput(), OR_ELIM));
    }


}
