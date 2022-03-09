package natded.UI;

import javafx.scene.control.Button;

class ResetButton extends Button {

    ResetButton(UserInterface view) {
        this.setStyle(UserInterface.incorrectDropdownStyle);
        this.setText("Reset");
        setOnMouseClicked(event -> view.reset());
    }
}
