package natded;

import natded.UI.NDScene;
import natded.UI.UserInterface;
import natded.UI.Window;
import parser.Proof;
import natded.constants.SpaceState;

import java.util.ArrayList;

public class UIListener {

    private UserInterface view;
    private NatDedSpace space;

    public UIListener(NatDedSpace space, UserInterface view) {
        this.space = space;
        this.view = view;
    }

    /*
    public void onAddChildClick(StepNode node) {
            node.addChild(new StepNode(node, "", null, new ArrayList<>()));
            view.updateView(space.getRoot());
    }


    public void onTextInput(StepNode node, String input) {
            node.setInput(input);
    }

*/
    public void onFinishedClick() {
            StepNode root = view.getTree();
            Proof p = Proof.parse(root);
            if (p.isValid()) {
                space.setSpaceState(SpaceState.VALID);
            } else {
                space.setSpaceState(SpaceState.INVALID);
            }
            if (space.getSpaceState()==SpaceState.VALID) {
                view.showDialog("complete");
            } else {
                view.showDialog("invalid");
            }
    }


}
