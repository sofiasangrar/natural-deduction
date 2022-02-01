package natded.UI;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import natded.StepNode;
import natded.constants.Step;
import natded.exceptions.AssumptionsMismatchException;
import natded.exceptions.JustificationMismatchException;
import natded.exceptions.NoJustificationException;
import natded.exceptions.PremiseNumberException;
import parser.Assumptions;

import java.util.ArrayList;

public class LeafNode extends VBox {

    private StepTextField field;
    private HBox childrenBox;
    private HBox fieldHbox;
    private LeafNode parent;
    private Justification justif;
    Tooltip t = new Tooltip();


    LeafNode(){
        super();
        this.parent = null;
        this.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(this, Priority.ALWAYS);
        VBox.setMargin(this, new Insets(10.0d));
        this.setAlignment(Pos.BOTTOM_CENTER);
        childrenBox = new HBox();
        HBox.setMargin(childrenBox, new Insets(10.0d));
        HBox.setHgrow(childrenBox, Priority.ALWAYS);

        field = new StepTextField(this);
        //field.setEditable(false);
        field.setMinSize(field.getWidth(), field.getHeight());
        field.setAlignment(Pos.CENTER);
        fieldHbox = new HBox();
        childrenBox.setAlignment(Pos.CENTER);
        VBox.setMargin(fieldHbox, new Insets(10.0d));
        childrenBox.setAlignment(Pos.BOTTOM_CENTER);
        fieldHbox.setStyle("-fx-border-style: solid hidden hidden hidden;");
        VBox.setMargin(fieldHbox, new Insets(10.0d));
        HBox.setHgrow(fieldHbox, Priority.ALWAYS);

        AddButton addButton = new AddButton(this);

        this.getChildren().add(childrenBox);

        justif = new Justification(this);
        HBox justifBox = new HBox();
        justifBox.getChildren().add(justif);
        justifBox.setAlignment(Pos.TOP_LEFT);

        fieldHbox.getChildren().addAll(addButton, field, justifBox);
        fieldHbox.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().add(fieldHbox);

    }

    public void setText(String text){
        field.setText(text);
    }

    LeafNode(LeafNode parent){
        this();
        this.parent = parent;
        field.setEditable(true);
        addSubButton();
    }

    private void addSubButton(){
        fieldHbox.getChildren().add(1, new SubtractButton(this));
    }


    private ArrayList<LeafNode> getChildNodes(){
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
        if (e instanceof NoJustificationException) {
            fieldHbox.getChildren().add(new Text(e.getMessage()));
            //Tooltip.install(field, t);
            field.setTooltip(t);
            this.justif.setStyle("-fx-background-color: #eb6651");
        }
        if (e instanceof AssumptionsMismatchException) {
            //Tooltip.install(field, t);
            field.setTooltip(t);
            //fieldHbox.getChildren().add(new Text(e.getMessage()));
        }
        if (e instanceof JustificationMismatchException) {
            fieldHbox.getChildren().add(new Text(e.getMessage()));
            //Tooltip.install(field, t);
            field.setTooltip(t);
            this.justif.setStyle("-fx-background-color: #eb6651");
        }
        if (e instanceof PremiseNumberException) {
            Tooltip.install(field, t);
            field.setTooltip(t);
            //fieldHbox.getChildren().add(new Text(e.getMessage()));
        }
    }

    public void resetError(){
        if (fieldHbox.getChildren().get(fieldHbox.getChildren().size()-1) instanceof Text) {
            fieldHbox.getChildren().remove(fieldHbox.getChildren().size()-1);
        }
        this.justif.setStyle("-fx-background-color: gray");
        field.setTooltip(null);
    }


}
