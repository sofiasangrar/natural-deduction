package natded.UI;

import javafx.scene.Scene;
import javafx.scene.paint.Color;


public class NDScene extends Scene {
    static final Color WINDOW_BACKGROUND_COLOR = Color.WHITESMOKE;

    public NDScene(double width, double height){
        super(new UserInterface(), width, height, WINDOW_BACKGROUND_COLOR);
    }

    public UserInterface getUI(){
        return (UserInterface)this.getRoot();
    }

    public void setGoal(String goal) {
        getUI().setGoal(goal);
    }

}
