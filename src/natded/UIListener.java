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

    public UIListener(UserInterface view) {
        this.view = view;
    }
    public void onFinishedClick() {
            StepNode root = view.getTree();
            Proof p = Proof.parse(root);
            if (p.isValid()) {
                System.out.println("valid!");
            } else {
                view.showError("invalid");
            }
    }


}
