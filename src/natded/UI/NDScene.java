package natded.UI;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import natded.StepNode;
import natded.UIListener;

import java.util.HashMap;

public class NDScene extends Scene {

    private HashMap<Integer, StepTextField> indexes;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.WHITE;

    public NDScene(double width, double height){
        super(new UserInterface(), width, height, WINDOW_BACKGROUND_COLOR);
        this.indexes = new HashMap<>();
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
