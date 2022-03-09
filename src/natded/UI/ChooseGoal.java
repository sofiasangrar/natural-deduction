package natded.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import static natded.NatDedUtilities.*;

public class ChooseGoal extends ChoiceBox<String> {

    private final String STYLE = UserInterface.buttonStyle;
    static String chooseOwn = "Choose own goal";


    ChooseGoal(){
        super();

        this.setStyle(STYLE);

        ObservableList<String> choices = FXCollections.observableArrayList(proofs);
        choices.add(chooseOwn);

        this.setItems(choices);

    }

}
