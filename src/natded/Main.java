package natded;

import javafx.application.Application;
import javafx.stage.Stage;
import natded.UI.NDScene;
import natded.UI.Window;

public class Main extends Application {

    private Window ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setFullScreen(true);
        NDScene ui = new NDScene(primaryStage.widthProperty().doubleValue(), primaryStage.heightProperty().doubleValue());
        primaryStage.setScene(ui);
        primaryStage.show();

        NatDedSpace initialState;
        initialState = new NatDedSpace(); //generates random goal
        //UIListener uiLogic = new UIListener(initialState, ui);
        //ui.setListener(uiLogic);
        ui.updateView(initialState.getRoot());

    }
}
