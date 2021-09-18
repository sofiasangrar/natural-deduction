package natded.UI;

import javafx.scene.control.Button;
import natded.computationLogic.NatDedUtilities;

import javax.swing.text.Utilities;

public class AddButton extends Button {

    int index;

    AddButton(int index) {

        this.index = index;
        this.setWidth(20);
        this.setHeight(20);
        this.setText("+");
    }
}
