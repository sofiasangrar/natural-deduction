package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import natded.UIListener;


public class NDScene extends Scene {
    public static final Color WINDOW_BACKGROUND_COLOR = Color.WHITESMOKE;

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
