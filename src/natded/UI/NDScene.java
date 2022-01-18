package natded.UI;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import natded.UIListener;

import java.util.HashMap;

public class NDScene extends Scene {
    private static final Color WINDOW_BACKGROUND_COLOR = Color.WHITE;

    public NDScene(double width, double height){
        super(new UserInterface(), width, height, WINDOW_BACKGROUND_COLOR);
    }

    public UserInterface getUI(){
        return (UserInterface)this.getRoot();
    }

    public void setGoal(String goal) {
        getUI().setGoal(goal);
    }

    public void setListener(UIListener listener) {
        getUI().setListener(listener);
    }
}
