package natded;

import natded.constants.Step;
import parser.Clause;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    StepNode parent;
    String input;
    Clause parsedInput;
    List<StepNode> children;
    int index;
    Step step;

    public StepNode(StepNode parent) {
        new StepNode(parent, "", null, new ArrayList<>());
    }

    public StepNode() {
    }

    public StepNode(StepNode parent, String input, Clause parsedInput, List<StepNode> children) {
        this.parent = parent;
        this.input = input;
        this.parsedInput = parsedInput;
        this.children = children;
        index=NatDedUtilities.nextIndex();
        step=Step.UNASSIGNED;
    }

    public StepNode getParent() {
        return parent;
    }

    public void setParent(StepNode parent) {
        this.parent = parent;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
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

    public int getIndex(){ return index;}

    public void setIndex(int index){
        this.index = index;
    }

    public void setChildrenNotNull() {
        this.children = new ArrayList<>();
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
