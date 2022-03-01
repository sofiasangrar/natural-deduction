package natded.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import natded.StepNode;
import natded.UIListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UserInterface extends VBox {

    LeafNode root;
    static String buttonStyle = "-fx-background-color: linear-gradient(whitesmoke, darkgray); -fx-border-color: gray; -fx-border-radius: 1";
    static String incorrectDropdownStyle = "-fx-background-color: linear-gradient(whitesmoke, #eb6651); -fx-border-color: gray; -fx-border-radius: 1";
    static Image alert;

    static {
        try {
            alert = new Image(new FileInputStream("src/natded/UI/alert.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private UIListener listener;

    private static final String TITLE = "Natural Deduction";


    public void setGoal(String goal) {
        root.setText(goal);
    }

    public UserInterface(){
        super();
        this.setAlignment(Pos.TOP_CENTER);
        initializeUserInterface();
    }

    public void setListener(UIListener listener) {
        this.listener = listener;
    }

    public StepNode getTree(){
        return root.getTree();
    }


    public void initializeUserInterface() {

        drawTitle();
        drawSpace();
        drawDoneButton();
    }

    private void drawSpace() {

        VBox boardBackground = new VBox();
        VBox.setMargin(boardBackground, new Insets(10.0d, 10.0d, 10.0d, 10.0d));


        VBox.setVgrow(boardBackground, Priority.ALWAYS);
        boardBackground.setAlignment(Pos.CENTER);
        root = new LeafNode();
        boardBackground.setAlignment(Pos.BOTTOM_CENTER);
        boardBackground.getChildren().add(root);
        getChildren().add(boardBackground);
    }


    private void drawTitle() {
        Text title = new Text(TITLE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.NAVY);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        getChildren().add(title);

    }

    private void drawDoneButton() {

        DoneButton button = new DoneButton(this);
        HBox doneBox = new HBox(button);
        HBox.setMargin(doneBox, new Insets(10.0d));


        doneBox.setAlignment(Pos.BOTTOM_RIGHT);
        //button.setOnMouseClicked(e -> listener.onFinishedClick());
        getChildren().add(doneBox);
    }

    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

}
