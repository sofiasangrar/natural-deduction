package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;



public class AddButton extends Button {

    LeafNode parent;

    AddButton(LeafNode parent) {
        this.parent = parent;
        this.setStyle(UserInterface.buttonStyle);
        //this.setPadding(new Insets(0.0,0.0,0.0,0.0));
        this.setWrapText(false);
        this.setText("+");
        this.setTextFill(Paint.valueOf("green"));
        setOnMouseClicked(event -> parent.addChild());
    }


}
