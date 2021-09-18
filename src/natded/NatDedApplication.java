package natded;

import javafx.application.Application;
import javafx.stage.Stage;
import natded.UI.IUserInterface;
import natded.UI.UserInterfaceImpl;
import natded.buildLogic.NatDedBuildLogic;

import java.io.IOException;


public class NatDedApplication extends Application {
    private IUserInterface.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws IOException {
        uiImpl = new UserInterfaceImpl(primaryStage);

        try {
            NatDedBuildLogic.build(uiImpl);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}