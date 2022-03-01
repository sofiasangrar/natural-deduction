package natded.UI;

import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

public class AddButton extends Button {

    LeafNode parent;

    AddButton(LeafNode parent) {
        this.parent = parent;
        this.setStyle(UserInterface.buttonStyle);
        this.setWidth(20);
        this.setHeight(20);
        this.setText("+");
        this.setTextFill(Paint.valueOf("green"));
        setOnMouseClicked(event -> parent.addChild());
    }


}
