package natded.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ErrorSymbol extends ImageView {

    public ErrorSymbol() {
        super(UserInterface.alert);
        setPreserveRatio(true);
        setOnMouseEntered((e)->{
                    this.setStyle("-fx-opacity: 0.5");
                }
        );
        setOnMouseExited((e)->{
                    this.setStyle("-fx-opacity: 1");
                }
        );
    }

    public void animate(){
        // Defining the Durations of animations
        Duration start = Duration.ZERO;
        Duration mid = Duration.seconds(0.25);
        Duration end = Duration.seconds(0.5);

        //Creating the keyFrames to use in timeline
        double init = getFitWidth();
        KeyValue startValueW = new KeyValue(scaleXProperty(),1);
        KeyValue startValueH = new KeyValue(scaleYProperty(),1);
        KeyFrame startFrame = new KeyFrame(start, startValueW, startValueH);
        KeyValue midValueW = new KeyValue(scaleXProperty(), 1.5);
        KeyValue midValueH = new KeyValue(scaleYProperty(), 1.5);
        KeyFrame midFrame = new KeyFrame(mid, midValueW, midValueH);
        KeyFrame endFrame = new KeyFrame(end, startValueH, startValueW);
// Creating a Timeline using above values
        Timeline t1 = new Timeline(startFrame, midFrame, endFrame);
// Setting the cycle count of animation
        t1.setCycleCount(1);
        //t1.setCycleCount(t1.INDEFINITE);
//Setting the auto reverse property of animation
        t1.setAutoReverse(true);
// Running the animation using play() method
        t1.play();
    }
}
