package natded.UI;

import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

public class SubtractButton extends Button {


    SubtractButton(LeafNode parent) {
        this.setStyle(UserInterface.buttonStyle);
        this.setText("-");
        this.setTextFill(Paint.valueOf("red"));
        setOnMouseClicked(event -> parent.delete());
    }


}
