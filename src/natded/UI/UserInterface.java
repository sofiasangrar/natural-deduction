package natded.UI;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import natded.StepNode;
import natded.UIListener;
import sun.tools.jstat.Alignment;

import java.util.HashMap;

public class UserInterface extends VBox {

    private HashMap<Integer, StepTextField> indexes;
    LeafNode root;

    private UIListener listener;

    private static final String TITLE = "Natural Deduction";


    public void setGoal(String goal) {
        root.setText(goal);
    }

    public UserInterface(){
        super();
        //this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        this.indexes = new HashMap<>();
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
        //drawTextFields(root);
        //
        //stage.show();
    }


    /**
     * Helper method for styling a sudoku tile number
     * @param tile
     * @param x
     * @param y
     */
    private void styleField(StepTextField tile, double x, double y, double width, double height) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(height);
        tile.setPrefWidth(width);

        tile.setBackground(Background.EMPTY);
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

        DoneButton button = new DoneButton();
        HBox doneBox = new HBox(button);
        HBox.setMargin(doneBox, new Insets(10.0d));


        doneBox.setAlignment(Pos.BOTTOM_RIGHT);
        button.setOnMouseClicked(e -> listener.onFinishedClick());
        getChildren().add(doneBox);
    }

    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

}
