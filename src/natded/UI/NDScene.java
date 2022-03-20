package natded.UI;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import natded.StepNode;


public class NDScene extends Scene {
    static final Color WINDOW_BACKGROUND_COLOR = Color.WHITESMOKE;
    static final String BACKGROUND_COLOR_STRING = "whitesmoke";

    public NDScene(double width, double height){
        super(new UserInterface(), width, height, WINDOW_BACKGROUND_COLOR);
    }

    public UserInterface getUI(){
        return (UserInterface)this.getRoot();
    }

    public void setGoal(String goal) {
        getUI().setGoal(goal);
    }

    public void setProof(StepNode proof) {
        getUI().setProof(proof);
    }

}