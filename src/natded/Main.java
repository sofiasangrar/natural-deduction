package natded;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import natded.UI.NDScene;
import natded.UI.StartScene;
import natded.UI.UserInterface;
import natded.UI.Window;

public class Main extends Application {

    public static final double DISPLAY_WIDTH = Screen.getPrimary().getBounds().getWidth();
    public static final double DISPLAY_HEIGHT = Screen.getPrimary().getBounds().getHeight();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Proof Checker");
        //primaryStage.setFullScreen(true);
        StartScene start = new StartScene(DISPLAY_WIDTH/3, DISPLAY_HEIGHT/3);
        NDScene ui = new NDScene(primaryStage.widthProperty().doubleValue(), primaryStage.heightProperty().doubleValue());


        Button b = new Button("Start");
        b.setStyle(UserInterface.buttonStyle);
        b.setOnMouseClicked(event -> {
            primaryStage.setScene(ui);
            ui.setGoal(NatDedUtilities.randomGoal());
            primaryStage.setFullScreen(true);

        });
        start.addElement(b);

        Button load = new Button("Load");
        load.setStyle(UserInterface.buttonStyle);
        load.setOnMouseClicked(event -> {
            primaryStage.setScene(ui);
            ui.setProof(NatDedUtilities.load());
            primaryStage.setFullScreen(true);

        });
        start.addElement(load);

        primaryStage.setScene(start);
        primaryStage.show();

    }
}
