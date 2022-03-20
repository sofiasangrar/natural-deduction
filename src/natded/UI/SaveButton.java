package natded.UI;

import javafx.scene.control.Button;
import natded.StepNode;
import parser.Proof;

public class SaveButton extends Button {

    SaveButton(UserInterface view, boolean quit) {
        this.setStyle(UserInterface.buttonStyle);
        this.setText("Save");
        if (quit){
            this.setText("Save and Quit");
        }
        setOnMouseClicked(event -> {
            StepNode root = view.getTree();
            saveTree(root);
            if (quit){
                System.exit(0);
            }
        });
    }

    public void saveTree(StepNode root){

    }
}
