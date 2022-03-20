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
import natded.NatDedUtilities;
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
    public static String buttonStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", darkgray); -fx-border-color: " + borderColor + "; -fx-border-radius: " + borderRadius;
    static String greenButtonStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", lightgreen); -fx-border-color: " + borderColor + "; -fx-border-radius: " + borderRadius;
    static String incorrectDropdownStyle = "-fx-background-color: linear-gradient("+ lightGrad + ", #eb6651); -fx-border-color: "+ borderColor +"; -fx-border-radius: " + borderRadius;
    static Image alert;
    private static Image tick;
    private static Image[] instructionImages = new Image[3];
    private static Image[] ruleImages = new Image[12];
    private static Image deMorgan;
    private static String imgDir = "src/natded/UI/images/";
    //get images
    static {
        try {
            tick = new Image(new FileInputStream(imgDir + "tick.png"));
            alert = new Image(new FileInputStream(imgDir + "alert.png"));
            instructionImages[0] = new Image(new FileInputStream(imgDir + "1v3.png"));
            instructionImages[1] = new Image(new FileInputStream(imgDir + "2.png"));
            instructionImages[2] = new Image(new FileInputStream(imgDir + "3.png"));
            ruleImages[0] = new Image(new FileInputStream(imgDir + "andIntro.png"));
            ruleImages[1] = new Image(new FileInputStream(imgDir + "orIntro.png"));
            ruleImages[2] = new Image(new FileInputStream(imgDir + "impIntro.png"));
            ruleImages[3] = new Image(new FileInputStream(imgDir + "trueintro.png"));
            ruleImages[4] = new Image(new FileInputStream(imgDir + "notIntro.png"));
            ruleImages[5] = new Image(new FileInputStream(imgDir + "andElim.png"));
            ruleImages[6] = new Image(new FileInputStream(imgDir + "orElim.png"));
            ruleImages[7] = new Image(new FileInputStream(imgDir + "impElim.png"));
            ruleImages[8] = new Image(new FileInputStream(imgDir + "falseelim.png"));
            ruleImages[9] = new Image(new FileInputStream(imgDir + "notElim.png"));
            ruleImages[10] = new Image(new FileInputStream(imgDir + "LEM.png"));
            ruleImages[11] = new Image(new FileInputStream(imgDir + "ass.png"));
            deMorgan = new Image(new FileInputStream(imgDir + "deMorganSmall.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final String TITLE = "Natural Deduction";

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
        box.getChildren().addAll(getRules(), getInstructions(), getExample());
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
        g.setVgap(DISPLAY_WIDTH/50);

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
        Label label = new Label("Keyboard Usage:");;
        label.setStyle("-fx-text-fill: #2f2f2f");
        label.setFont(new Font(18));
        label.setPadding(new Insets(DISPLAY_HEIGHT/50, 0.0,DISPLAY_HEIGHT/200,0.0));
        box.getChildren().add(label);
        GridPane textInstr = new GridPane();
        textInstr.setVgap(DISPLAY_HEIGHT/200);
        textInstr.setHgap(DISPLAY_WIDTH/50);

        Label[] instrs = new Label[12];
        instrs[0]=new Label("For conjunction:");
        instrs[1] = new Label("For disjunction:");
        instrs[2]=new Label("For implication:");
        instrs[3] = new Label("For negation:");
        instrs[4]=new Label("For empty set of assumptions:");
        instrs[5] = new Label("For turnstile:");
        instrs[6]=new Label(getKeys(AndToken.class));
        instrs[7] = new Label(getKeys(OrToken.class));
        instrs[8] =new Label(getKeys(ImpliesToken.class));
        instrs[9]=new Label(getKeys(NotToken.class));
        instrs[10]=new Label(getKeys(EmptyToken.class));
        instrs[11] = new Label(getKeys(TurnstileToken.class));

        for (int i = 0; i < instrs.length; i++){
            instrs[i].setStyle("-fx-text-fill: gray");
            textInstr.add(instrs[i], i % (int)(instrs.length/2), (int)(i/(instrs.length/2)));
        }

        box.getChildren().add(textInstr);

        TitledPane tp = new TitledPane("Instructions", box);
        tp.setAlignment(Pos.CENTER);
        tp.setExpanded(false);

        return tp;
    }

    /**
     * create example pane
     * @return pane containing example
     */
    private TitledPane getExample(){
        ImageView imView = new ImageView(deMorgan);
        imView.setPreserveRatio(true);
        imView.setFitWidth(DISPLAY_WIDTH - DISPLAY_WIDTH/5);
        TitledPane tp = new TitledPane("Example", imView);
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

        bottomBar.getChildren().addAll(label, getChooseBox(), getButtons());
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
     * get save and check buttons
     * @return styled check button
     */
    private HBox getButtons(){
        DoneButton button = new DoneButton(this);
        SaveButton s = new SaveButton(this, false);
        SaveButton sq = new SaveButton(this, true);
        HBox doneBox = new HBox(s, sq, button);
        doneBox.setSpacing(5);
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

    /**
     * load proof to scene
     * @param proof proof to load
     */
    public void setProof(StepNode proof){
        root.setText(proof.getInput());
        root.setJustif(proof.getStep());

        for (StepNode child : proof.getChildren()){
            root.addChild(constructProof(child));
        }
    }

    /**
     * recursive step to get proof
     * @param child node being processed
     * @return ui element for current node
     */
    private LeafNode constructProof(StepNode child){
            LeafNode n = new LeafNode(null);
            n.setText(child.getInput());
            n.setJustif(child.getStep());
            for (StepNode childNode : child.getChildren()){
                n.addChild(constructProof(childNode));
            }
            return n;

    }


}
