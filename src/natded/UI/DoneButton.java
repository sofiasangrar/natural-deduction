package natded.UI;

import javafx.scene.control.Button;
import natded.StepNode;
import parser.Proof;

public class DoneButton extends Button {

    DoneButton(UserInterface view) {
        this.setStyle(UserInterface.buttonStyle);
        this.setWidth(20);
        this.setHeight(20);
        this.setText("Check");
        setOnMouseClicked(event -> {StepNode root = view.getTree();
        Proof p = Proof.parse(root);
        if (p.isValid()) {
            System.out.println("valid!");
        } else {
            System.out.println("invalid!");
        }});
    }
}
