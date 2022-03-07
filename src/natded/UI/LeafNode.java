package natded.UI;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import natded.StepNode;
import natded.constants.Step;
import natded.exceptions.JustificationMismatchException;
import natded.exceptions.NoJustificationException;


import java.util.ArrayList;

import static natded.Main.DISPLAY_HEIGHT;

public class LeafNode extends VBox {
//public class LeafNode extends HBox {


    private StepTextField field;
    private HBox childrenBox;
    private HBox fieldHbox;
    private LeafNode parent;
    private Justification justif;
    private ErrorSymbol error;
    Tooltip t = new Tooltip();
    double margin = DISPLAY_HEIGHT/100;


    LeafNode(){
        super();
        this.parent = null;
        VBox textAndLine = new VBox();
        this.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(this, Priority.ALWAYS);

        textAndLine.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(textAndLine, Priority.ALWAYS);
        textAndLine.setAlignment(Pos.BOTTOM_CENTER);
        childrenBox = new HBox();
        HBox.setHgrow(childrenBox, Priority.ALWAYS);
        fieldHbox = new HBox();
        fieldHbox.setPrefHeight(DISPLAY_HEIGHT/30);
        childrenBox.setAlignment(Pos.CENTER);
        childrenBox.setAlignment(Pos.BOTTOM_CENTER);
        fieldHbox.setStyle("-fx-border-style: solid hidden hidden hidden;");
        fieldHbox.setAlignment(Pos.BOTTOM_CENTER);
        fieldHbox.setPadding(new Insets(margin,0.0,0.0,0.0));

        VBox.setMargin(fieldHbox, new Insets(0, margin*2, margin,0));
        HBox.setHgrow(fieldHbox, Priority.ALWAYS);

        AddButton addButton = new AddButton(this);

        this.getChildren().add(childrenBox);

        justif = new Justification(this);
        justif.setPrefHeight(fieldHbox.getPrefHeight());
        justif.setPrefWidth(1.75*fieldHbox.getPrefHeight());

        addButton.setPrefSize(fieldHbox.getPrefHeight(), fieldHbox.getPrefHeight());
        //addButton.setMaxSize(fieldHbox.getPrefHeight(), fieldHbox.getPrefHeight());
        field = new StepTextField(this);

        field.setAlignment(Pos.CENTER);
        field.selectPositionCaret(field.getLength());
        field.setPrefHeight(fieldHbox.getPrefHeight() + 3);

        error = new ErrorSymbol();

        fieldHbox.getChildren().addAll(addButton, field, justif);

        fieldHbox.setAlignment(Pos.BOTTOM_CENTER);

        this.getChildren().add(fieldHbox);


    }

    public void setFieldMinWidth(double width) {
        field.setMinWidth(width);
    }

    public void setText(String text){
        field.setText(text);
    }

    public void setPlaceHolder(String text) {
        field.setPromptText(text);
    }

    LeafNode(LeafNode parent){
        this();
        this.parent = parent;
        field.setEditable(true);
        addSubButton();


    }

    private void addSubButton(){
        SubtractButton s = new SubtractButton(this);
        s.setPrefSize(fieldHbox.getPrefHeight(), fieldHbox.getPrefHeight());
        fieldHbox.getChildren().add(1, s);

    }


    public ArrayList<LeafNode> getChildNodes(){
        ArrayList<LeafNode> fields = new ArrayList<>();
        if (hasChildren()) {
            ObservableList<Node> boxes = childrenBox.getChildren();

            for (Node box : boxes) {
                LeafNode node = (LeafNode) box;
                fields.add(node);
            }
        }
        return fields;
    }

    private boolean hasChildren(){
        return childrenBox.getChildren().size()>0;
    }

    void addChild(){
        childrenBox.getChildren().add(new LeafNode(this));
    }


    StepNode getTree(){
        Step s;
        try {
            s = justif.getValue().getValue();
        } catch (NullPointerException e) {
            s = Step.UNASSIGNED;
        }

        resetError();
        StepNode n = new StepNode(this.field.getText(), s, this);

        for (LeafNode child : getChildNodes()) {
            n.addChild(child.getTree());
        }
        return n;
    }

    private void deleteChild(LeafNode node){
        childrenBox.getChildren().remove(node);
    }

    public void delete() {
        parent.deleteChild(this);
    }

    public void displayException(RuntimeException e) {
        t.setText(e.getMessage());
        t.setFont(new Font(field.getFont().getSize()));
        Tooltip.install(error, t);
        if (e instanceof NoJustificationException || e instanceof JustificationMismatchException) {
            justif.setIncorrect();
        }
        error.setFitHeight(fieldHbox.getPrefHeight());

        fieldHbox.getChildren().add(error);
        error.animate();



    }

    public void resetError() {

        justif.resetStyle();
        Tooltip.uninstall(error, t);
        this.fieldHbox.getChildren().remove(error);
    }

    public void deleteChildren() {
        for (int i = getChildNodes().size()-1; i >=0; i--) {
            getChildNodes().get(i).delete();
        }
    }

    public void setEditable(boolean editable){
        field.setEditableField(editable);
    }

    public void resetJustif(){
        this.justif.getSelectionModel().clearSelection();
    }

}
