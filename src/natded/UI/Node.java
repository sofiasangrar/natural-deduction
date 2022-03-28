package natded.UI;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import natded.StepNode;
import natded.constants.Step;
import natded.exceptions.JustificationMismatchException;
import natded.exceptions.NoJustificationException;


import java.util.ArrayList;
import java.util.List;

import static natded.Main.DISPLAY_HEIGHT;

public class Node extends VBox {

    private StepTextField field;
    private HBox childrenBox;
    private HBox fieldHbox;
    private Node parent;
    private Justification justif;
    private ErrorSymbol error;
    private Tooltip t = new Tooltip();
    private final double MARGIN = DISPLAY_HEIGHT/100;


    Node(){
        super();
        this.parent = null;
        this.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(this, Priority.ALWAYS);

        //container for subtrees
        childrenBox = new HBox();
        childrenBox.setAlignment(Pos.CENTER);
        childrenBox.setAlignment(Pos.BOTTOM_CENTER);
        HBox.setHgrow(childrenBox, Priority.ALWAYS);
        this.getChildren().add(childrenBox);

        //container for the text field, add/substract buttons and justification
        fieldHbox = new HBox();
        fieldHbox.setPrefHeight(DISPLAY_HEIGHT/30);
        fieldHbox.setStyle("-fx-border-style: solid hidden hidden hidden;"); //add top border line
        fieldHbox.setAlignment(Pos.BOTTOM_CENTER);
        fieldHbox.setPadding(new Insets(MARGIN,0.0,0.0,0.0));
        VBox.setMargin(fieldHbox, new Insets(0, MARGIN*2, MARGIN,0));
        HBox.setHgrow(fieldHbox, Priority.ALWAYS);

        //add button
        AddButton addButton = new AddButton(this);
        addButton.setPrefSize(fieldHbox.getPrefHeight(), fieldHbox.getPrefHeight());

        //text field to write proof steps
        field = new StepTextField();
        field.setAlignment(Pos.CENTER);
        field.selectPositionCaret(field.getLength());
        field.setPrefHeight(fieldHbox.getPrefHeight() + 3);

        //justification dropdown
        justif = new Justification();
        justif.setPrefHeight(fieldHbox.getPrefHeight());
        justif.setPrefWidth(1.75*fieldHbox.getPrefHeight());

        //error symbol
        error = new ErrorSymbol();

        fieldHbox.getChildren().addAll(addButton, field, justif);

        this.getChildren().add(fieldHbox);
    }

    /**
     * assign a minimum width to the text field
     * @param width desired minimum width
     */
    void setFieldMinWidth(double width) {
        field.setMinWidth(width);
    }

    /**
     * set text
     * @param text text to place into field
     */
    void setText(String text){
        field.setText(text);
    }

    /**
     * set placeholder text when own goal is selected
     */
    void setPlaceHolder() {
        field.setPromptText("Type goal here...");
    }

    /**
     * create a new non-root node
     */
    public Node(Node parent){
        this();
        this.parent = parent;
        setEditable(true);
        addSubButton();
    }

    /**
     * add a remove button for the current node into the correct place
     */
    private void addSubButton(){
        SubtractButton s = new SubtractButton(this);
        s.setPrefSize(fieldHbox.getPrefHeight(), fieldHbox.getPrefHeight());
        fieldHbox.getChildren().add(1, s);

    }

    /**
     * get antecedent nodes from UI
     * @return list of child nodes
     */
    ArrayList<Node> getChildNodes(){
        ArrayList<Node> fields = new ArrayList<>();
        if (hasChildren()) {
            ObservableList<javafx.scene.Node> boxes = childrenBox.getChildren();

            for (javafx.scene.Node box : boxes) {
                Node node = (Node) box;
                fields.add(node);
            }
        }
        return fields;
    }

    /**
     * determine whether the node has antecedents
     * @return if node has child nodes
     */
    private boolean hasChildren(){
        return childrenBox.getChildren().size()>0;
    }

    /**
     * add new premise to node
     */
    void addChild(){
        childrenBox.getChildren().add(new Node(this));
    }

    /**
     * add new pre-defined child for loading
     * @param node node being written
     */
    void addChild(Node node){
        childrenBox.getChildren().add(node);
        node.parent = this;
    }


    /**
     * retrieve text in each textfield and place into a proof tree
     * @return unparsed proof tree
     */
    StepNode getTree(){
        Step s;
        try {
            s = justif.getValue().getValue();
        } catch (NullPointerException e) {
            s = Step.UNASSIGNED;
        }

        resetError();
        String text = "";
        if (this.field.getText()!=null) {
         text = this.field.getText();
        }
        StepNode n = new StepNode(text, s, this);

        for (Node child : getChildNodes()) {
            n.addChild(child.getTree());
        }
        return n;
    }

    /**
     * delete a specified antecedent step from the tree
     * @param node node to delete
     */
    private void deleteChild(Node node){
        childrenBox.getChildren().remove(node);
    }

    /**
     * delete this node from the tree
     */
    public void delete() {
        int index = parent.getChildNodes().indexOf(this);
        List<Node> children = this.getChildNodes();
        parent.deleteChild(this);
        parent.childrenBox.getChildren().addAll(index, children);
        for (Node child : children){
            child.parent = parent;
        }
    }

    /**
     * show the error symbol and assign it a tooltip detailing the issue
     * @param e exception to display
     */
    public void displayException(RuntimeException e) {
        t.setText(e.getMessage());
        t.setFont(new Font(field.getFont().getSize()));
        Tooltip.install(error, t);

        //highlight the justification box if the error is to do with the justification
        if (e instanceof NoJustificationException || e instanceof JustificationMismatchException) {
            justif.setIncorrect();
        }
        error.setFitHeight(fieldHbox.getPrefHeight());
        fieldHbox.getChildren().add(error);

        //animate the error each time it is shown
        UserInterface.animate(error);
    }

    /**
     * remove error signifiers from node
     */
    public void resetError() {
        justif.resetStyle();
        Tooltip.uninstall(error, t);
        this.fieldHbox.getChildren().remove(error);
    }

    /**
     * delete all premisses of this node from ui
     */
    void deleteChildren() {
        if (getChildNodes().size() > 0) {
            childrenBox.getChildren().clear();
        }
    }

    /**
     * set text field to be editable
     * @param editable whether or not the field should be editable
     */
    public void setEditable(boolean editable){
        field.setEditableField(editable);
    }

    /**
     * clear the selection on the justification
     */
    public void resetJustif(){
        this.justif.set(Step.UNASSIGNED);
    }

    /**
     * set step for justification
     * @param step step to select
     */
    public void setJustif(Step step){
        justif.set(step);
    }

    /**
     * fit field to width of text
     */
    public void adjustFieldSize(){
        field.adjustSize();
    }

}
