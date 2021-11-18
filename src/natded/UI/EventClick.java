package natded.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import natded.UI.logic.ControlLogic;
import natded.problemDomain.StepNode;

import java.util.HashMap;

public class EventClick implements EventHandler<MouseEvent> {


    HashMap<Integer, StepTextField> fieldMap;
    ControlLogic listener;

    public EventClick(HashMap<Integer, StepTextField> fields, ControlLogic listener) {
        this.fieldMap = fields;
        this.listener = listener;
    }

    @Override
    public void handle(MouseEvent event) {
        if(event.getSource() instanceof AddButton) {
            int index = ((AddButton) event.getSource()).index;
            StepNode node = fieldMap.get(index).getNode();
            listener.onAddChildClick(node);
        }
    }
}
