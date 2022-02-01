package natded;

import natded.UI.LeafNode;
import natded.constants.Step;
import parser.Clause;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    String input;
    Clause parsedInput;
    List<StepNode> children;
    Step step;
    LeafNode node;

    public StepNode(String input, Step step) {
        this.input = input;
        this.parsedInput = null;
        this.children = new ArrayList<>();
        this.step=step;
        this.node = null;
    }

    public StepNode(String input, Step step, LeafNode node) {
        this(input, step);
        this.node = node;
    }

    public String getInput() {
        return input;
    }

    public Clause getParsedInput() {
        return parsedInput;
    }

    public void setParsedInput(Clause parsedInput) {
        this.parsedInput = parsedInput;
    }

    public List<StepNode> getChildren(){
        return children;
    }

    public ArrayList<Clause> getPremisses(){
        ArrayList<Clause> clauses = new ArrayList<>();
        for (StepNode node : children) {
            clauses.add(node.parsedInput);
        }
        return clauses;
    }

    public void addChild(StepNode child) {
        children.add(child);
    }

    public void addChildren(ArrayList<StepNode> children) {
        this.children.addAll(children);
    }

    public Step getStep() {
        return step;
    }

    public LeafNode getUIElement(){
        return node;
    }
}
