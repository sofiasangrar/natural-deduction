package natded.buildLogic;

import natded.UI.IUserInterface;
import natded.UI.logic.ControlLogic;
import natded.computationLogic.Logic;
import natded.persistence.LocalStorageImpl;
import natded.problemDomain.IStorage;
import natded.problemDomain.NatDedSpace;

import java.io.IOException;

public class NatDedBuildLogic {

    public static void build(IUserInterface.View userInterface) throws IOException {
        NatDedSpace initialState;
        //IStorage storage = new LocalStorageImpl();
        initialState = Logic.getNewSpace();

        IUserInterface.EventListener uiLogic = new ControlLogic(initialState, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateView(initialState.getRoot());
    }

}
