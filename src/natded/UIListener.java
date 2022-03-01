package natded;

import natded.UI.UserInterface;
import parser.Proof;

public class UIListener {

    private UserInterface view;

    public UIListener(UserInterface view) {
        this.view = view;
    }

    public void onFinishedClick() {
            StepNode root = view.getTree();
            Proof p = Proof.parse(root);
            if (p.isValid()) {
                System.out.println("valid!");
            } else {
                System.out.println("invalid!");
            }
    }


}
