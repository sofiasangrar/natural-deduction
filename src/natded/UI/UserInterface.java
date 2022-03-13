package natded.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lexer.tokens.*;
import natded.StepNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static javafx.scene.layout.Priority.ALWAYS;
import static natded.Main.DISPLAY_HEIGHT;
import static natded.Main.DISPLAY_WIDTH;

public class UserInterface extends StackPane {

    VBox space = new VBox();
    LeafNode root;
    private static final String lightGrad = "whitesmoke";
    private static final String borderColor = "gray";
    private static final int borderRadius = 1;
    static String buttonStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", darkgray); -fx-border-color: " + borderColor + "; -fx-border-radius: " + borderRadius;
    static String incorrectDropdownStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", #eb6651); -fx-border-color: "+ borderColor +"; -fx-border-radius: " + borderRadius;
    static Image alert;
    private static Image tick;
    private static Image[] instructionImages = new Image[3];
    private static Image[] ruleImages = new Image[13];

    //get images
    static {
        try {
            tick = new Image(new FileInputStream("src/natded/UI/images/tick.png"));
            alert = new Image(new FileInputStream("src/natded/UI/images/alert.png"));
            instructionImages[0] = new Image(new FileInputStream("src/natded/UI/images/1v2.png"));
            instructionImages[1] = new Image(new FileInputStream("src/natded/UI/images/2.png"));
            instructionImages[2] = new Image(new FileInputStream("src/natded/UI/images/3.png"));
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

    /**
     * set the goal of the root
     * @param goal string to use in root
     */
    void setGoal(String goal) {
        root.setText(goal);
    }

    UserInterface(){
        super();
        this.getChildren().add(space);
        space.setAlignment(Pos.TOP_CENTER);
        space.setBackground(new Background(new BackgroundFill(NDScene.WINDOW_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        initializeUserInterface();
    }

    /**
     * get proof tree
     * @return proof tree
     */
    StepNode getTree(){
        return root.getTree();
    }


    /**
     * called on startup, draws all elements
     */
    private void initializeUserInterface() {
        drawTitle();
        drawTop();
        drawSpace();
        drawBottom();
    }

    /**
     * draw proof writing area
     */
    private void drawSpace() {
        //elements grow from the bottom up
        VBox boardBackground = new VBox();
        VBox.setVgrow(boardBackground, ALWAYS);
        boardBackground.setAlignment(Pos.BOTTOM_CENTER);


        //wrapper for proof area to contain its width
        HBox wrapper = new HBox();
        wrapper.setAlignment(Pos.CENTER);

        //create starting root
        root = new LeafNode();
        root.setEditable(false);
        root.setFieldMinWidth(DISPLAY_WIDTH/10);

        wrapper.getChildren().add(root);
        boardBackground.getChildren().add(wrapper);
        space.getChildren().add(boardBackground);

    }

    /**
     * draw title at top of screen
     */
    private void drawTitle() {
        Text title = new Text(TITLE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.NAVY);

        Font titleFont = new Font(DISPLAY_WIDTH/40);
        title.setFont(titleFont);
        space.getChildren().add(title);
    }

    /**
     * draw top elements, meanig the natural deduction rules and the how-to instructions
     */
    private void drawTop(){
        VBox box = new VBox ();
        box.getChildren().addAll(getRules(), getInstructions());
        space.getChildren().add(box);
    }

    /**
     * get element containing natural deduction rules
     * @return pane containing rules
     */
    private TitledPane getRules(){
        GridPane g = new GridPane();
        g.setAlignment(Pos.CENTER);
        GridPane.setHalignment(g, HPos.CENTER);
        g.setVgap(DISPLAY_HEIGHT/200);

        //fill images one by onw from left to right into the grid
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
        return tp;
    }

    /**
     * create instructions pane
     * @return pane containign instructions
     */
    private TitledPane getInstructions(){
        //the proportion of the display width of each image
        int[] imgProps = {3, 4, 4};

        //stack rules and keyboard instructions vertically
        VBox box = new VBox ();

        GridPane g = new GridPane();
        g.setAlignment(Pos.CENTER);
        GridPane.setHalignment(g, HPos.CENTER);
        g.setVgap(DISPLAY_HEIGHT/200);

        double totalWidth = 0;

        for (int i = 0; i < instructionImages.length; i++) {
            ImageView imView = new ImageView(instructionImages[i]);
            imView.setPreserveRatio(true);
            double width = DISPLAY_WIDTH/imgProps[i];
            totalWidth+=width;
            imView.setFitWidth(width);
            g.add(new StackPane(imView), i,0);

        }

        //spread images equally, and as far as the space allows for
        double hGap = (DISPLAY_WIDTH - totalWidth)/(imgProps.length+1);
        g.setHgap(hGap);

        box.getChildren().add(g);

        //draw keyboard usage instructions
        Label label = new Label("Keyboard Usage:");
        label.setFont(new Font(18));
        label.setPadding(new Insets(DISPLAY_HEIGHT/50, 0.0,DISPLAY_HEIGHT/200,0.0));
        box.getChildren().add(label);
        GridPane textInstr = new GridPane();
        textInstr.setVgap(DISPLAY_HEIGHT/200);
        textInstr.setHgap(DISPLAY_WIDTH/50);

        textInstr.add(new Text("For conjunction:"), 0, 0);
        textInstr.add(new Text(getKeys(OrToken.class)), 0, 1);

        textInstr.add(new Text("For disjunction:"), 1, 0);
        textInstr.add(new Text(getKeys(OrToken.class)), 1, 1);

        textInstr.add(new Text("For implication:"), 2, 0);
        textInstr.add(new Text(getKeys(ImpliesToken.class)), 2, 1);

        textInstr.add(new Text("For negation:"), 3, 0);
        textInstr.add(new Text(getKeys(NotToken.class)), 3, 1);

        textInstr.add(new Text("For empty set of assumptions:"), 4, 0);
        textInstr.add(new Text(getKeys(EmptyToken.class)), 4, 1);

        textInstr.add(new Text("For turnstile:"), 5, 0);
        textInstr.add(new Text(getKeys(TurnstileToken.class)), 5, 1);

        box.getChildren().add(textInstr);

        TitledPane tp = new TitledPane("Instructions", box);
        tp.setAlignment(Pos.CENTER);
        tp.setExpanded(false);

        return tp;
    }

    /**
     * draw elements at bottom of page, including reset button, choose goal choicebox, and check button
     */
    private void drawBottom(){
        //elements are aligned horizontally
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(0.0, DISPLAY_HEIGHT/50, DISPLAY_HEIGHT/50, DISPLAY_HEIGHT/50));
        bottomBar.getChildren().add(new ResetButton(this));

        Label label = new Label("Choose goal:");
        label.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));

        bottomBar.getChildren().addAll(label, getChooseBox(), getCheckButton());
        space.getChildren().add(bottomBar);
    }

    /**
     * get a choicebox to draw
     * @return chociebox
     */
    private ChooseGoal getChooseBox(){
        ChooseGoal c = new ChooseGoal();

        c.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            root.setText(null);
            resetErrors();
            removeValid();
            if (newValue.equals(ChooseGoal.chooseOwn)) {
                root.setEditable(true);
                root.setPlaceHolder();
            } else {
                setGoal(newValue);
                root.setEditable(false);
            }
        });

        return c;
    }

    /**
     * get a check button ot draw
     * @return styled check button
     */
    private HBox getCheckButton(){
        DoneButton button = new DoneButton(this);
        HBox doneBox = new HBox(button);
        doneBox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(doneBox, ALWAYS);
        return doneBox;
    }

    /**
     * remove all errors in the tree
     */
    public void resetErrors(){
        resetErrors(root);
    }

    /**
     * recursively remove errors in tree and subtrees, stating from a specified node
     * @param node tree node where error formatting removal should start from
     */
    private void resetErrors(LeafNode node) {
        node.resetError();
        for (LeafNode child: node.getChildNodes()){
            resetErrors(child);
        }
    }

    /**
     * get formatted list of keys that can be used to represent a particular token
     * @param clazz token class of symbol
     * @return comma-delimited list of symbols
     */
    private String getKeys(Class<? extends Token> clazz){
        try {
            String text = (String)clazz.getMethod("getString").invoke(null);
            String[] keys = (String[])clazz.getMethod("getKeys").invoke(null);
            for (int i = 0; i < keys.length; i++){
                text += ", " + keys[i];
            }
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";


    }

    /**
     * remove everything in screen, like error messages, except for goal
     */
    void reset(){
        root.deleteChildren();
        resetErrors();
        root.resetJustif();
        removeValid();
    }

    /**
     * remove the valid proof symbol
     */
    public void removeValid(){
        if (this.getChildren().size()==2) {
            this.getChildren().remove(1);
        }
    }

    /**
     * show the valid proof symbol, including animation
     */
    public void showValid(){
        ImageView tickView = new ImageView(tick);
        tickView.setPreserveRatio(true);
        tickView.setFitWidth(DISPLAY_WIDTH/15);
        tickView.setTranslateX(DISPLAY_WIDTH/3);
        getChildren().add(tickView);
        animate(tickView);
    }

    /**
     * a scaling animation to draw attention
     */
    public static void animate(Node node) {
        Duration start = Duration.ZERO;
        Duration mid = Duration.seconds(0.25);
        Duration end = Duration.seconds(0.5);

        //at start, scale is normal
        KeyValue startValueW = new KeyValue(node.scaleXProperty(), 1);
        KeyValue startValueH = new KeyValue(node.scaleYProperty(), 1);
        KeyFrame startFrame = new KeyFrame(start, startValueW, startValueH);

        //scales up to 1.5
        KeyValue midValueW = new KeyValue(node.scaleXProperty(), 1.5);
        KeyValue midValueH = new KeyValue(node.scaleYProperty(), 1.5);
        KeyFrame midFrame = new KeyFrame(mid, midValueW, midValueH);

        //scale back to regular size at the end
        KeyFrame endFrame = new KeyFrame(end, startValueH, startValueW);

        Timeline t1 = new Timeline(startFrame, midFrame, endFrame);
        t1.setCycleCount(1);
        t1.setAutoReverse(true);
        t1.play();
    }


}
