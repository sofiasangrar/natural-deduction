package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

import static natded.Main.DISPLAY_WIDTH;

public class AddButton extends Button {

    LeafNode parent;

    AddButton(LeafNode parent) {
        this.parent = parent;
        this.setStyle(UserInterface.buttonStyle);
        this.setWrapText(false);
        this.setText("+");
        this.setTextFill(Paint.valueOf("green"));
        setOnMouseClicked(event -> parent.addChild());
    }


}
