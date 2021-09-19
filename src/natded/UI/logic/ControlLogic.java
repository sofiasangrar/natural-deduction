package natded.UI.logic;

import natded.UI.UserInterface;
import natded.computationLogic.Logic;
import natded.constants.Messages;
import natded.constants.SpaceState;
import natded.problemDomain.NatDedSpace;
import natded.problemDomain.StepNode;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

public class ControlLogic {

    //Remember, this could be the real UserInterface, or it could be a test class
    //which implements the same interface!
    private UserInterface view;
    private NatDedSpace space;

    public ControlLogic(NatDedSpace space, UserInterface view) {
        this.space = space;
        this.view = view;
    }

    public void onAddChildClick(StepNode node) {
        //try {
            node.addChild(new StepNode(node, "", null, new ArrayList<>()));
            //NatDedSpace spaceData = storage.getSpaceData();
            //StepNode newTreeState = space.getCopyOfTreeData();
            //todo -
            // hashmap.get(index).setInput(input);

            //space = new NatDedSpace(newTreeState);

            //storage.updateSpaceData(spaceData);

            //either way, update the view
            view.updateView(space.getRoot());

        //} catch (IOException e) {
          //  e.printStackTrace();
            //view.showError(Messages.ERROR);
        //}

    }

    public void onTextInput(StepNode node, String input) {
        //try {
            //NatDedSpace spaceData = storage.getSpaceData();
            //StepNode newTreeState = spaceData.getCopyOfTreeData();
            //todo -
            // hashmap.get(index).setInput(input);

            node.setInput(input);

            //spaceData = new NatDedSpace(newTreeState);

            //storage.updateSpaceData(spaceData);

            //either way, update the view
            //view.updateField(node.getIndex(), input);

        //} catch (IOException e) {
          //  e.printStackTrace();
            //view.showError(Messages.ERROR);
        //}
    }

    public void onFinishedClick() {
      //  try {
           // NatDedSpace spaceData = storage.getSpaceData();
            SpaceState s = Logic.checkForCompletion(space.getRoot());
            if (s==SpaceState.VALID) {
                view.showDialog(Messages.COMPLETE);
            } else {
                view.showDialog(Messages.INVALID);
            }
        //} catch (IOException e) {
        //e.printStackTrace();
        //view.showError(Messages.ERROR);
    //}
    }
}
