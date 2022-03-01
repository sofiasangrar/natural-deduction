package natded.UI;

import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

public class SubtractButton extends Button {

    LeafNode parent;

    SubtractButton(LeafNode parent) {
        this.setStyle(UserInterface.buttonStyle);
        this.parent = parent;
        this.setWidth(20);
        this.setHeight(20);
        this.setText("-");
        this.setTextFill(Paint.valueOf("red"));
        setOnMouseClicked(event -> parent.delete());
    }


}
