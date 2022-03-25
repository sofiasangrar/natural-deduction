package natded;

import natded.UI.Node;
import natded.constants.Step;
import parser.Sequent;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    private String input;
    private Sequent parsedInput;
    private List<StepNode> children;
    private Step step;
    private Node node;
    private boolean incorrectSyntax; //true if syntax is incorrect

    public StepNode(String input, Step step) {
        this.input = input;
        this.parsedInput = null;
        this.children = new ArrayList<>();
        this.step = step;
        this.node = null;
        this.incorrectSyntax = false;
    }

    public StepNode(String input, Step step, Node node) {
        this(input, step);
        this.node = node;
    }

    /**
     * record whether the current node has incorrect syntax
     * @param incorrectSyntax whether the syntax is incorrect
     */
    public void setIncorrectSyntax(boolean incorrectSyntax) {
        this.incorrectSyntax = incorrectSyntax;
    }

    /**
     * getter for syntax correctness
     * @return return wjhether syntax is incorrect or not
     */
    public boolean hasIncorrectSyntax() {
        return incorrectSyntax;
    }

    /**
     * get String representation of node from field
     * @return string of node
     */
    public String getInput() {
        return input;
    }

    /**
     * get a natural deduction step if the input has been parsed
     * @return parsed input step
     */
    public Sequent getParsedInput() {
        return parsedInput;
    }

    /**
     * set the parse tree
     * @param parsedInput a parsed natural deduction step
     */
    public void setParsedInput(Sequent parsedInput) {
        this.parsedInput = parsedInput;
    }

    /**
     * get child nodes
     * @return list of child nodes
     */
    public List<StepNode> getChildren(){
        return children;
    }

    /**
     * get child nodes in their parsed format
     * @return list of parse trees of child nodes
     */
    public ArrayList<Sequent> getPremisses(){
        ArrayList<Sequent> sequents = new ArrayList<>();
        for (StepNode node : children) {
            sequents.add(node.parsedInput);
        }
        return sequents;
    }

    /**
     * add antecedent to node
     * @param child child node to add
     */
    public void addChild(StepNode child) {
        children.add(child);
    }

    /**
     * add multiple antecedents
     * @param children antecedent nodes to add to current node as children
     */
    public void addChildren(ArrayList<StepNode> children) {
        this.children.addAll(children);
    }

    /**
     * getter for assigned justification
     * @return justification for step
     */
    public Step getStep() {
        return step;
    }

    /**
     * get coupled ui element
     * @return UI element that the input to this node came from
     */
    public Node getUIElement(){
        return node;
    }

}
