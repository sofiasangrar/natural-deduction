package natded.UI;

import javafx.scene.control.ChoiceBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import lexer.tokens.AndToken;
import lexer.tokens.ImpliesToken;
import lexer.tokens.NotToken;
import lexer.tokens.OrToken;
import natded.constants.Step;

import java.util.ArrayList;
import java.util.List;

public class Justification extends ChoiceBox<Pair<String, Step>> {

    private LeafNode parent;
    private String style = UserInterface.buttonStyle;
    private String wrongStyle = UserInterface.incorrectDropdownStyle;

    public void resetStyle() {
        this.setStyle(style);
    }

    public void setIncorrect(){
        this.setStyle(wrongStyle);
    }

    Justification(LeafNode parent){
        super();
        this.setStyle(style);
        this.parent = parent;
        this.setMaxSize(30,20);

        List<Pair<String, Step>> choices = new ArrayList<>();
        choices.add(new Pair<>("Ass.", Step.ASSUMPTION));
        choices.add(new Pair<>(ImpliesToken.getString()+"-E", Step.IMP_ELIM));
        choices.add(new Pair<>(ImpliesToken.getString()+"-I", Step.IMP_INTRO));
        choices.add(new Pair<>(AndToken.getString()+"-E", Step.AND_ELIM));
        choices.add(new Pair<>(AndToken.getString()+"-I", Step.AND_INTRO));
        choices.add(new Pair<>(OrToken.getString()+"-E", Step.OR_ELIM));
        choices.add(new Pair<>(OrToken.getString()+"-I", Step.OR_INTRO));
        choices.add(new Pair<>(NotToken.getString()+"-E", Step.NEG_ELIM));
        choices.add(new Pair<>(NotToken.getString()+"-I", Step.NEG_INTRO));
        choices.add(new Pair<>("F-E", Step.FALSE_ELIM));
        choices.add(new Pair<>("T-I", Step.TRUE_INTRO));
        choices.add(new Pair<>("LEM", Step.EXCL_MIDDLE));

        this.setConverter( new StringConverter<Pair<String,Step>>() {
            @Override
            public String toString(Pair<String, Step> pair) {
                return pair.getKey();
            }

            @Override
            public Pair<String, Step> fromString(String string) {
                return null;
            }
        });

        this.getItems().addAll( choices );
    }

}
