import clauseParser.Proof;
import lexer.Lexer;
import natded.computationLogic.Logic;
import natded.problemDomain.StepNode;
import org.junit.Test;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ProofStepTests {

    StepNode root;

    public void parseTree(StepNode node){
        Lexer.setLexString(node.getInput());
        Parser.t = Lexer.lex();
        node.setParsedInput(Clause.parse(node.getIndex()));
        for (int i = 0; i < node.getChildren().size(); i++) {
            parseTree(node.getChildren().get(i));
        }
    }

    @Test
    public void testProof(){
        ArrayList<StepNode> l4 = new ArrayList<>();
        l4.add(new StepNode(null, "!(PVQ), P |- P", null, new ArrayList<>()));
        ArrayList<StepNode> l3 = new ArrayList<>();
        l3.add(new StepNode(null, "!(PVQ), P |- P V Q", null, l4));
        l3.add(new StepNode(null, "!(PVQ), P |- !(P V Q)", null, new ArrayList<>()));

        ArrayList<StepNode> l6 = new ArrayList<>();
        l6.add(new StepNode(null, "!(PVQ), Q |- Q", null, new ArrayList<>()));
        ArrayList<StepNode> l5 = new ArrayList<>();
        l5.add(new StepNode(null, "!(PVQ), Q |- P V Q", null, l6));
        l5.add(new StepNode(null, "!(PVQ), Q |- !(P V Q)", null, new ArrayList<>()));

        ArrayList<StepNode> l2 = new ArrayList<>();
        l2.add(new StepNode(null, "!(PVQ) |- (!P)", null, l3));
        l2.add(new StepNode(null, "!(PVQ) |- (!Q)", null,l5));

        ArrayList<StepNode> l1 = new ArrayList<>();
        l1.add(new StepNode(null, "!(PVQ) |- (!P) ^ (!Q)", null,l2));

        root = new StepNode(null, "%|-!(PVQ)=>(!P) ^ (!Q)", null, l1);

        parseTree(root);
        assertFalse(Logic.proofIsInvalid(root));
    }

    @Test
    public void assumptionWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode(null, "P^Q, !(R ^ S)|- P^Q", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void trueIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode(null, "P^Q, !(R ^ S)|- T", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void excludedMiddleWorks1(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode(null, "P^Q, !(R ^ S)|- !X V !(!X)", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void excludedMiddleWorks2(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        root = new StepNode(null, "P^Q, !(R ^ S)|- X V !X", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void andElim1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !(R ^ S)|- P^Q", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, !(R ^ S)|- P", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void andElim2Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !(R ^ S)|- P^Q", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, !(R ^ S)|- Q", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void impIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, R, S|- !(!P ^ !Q)", null, new ArrayList<>()));
        root = new StepNode(null, "S, R|- (P^Q) => !((!P)^(!Q))", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void orIntro1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !(R ^ S)|- P^Q", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, !(R ^ S)|- (P^Q) V W", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void orIntro2Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !(R ^ S)|- P^Q", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, !(R ^ S)|- W V (P^Q)", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void falseElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !(P ^ Q)|- F", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, !(P ^ Q)|- W", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void negIntro1Works(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, !P|- F", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q |- !(!P)", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void andIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "R, P, Q|- P", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "P, R, Q|- Q", null, new ArrayList<>()));
        root = new StepNode(null, "P, R, Q|- P^Q", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void impElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^Q, Q=>W |- Q", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "P^Q, Q=>W|- Q=>W", null, new ArrayList<>()));
        root = new StepNode(null, "P^Q, Q=>W |- W", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void negIntroWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "S, Q|- P", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "R, Q|- !P", null, new ArrayList<>()));
        root = new StepNode(null, "S, R|- !Q", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void negElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "P^(!P) |- P", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "P^(!P) |- !P", null, new ArrayList<>()));
        root = new StepNode(null, "P^(!P) |- Q", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }

    @Test
    public void orElimWorks(){
        ArrayList<StepNode> premisses = new ArrayList<>();
        premisses.add(new StepNode(null, "PVQ |- P V Q", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "P V Q, P |- R", null, new ArrayList<>()));
        premisses.add(new StepNode(null, "P V Q, Q |- R", null, new ArrayList<>()));
        root = new StepNode(null, "P V Q |- R", null, premisses);
        parseTree(root);
        assertTrue(Proof.checkStep(root.getPremisses(), root.getParsedInput()));
    }


}
