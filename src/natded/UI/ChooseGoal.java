package natded.UI;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import lexer.tokens.AndToken;
import lexer.tokens.ImpliesToken;
import lexer.tokens.NotToken;
import lexer.tokens.OrToken;
import natded.NatDedUtilities;
import natded.constants.Step;

import java.util.*;

public class ChooseGoal extends ChoiceBox<String> {

    private String style = UserInterface.buttonStyle;
    public static String chooseOwn = "Choose own goal";


    ChooseGoal(){
        super();
        this.setStyle(style);

        ObservableList<String> choices = FXCollections.observableArrayList(NatDedUtilities.deMorgan, NatDedUtilities.notPorQ, chooseOwn);

        this.setItems(choices);

    }

}
