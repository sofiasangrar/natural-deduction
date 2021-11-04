package natded.UI;

import javafx.scene.control.Button;

public class AddButton extends Button {

    int index;

    AddButton(int index) {
        this.index = index;
        this.setWidth(20);
        this.setHeight(20);
        this.setText("+");

    }
}
