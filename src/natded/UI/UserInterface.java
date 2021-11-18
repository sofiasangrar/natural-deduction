package natded.UI;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import natded.UI.logic.ControlLogic;
import natded.computationLogic.NatDedUtilities;
import natded.constants.SpaceState;
import natded.problemDomain.NatDedSpace;
import natded.problemDomain.StepNode;

import java.util.HashMap;

public class UserInterface implements EventHandler<KeyEvent> {
    private final Stage stage;
    private final Group root;

     private HashMap<Integer, StepTextField> indexes;

    private ControlLogic listener;

    //Size of the window
    private static final double WINDOW_Y = 650;
    private static final double WINDOW_X = 1000;
    //distance between window and board
    private static final double BOARD_PADDING = 50;
    private static final double Y_DELTA = 50;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String TITLE = "Natural Deduction";

    /**
     * Stage and Group are JavaFX specific classes for modifying the UI. Think of them as containers of various UI
     * components.
     *
     * A HashMap is a data structure which stores key/value pairs. Rather than creating a member variable for every
     * SudokuTextField object (all 81 of them), I instead store these references within a HashMap, and I retrieve
     * them by using their X and Y Coordinates as a "key" (a unique value used to look something up).
     *
     * @param stage
     */
    public UserInterface(Stage stage) {
            this.stage = stage;
            this.root = new Group();
            this.indexes = new HashMap<>();
            initializeUserInterface();
            }


    public void setListener(ControlLogic listener) {
            this.listener = listener;
            }

    public void updateField(int index, String input) {
        StepTextField field = indexes.get(index);
        field.textProperty().setValue(input);
    }

    public void addChild(int rootIndex) {
    }

    public void updateView(StepNode root) {
        drawDoneButton(this.root);
        double width = WINDOW_X;
        addField(root, width, 0);
        int noChildren = root.getChildren().size();
        if (noChildren > 0) {
            double childWidth = width/noChildren;
            for (int i = 0; i < noChildren; i++) {
                addField(root.getChildren().get(i), childWidth, i);
            }
        }

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

        //Note: Note that UserInterface implements EventHandler<ActionEvent> in the class declaration.
        //By passing "this" (which means the current instance of UserInterface), when an action occurs,
        //it will jump straight to "handle(ActionEvent actionEvent)" down below.
        field.setOnKeyPressed(this);

        indexes.put(node.getIndex(), field);

        field.getAddButton().setLayoutX(startX + width - BOARD_PADDING);
        field.getAddButton().setLayoutY(field.getLayoutY() + BOARD_PADDING/2);

        field.getAddButton().setOnMouseClicked(new EventClick(indexes, listener));
        if (root.getChildren().contains(field)) {
            root.getChildren().remove(field);
            root.getChildren().remove(field.getAddButton());
            root.getChildren().remove(field.getLine());
        }
        root.getChildren().add(field.getAddButton());
        root.getChildren().add(field);
        field.setLine(drawLine(field));
        root.getChildren().add(field.getLine());


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
            drawBackground(root);
            drawSpace(root);
            drawDoneButton(root);
            //drawTextFields(root);
            drawTitle(root);
            stage.show();
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

    /**
     * Background of the primary window
     * @param root
     */
    private void drawBackground(Group root) {
            Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
            scene.setFill(WINDOW_BACKGROUND_COLOR);
            stage.setScene(scene);
            }

    /**
     * Background of the actual sudoku board, offset from the window by BOARD_PADDING
     * @param root
     */
    private void drawSpace(Group root) {
            Rectangle boardBackground = new Rectangle();
            boardBackground.setX(BOARD_PADDING);
            boardBackground.setY(BOARD_PADDING);
            boardBackground.setWidth(WINDOW_X-BOARD_PADDING);
            boardBackground.setHeight(WINDOW_Y-BOARD_PADDING);
            boardBackground.setFill(BOARD_BACKGROUND_COLOR);
            root.getChildren().add(boardBackground);
            }

    private void drawTitle(Group root) {
            Text title = new Text(350, BOARD_PADDING-3, TITLE);
            title.setFill(Color.WHITE);
            Font titleFont = new Font(43);
            title.setFont(titleFont);
            root.getChildren().add(title);
            }

    private void drawDoneButton(Group root) {
        DoneButton button = new DoneButton();
        button.setLayoutX(WINDOW_X-BOARD_PADDING);
        button.setLayoutY(BOARD_PADDING);
        root.getChildren().add(button);
        button.setOnMouseClicked(new DoneClick(listener));
    }

    public void showError(String message) {
            Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            dialog.showAndWait();
            }


    @Override
    public void handle(KeyEvent event) {
        listener.onTextInput(((StepTextField) event.getSource()).getNode(), ((StepTextField) event.getSource()).getText());
        /*
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            String value = event.getText();
            handleInput(value, event.getSource());
        } else if (event.getCode() == KeyCode.BACK_SPACE) {
            handleInput("", event.getSource());
        } else {
            ((TextField)event.getSource()).setText("");
        }
        */
        event.consume();
    }

    /**
     * @param value  expected to be an integer from 0-9, inclusive
     * @param source the textfield object that was clicked.
     */
    private void handleInput(String value, Object source) {

    }
}
