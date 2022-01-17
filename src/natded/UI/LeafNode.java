package natded.UI;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Pair;
import javafx.util.StringConverter;
import natded.StepNode;
import natded.constants.SpaceState;
import natded.constants.Step;

import java.util.ArrayList;
import java.util.List;

public class LeafNode extends VBox {

    private TextField field;
    private HBox childrenBox;
    private HBox fieldHbox;
    private LeafNode parent;
    private ChoiceBox<Pair<String, Step>> justif;

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

        field = new TextField();
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

        createChoiceBox();
        HBox justifBox = new HBox();
        justifBox.getChildren().add(justif);
        justifBox.setAlignment(Pos.TOP_LEFT);

        fieldHbox.getChildren().addAll(field, addButton);
        fieldHbox.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().add(fieldHbox);




    }

    LeafNode(LeafNode parent){
        this();
        this.parent = parent;
        addSubButton();
    }

    private void createChoiceBox(){
        justif = new ChoiceBox<>();
        List<Pair<String, Step>> choices = new ArrayList<>();
        choices.add(new Pair("Asseumption", Step.ASSUMPTION));
        choices.add(new Pair("Implication-Elimination", Step.IMP_ELIM));
        choices.add(new Pair("Implication-Introduction", Step.IMP_INTRO));
        choices.add(new Pair("And-Elimination", Step.AND_ELIM));
        choices.add(new Pair("And-Introduction", Step.AND_INTRO));
        choices.add(new Pair("Or-Elimination", Step.OR_ELIM));
        choices.add(new Pair("Or-Introduction", Step.OR_INTRO));
        choices.add(new Pair("Negation-Elimination", Step.NEG_ELIM));
        choices.add(new Pair("Negation-Introduction", Step.NEG_INTRO));
        choices.add(new Pair("False-Elimination", Step.FALSE_ELIM));
        choices.add(new Pair("True-Introduction", Step.TRUE_INTRO));
        choices.add(new Pair("Excluded Middle", Step.EXCL_MIDDLE));

        justif.setConverter( new StringConverter<Pair<String,Step>>() {
            @Override
            public String toString(Pair<String, Step> pair) {
                return pair.getKey();
            }

            @Override
            public Pair<String, Step> fromString(String string) {
                return null;
            }
        });

        justif.getItems().addAll( choices );
    }

    private void addSubButton(){
        fieldHbox.getChildren().add(new SubtractButton(this));
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
        StepNode n = new StepNode(null, this.field.getText(), null, new ArrayList<>());
        for (LeafNode child : getChildNodes()) {
            n.addChild(child.getTree(n));
        }
        return n;
    }

    private StepNode getTree(StepNode parent){
        StepNode n = new StepNode(parent, this.field.getText(), null, new ArrayList<>());
        for (LeafNode child : getChildNodes()) {
            n.addChild(child.getTree(n));
        }
        return n;
    }

    private void deleteChild(LeafNode node){
        childrenBox.getChildren().remove(node);
    }

    public void delete() {
        parent.deleteChild(this);
    }
}
