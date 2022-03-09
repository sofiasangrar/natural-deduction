package natded.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ErrorSymbol extends ImageView {

    ErrorSymbol() {
        super(UserInterface.alert);
        setPreserveRatio(true);

        //show different styles when mouse is hovering over
        setOnMouseEntered((e)->{
                    this.setStyle("-fx-opacity: 0.5");
                }
        );
        setOnMouseExited((e)->{
                    this.setStyle("-fx-opacity: 1");
                }
        );
    }

    /**
     * a scaling animation to draw attention
     */
    public void animate(){

        Duration start = Duration.ZERO;
        Duration mid = Duration.seconds(0.25);
        Duration end = Duration.seconds(0.5);

        //at start, scale is normal
        KeyValue startValueW = new KeyValue(scaleXProperty(),1);
        KeyValue startValueH = new KeyValue(scaleYProperty(),1);
        KeyFrame startFrame = new KeyFrame(start, startValueW, startValueH);

        //scales up to 1.5
        KeyValue midValueW = new KeyValue(scaleXProperty(), 1.5);
        KeyValue midValueH = new KeyValue(scaleYProperty(), 1.5);
        KeyFrame midFrame = new KeyFrame(mid, midValueW, midValueH);

        //scale back to regular size at the end
        KeyFrame endFrame = new KeyFrame(end, startValueH, startValueW);

        Timeline t1 = new Timeline(startFrame, midFrame, endFrame);
        t1.setCycleCount(1);
        t1.setAutoReverse(true);
        t1.play();
    }
}
