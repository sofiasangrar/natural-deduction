package natded.problemDomain;

import natded.computationLogic.NatDedUtilities;
import natded.constants.Step;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    StepNode parent;
    String input;
    TokenNode parsedInput;
    List<StepNode> children;
    int index;
    Step step;

    public StepNode(StepNode parent) {
        new StepNode(parent, "", null, new ArrayList<>());
    }

    public StepNode() {
    }

    public StepNode(StepNode parent, String input, TokenNode parsedInput, List<StepNode> children) {
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

    public TokenNode getParsedInput() {
        return parsedInput;
    }

    public void setParsedInput(TokenNode parsedInput) {
        this.parsedInput = parsedInput;
    }

    public List<StepNode> getChildren(){
        return children;
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
