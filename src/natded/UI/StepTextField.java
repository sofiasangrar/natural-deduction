package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import natded.problemDomain.StepNode;

public class StepTextField extends TextField {
    private AddButton addButton;
    private StepNode node;
    private Rectangle line;


    public StepTextField(StepNode node) {
        this.node = node;
        addButton = new AddButton(node.getIndex());
        line = new Rectangle();

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
