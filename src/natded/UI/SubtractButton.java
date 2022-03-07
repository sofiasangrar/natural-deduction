package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

public class SubtractButton extends Button {

    LeafNode parent;

    SubtractButton(LeafNode parent) {
        this.setStyle(UserInterface.buttonStyle);
        this.parent = parent;
        //this.setPadding(new Insets(0.0,0.0,0.0,0.0));
        this.setText("-");
        this.setTextFill(Paint.valueOf("red"));
        setOnMouseClicked(event -> parent.delete());
    }


}
