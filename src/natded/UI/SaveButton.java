package natded.UI;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import natded.StepNode;
import natded.constants.Step;
import parser.Proof;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        File saveFile = new File("src/IO/save.txt");
        String saveString = getStringTree(root, 0);
        try {
            FileWriter writer = new FileWriter(saveFile);
            writer.write(saveString);
            writer.close();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nowhere to save file.");
            alert.showAndWait();
        }
    }

    private String getString(StepNode node){
        return node.getInput() + "," + node.getStep() + "\n";
    }

    private String getStringTree(StepNode node, int level){
        String thisString="";
        for (int i = 0; i < level; i++){
            thisString +="\t";
        }
        thisString += getString(node);
        for (StepNode child : node.getChildren()){
            thisString += getStringTree(child, level + 1);
        }
        return thisString;
    }
}
