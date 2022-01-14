package natded.UI;

import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import natded.StepNode;

import java.util.ArrayList;

public class StepTextField extends TextField {
    private AddButton addButton;
    private StepNode node;
    private Rectangle line;


    public StepTextField(StepNode node) {
        this.node = node;
        addButton = new AddButton(node.getIndex());
        line = new Rectangle();

    }

    StepTextField(){
        super();
    }

    public int getIndex() {
        return node.getIndex();
    }

    public StepNode getNode() {
        return node;
    }

    public AddButton getAddButton() {
        return addButton;
    }

    public Rectangle getLine(){ return line;}

    public void setLine(Rectangle line) {
        this.line = line;
    }
}
