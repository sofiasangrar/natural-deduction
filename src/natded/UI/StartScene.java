package natded.UI;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static natded.Main.DISPLAY_WIDTH;


public class StartScene extends Scene {
    static final Color WINDOW_BACKGROUND_COLOR = Color.WHITESMOKE;
    HBox elements;

    public StartScene(double width, double height){
        super(new StackPane(), width, height, WINDOW_BACKGROUND_COLOR);
        instantiateUI();
    }

    public void instantiateUI(){
        StackPane root = (StackPane)this.getRoot();
        root.setAlignment(Pos.CENTER);

        elements = new HBox();
        elements.setSpacing(50);
        elements.setAlignment(Pos.CENTER);

        Text title = new Text(UserInterface.TITLE);
        title.setY(getHeight()/3);
        title.setFill(Color.NAVY);

        Font titleFont = new Font(DISPLAY_WIDTH/40);
        title.setFont(titleFont);
        VBox vbox = new VBox(title, elements);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);

        root.getChildren().add(vbox);


    }
    public void addElement(Node n){
        elements.getChildren().add(n);
    }


}