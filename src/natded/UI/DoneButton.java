package natded.UI;

import javafx.scene.control.Button;
import natded.StepNode;
import parser.Proof;

public class DoneButton extends Button {

    DoneButton(UserInterface view) {
        this.setStyle(UserInterface.buttonStyle);
        this.setText("Check");
        setOnMouseClicked(event -> {StepNode root = view.getTree();
        view.removeValid();
        Proof p = Proof.parse(root);
        if (p.isValid()) {
            view.showValid();
        }});
    }
}
