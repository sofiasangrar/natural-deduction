package natded.UI;

import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import lexer.tokens.AndToken;
import lexer.tokens.ImpliesToken;
import lexer.tokens.NotToken;
import lexer.tokens.OrToken;
import natded.constants.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Justification extends ChoiceBox<Pair<String, Step>> {

    private String style = UserInterface.buttonStyle;
    private String wrongStyle = UserInterface.incorrectDropdownStyle;
    private HashMap<Step, Pair<String, Step>> values;

    public void resetStyle() {
        this.setStyle(style);
    }

    public void setIncorrect(){
        this.setStyle(wrongStyle);
    }

    Justification(){
        super();
        this.setStyle(style);

        //list of choices for justification
        values = new HashMap<>();
        values.put(Step.ASSUMPTION, new Pair<>("Ass.", Step.ASSUMPTION));
        values.put(Step.IMP_ELIM, new Pair<>(ImpliesToken.getString()+"-E", Step.IMP_ELIM));
        values.put(Step.IMP_INTRO, new Pair<>(ImpliesToken.getString()+"-I", Step.IMP_INTRO));
        values.put(Step.AND_ELIM, new Pair<>(AndToken.getString()+"-E", Step.AND_ELIM));
        values.put(Step.AND_INTRO, new Pair<>(AndToken.getString()+"-I", Step.AND_INTRO));
        values.put(Step.OR_ELIM, new Pair<>(OrToken.getString()+"-E", Step.OR_ELIM));
        values.put(Step.OR_INTRO, new Pair<>(OrToken.getString()+"-I", Step.OR_INTRO));
        values.put(Step.NEG_ELIM, new Pair<>(NotToken.getString()+"-E", Step.NEG_ELIM));
        values.put( Step.NEG_INTRO, new Pair<>(NotToken.getString()+"-I", Step.NEG_INTRO));
        values.put(Step.FALSE_ELIM, new Pair<>("F-E", Step.FALSE_ELIM));
        //values.put(new Pair<>("F-I", Step.FALSE_INTRO));
        values.put(Step.TRUE_INTRO, new Pair<>("T-I", Step.TRUE_INTRO));
        values.put( Step.EXCL_MIDDLE, new Pair<>("LEM", Step.EXCL_MIDDLE));

        this.setConverter( new StringConverter<Pair<String,Step>>() {
            @Override
            public String toString(Pair<String, Step> pair) {
                return pair.getKey();
            }

            @Override
            public Pair<String, Step> fromString(String string) {
                return values.get(Step.valueOf(string));
            }
        });

        this.getItems().addAll( values.values() );
    }

    public void set(Step step){
        if (step==Step.UNASSIGNED){
            this.getSelectionModel().clearSelection();
        } else {
            this.getSelectionModel().select(values.get(step));
        }
    }

}
