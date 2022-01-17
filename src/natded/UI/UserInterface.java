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

    //distance between window and board
    private static final double BOARD_PADDING = 50;
    private static final double Y_DELTA = 50;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.WHITE;
    private static final Color BOARD_BACKGROUND_COLOR = Color.WHITE;
    private static final String TITLE = "Natural Deduction";
    int WINDOW_X = 100;
    int WINDOW_Y = 100;


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

    public void updateField(int index, String input) {
        StepTextField field = indexes.get(index);
        field.textProperty().setValue(input);
    }

    public void addChild(int rootIndex) {
    }

    public void updateView(StepNode root) {


        /*
        double width = WINDOW_X;
        addField(root, width, 0);
        int noChildren = root.getChildren().size();
        if (noChildren > 0) {
            double childWidth = width/noChildren;
            for (int i = 0; i < noChildren; i++) {
                addField(root.getChildren().get(i), childWidth, i);
            }
        }
        */

    }


    private void addField(StepNode node, double width, int xOffset){
        StepTextField field;
        if (indexes.containsKey(node.getIndex())) {
            field = indexes.get(node.getIndex());
        } else {
            field = new StepTextField(node);
        }
        field.setText(node.getInput());

        if (node.getIndex() == 0 && node.getInput() != "") {
            field.setStyle("-fx-opacity: 0.8;");
            field.setDisable(true);
        }else {
            field.setStyle("-fx-opacity: 1;");
            field.setDisable(false);
        }

        double startX, startY;
        if (node.getParent()!=null) {
            StepTextField rootField = indexes.get(node.getParent().getIndex());
            startX = rootField.getLayoutX() + width * xOffset;
            startY = rootField.getLayoutY() - Y_DELTA;
        } else {
            startX = BOARD_PADDING;
            startY = WINDOW_Y - BOARD_PADDING;
        }

        //encapsulated style information
        styleField(field, startX, startY - BOARD_PADDING/2, width - 2*BOARD_PADDING, Y_DELTA);

        //Note: Note that Window implements EventHandler<ActionEvent> in the class declaration.
        //By passing "this" (which means the current instance of Window), when an action occurs,
        //it will jump straight to "handle(ActionEvent actionEvent)" down below.
        //field.setOnKeyPressed(this);

        indexes.put(node.getIndex(), field);

        field.getAddButton().setLayoutX(startX + width - BOARD_PADDING);
        field.getAddButton().setLayoutY(field.getLayoutY() + BOARD_PADDING/2);

        field.getAddButton().setOnMouseClicked(new EventClick(indexes, listener));
        if (getChildren().contains(field)) {
            getChildren().remove(field);
            getChildren().remove(field.getAddButton());
            getChildren().remove(field.getLine());
        }
        getChildren().add(field.getAddButton());
        getChildren().add(field);
        field.setLine(drawLine(field));
        getChildren().add(field.getLine());


        int noChildren = node.getChildren().size();
        if (noChildren > 0) {
            double childWidth = width/noChildren;
            for (int i = 0; i < noChildren; i++) {
                addField(node.getChildren().get(i), childWidth, i);
            }
        }

    }

    public void showDialog(String message) {

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


    /**
     * In order to draw the various lines that make up the Sudoku grid, we use a starting x and y offset
     * value (remember, x grows positively from left to right, and y grows positively from top to bottom).
     * Each square is meant to be 64x64 units, so we add that number each time a
     * @param
     */
    //TODO - add NatDed lines

    private Rectangle drawLine(StepTextField field) {
        return getLine(field.getLayoutX() + BOARD_PADDING/2, field.getLayoutY(), 2, field.getWidth() - BOARD_PADDING);
    }


    /**
     * Convenience method to reduce repetitious code.
     *
     * X, Y, Height, Width,
     * @return A Rectangle to specification
     */
    public Rectangle getLine(double x, double y, double height, double width){
        Rectangle line = new Rectangle();

        line.setX(x);
        line.setY(y);

        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;

    }

    private void drawSpace() {

        VBox boardBackground = new VBox();
        VBox.setMargin(boardBackground, new Insets(10.0d, 10.0d, 10.0d, 10.0d));

        //boardBackground.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        VBox.setVgrow(boardBackground, Priority.ALWAYS);
        boardBackground.setAlignment(Pos.CENTER);
        //HBox rootNode = new HBox();
        root = new LeafNode();
        //root.setAlignment(Pos.BOTTOM_CENTER);
        boardBackground.setAlignment(Pos.BOTTOM_CENTER);
        //rootNode.getChildren().add(root);
        //boardBackground.getChildren().add(rootNode);
        boardBackground.getChildren().add(root);
        getChildren().add(boardBackground);
    }

    void drawLeaf(Group parent){

    }

    private void drawTitle() {
        Text title = new Text(TITLE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.NAVY);
        Font titleFont = new Font(43);
        title.setFont(titleFont);

        //HBox titleBox = new HBox(title);
        //titleBox.setAlignment(Pos.CENTER);
        //getChildren().add(titleBox);
        getChildren().add(title);

    }

    private void drawDoneButton() {

        DoneButton button = new DoneButton();
        //addButton.setAlignment(Pos.BOTTOM_RIGHT);
        HBox doneBox = new HBox(button);
        HBox.setMargin(doneBox, new Insets(10.0d));

        //doneBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        doneBox.setAlignment(Pos.BOTTOM_RIGHT);
        button.setOnMouseClicked(e -> listener.onFinishedClick());
        getChildren().add(doneBox);
    }

    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }


    /*
    @Override
    public void handle(KeyEvent event) {
        listener.onTextInput(((StepTextField) event.getSource()).getNode(), ((StepTextField) event.getSource()).getText());
        event.consume();
    }
    */
}
