package natded.UI;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import natded.NatDedUtilities;
import natded.StepNode;
import natded.constants.Step;
import parser.Proof;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveButton extends Button {

    UserInterface view;

    SaveButton(UserInterface view, boolean quit) {
        this.view = view;
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
        String saveString = getStringTree(root, 0);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File saveFile = fileChooser.showSaveDialog(view.getScene().getWindow());

        try {
            FileWriter writer = new FileWriter(saveFile);
            writer.write(saveString);
            writer.close();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nowhere to save file.");
            alert.showAndWait();
        } catch (NullPointerException ignored){

        }
    }

    private String getString(StepNode node){
        return node.getInput() + NatDedUtilities.del + node.getStep() + "\n";
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
