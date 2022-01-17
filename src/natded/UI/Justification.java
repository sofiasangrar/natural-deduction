package natded.UI;

import javafx.scene.control.ChoiceBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import natded.constants.Step;

import java.util.ArrayList;
import java.util.List;

public class Justification extends ChoiceBox<Pair<String, Step>> {

    LeafNode parent;

    Justification(LeafNode parent){
        super();
        this.parent = parent;
        List<Pair<String, Step>> choices = new ArrayList<>();
        choices.add(new Pair("Asseumption", Step.ASSUMPTION));
        choices.add(new Pair("Implication-Elimination", Step.IMP_ELIM));
        choices.add(new Pair("Implication-Introduction", Step.IMP_INTRO));
        choices.add(new Pair("And-Elimination", Step.AND_ELIM));
        choices.add(new Pair("And-Introduction", Step.AND_INTRO));
        choices.add(new Pair("Or-Elimination", Step.OR_ELIM));
        choices.add(new Pair("Or-Introduction", Step.OR_INTRO));
        choices.add(new Pair("Negation-Elimination", Step.NEG_ELIM));
        choices.add(new Pair("Negation-Introduction", Step.NEG_INTRO));
        choices.add(new Pair("False-Elimination", Step.FALSE_ELIM));
        choices.add(new Pair("True-Introduction", Step.TRUE_INTRO));
        choices.add(new Pair("Excluded Middle", Step.EXCL_MIDDLE));

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
