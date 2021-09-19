package natded;

import javafx.application.Application;
import javafx.stage.Stage;
import natded.UI.UserInterface;
import natded.UI.logic.ControlLogic;
import natded.computationLogic.SpaceGenerator;
import natded.problemDomain.NatDedSpace;

import java.io.IOException;

public class Main extends Application {

    private UserInterface uiImpl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        uiImpl = new UserInterface(primaryStage);
        NatDedSpace initialState;
        initialState = new NatDedSpace(SpaceGenerator.getNewSpace());

        ControlLogic uiLogic = new ControlLogic(initialState, uiImpl);
        uiImpl.setListener(uiLogic);
        uiImpl.updateView(initialState.getRoot());

    }
}
