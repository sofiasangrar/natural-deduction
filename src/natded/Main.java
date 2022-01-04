package natded;

import javafx.application.Application;
import javafx.stage.Stage;
import natded.UI.UserInterface;

public class Main extends Application {

    private UserInterface ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ui = new UserInterface(primaryStage);
        NatDedSpace initialState;
        initialState = new NatDedSpace(); //generates random goal

        UIListener uiLogic = new UIListener(initialState, ui);
        ui.setListener(uiLogic);
        ui.updateView(initialState.getRoot());

    }
}
