package natded.UI;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import natded.StepNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static natded.Main.DISPLAY_HEIGHT;
import static natded.Main.DISPLAY_WIDTH;

public class UserInterface extends VBox {

    LeafNode root;
    private static final String lightGrad = "whitesmoke";
    private static final String borderColor = "gray";
    private static final int borderRadius = 1;
    static String buttonStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", darkgray); -fx-border-color: " + borderColor + "; -fx-border-radius: " + borderRadius;
    static String incorrectDropdownStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", #eb6651); -fx-border-color: "+ borderColor +"; -fx-border-radius: " + borderRadius;
    static Image alert;
    private static Image[] ruleImages = new Image[13];

    static {
        try {
            alert = new Image(new FileInputStream("src/natded/UI/images/alert.png"));
            ruleImages[0] = new Image(new FileInputStream("src/natded/UI/images/andIntro.png"));
            ruleImages[1] = new Image(new FileInputStream("src/natded/UI/images/orIntro.png"));
            ruleImages[2] = new Image(new FileInputStream("src/natded/UI/images/impIntro.png"));
            ruleImages[3] = new Image(new FileInputStream("src/natded/UI/images/Falseintro.png"));
            ruleImages[4] = new Image(new FileInputStream("src/natded/UI/images/notIntro.png"));
            ruleImages[5] = new Image(new FileInputStream("src/natded/UI/images/andElim.png"));
            ruleImages[6] = new Image(new FileInputStream("src/natded/UI/images/orElim.png"));
            ruleImages[7] = new Image(new FileInputStream("src/natded/UI/images/impElim.png"));
            ruleImages[8] = new Image(new FileInputStream("src/natded/UI/images/falseelim.png"));
            ruleImages[9] = new Image(new FileInputStream("src/natded/UI/images/notElim.png"));
            ruleImages[10] = new Image(new FileInputStream("src/natded/UI/images/LEM.png"));
            ruleImages[11] = new Image(new FileInputStream("src/natded/UI/images/ass.png"));
            ruleImages[12] = new Image(new FileInputStream("src/natded/UI/images/trueintro.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String TITLE = "Natural Deduction";


    void setGoal(String goal) {
        root.setText(goal);
    }

    UserInterface(){
        super();
        this.setAlignment(Pos.TOP_CENTER);
        this.setBackground(new Background(new BackgroundFill(NDScene.WINDOW_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        initializeUserInterface();
    }


    public StepNode getTree(){
        return root.getTree();
    }


    private void initializeUserInterface() {

        drawTitle();
        drawRules();
        drawHelp();
        drawSpace();
        drawDoneButton();
    }

    private void drawSpace() {
        VBox boardBackground = new VBox();
        //VBox.setMargin(boardBackground, new Insets(10.0d, 10.0d, 10.0d, 10.0d));
        VBox.setVgrow(boardBackground, Priority.ALWAYS);
        boardBackground.setAlignment(Pos.CENTER);
        HBox wrapper = new HBox();
        wrapper.setAlignment(Pos.CENTER);
        root = new LeafNode();
        boardBackground.setAlignment(Pos.BOTTOM_CENTER);
        wrapper.getChildren().add(root);
        boardBackground.getChildren().add(wrapper);
        getChildren().add(boardBackground);

    }


    private void drawTitle() {
        Text title = new Text(TITLE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.NAVY);

        Font titleFont = new Font(DISPLAY_WIDTH/40);
        title.setFont(titleFont);
        getChildren().add(title);

    }

    private void drawRules(){
        VBox box = new VBox ();
        GridPane g = new GridPane();
        g.setAlignment(Pos.CENTER);
        GridPane.setHalignment(g, HPos.CENTER);
        g.setVgap(DISPLAY_HEIGHT/200);
        int noColumns = 5;
        for (int i = 0; i < ruleImages.length; i++) {
            ImageView view = new ImageView(ruleImages[i]);
            view.setPreserveRatio(true);
            view.setFitHeight(50);
            StackPane wrapper = new StackPane(view);
            g.add(wrapper, i % noColumns, i/noColumns);
        }
        TitledPane tp = new TitledPane("Natural Deduction Rules", g);
        tp.setAlignment(Pos.CENTER);
        tp.setExpanded(false);
        box.getChildren().add(tp);
        getChildren().add(box);
    }

    private void drawHelp(){
        VBox box = new VBox ();
        TitledPane tp = new TitledPane("Instructions", new Text("HELP!"));
        tp.setAlignment(Pos.CENTER);
        tp.setExpanded(false);
        box.getChildren().add(tp);
        getChildren().add(box);
    }

    private void drawDoneButton() {

        DoneButton button = new DoneButton(this);
        HBox doneBox = new HBox(button);
        doneBox.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(doneBox);
    }


}
