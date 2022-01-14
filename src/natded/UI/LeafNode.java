package natded.UI;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import natded.StepNode;

import java.util.ArrayList;

public class LeafNode extends VBox {

    TextField field;
    private HBox hbox;
    StepNode node;
    AddButton button;

    LeafNode(){
        super();
        this.setAlignment(Pos.BOTTOM_CENTER);
        hbox = new HBox();
        field = new TextField();
        button = new AddButton(0);
        button.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(hbox);
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().add(button);
        getChildren().add(buttonBox);
        getChildren().add(field);

    }

    LeafNode(StepNode node){
        super();
        this.node = node;
        this.setAlignment(Pos.BOTTOM_CENTER);
        hbox = new HBox();
        field = new TextField(node.getInput());
        button = new AddButton(0);
        button.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(hbox);
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().add(button);
        getChildren().add(buttonBox);
        getChildren().add(field);
    }

    ArrayList<TextField> getChildFields(){
        ArrayList<TextField> fields = new ArrayList<>();
        if (hasChildren()) {
            ObservableList<Node> boxes = hbox.getChildren();

            for (Node box : boxes) {
                LeafNode node = (LeafNode) box;
                fields.add(node.getTextField());
            }
        }
        return fields;
    }

    boolean hasChildren(){
        return hbox.getChildren().size()>0;
    }

    void addChild(){
        hbox.getChildren().add(new LeafNode());
    }

    TextField getTextField(){
        return field;
    }

}
