package natded.UI;

import javafx.scene.control.Button;
import javafx.scene.paint.Paint;



public class AddButton extends Button {

    AddButton(Node parent) {
        this.setStyle(UserInterface.buttonStyle);
        this.setWrapText(false);
        this.setText("+");
        this.setTextFill(Paint.valueOf("green"));
        setOnMouseClicked(event -> parent.addChild());
    }


}
