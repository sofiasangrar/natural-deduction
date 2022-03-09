package natded.UI;

import javafx.scene.image.ImageView;

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

}
