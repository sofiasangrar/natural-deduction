package natded.UI;

import javafx.scene.control.Button;
import natded.StepNode;
import parser.Proof;

public class ResetButton extends Button {

    ResetButton(UserInterface view) {
        this.setStyle(UserInterface.incorrectDropdownStyle);
        this.setText("Reset");
        setOnMouseClicked(event -> {view.root.deleteChildren(); view.resetErrors(); view.root.resetJustif();
        });
    }
}
