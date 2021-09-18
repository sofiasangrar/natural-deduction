package natded.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import natded.problemDomain.StepNode;

import java.util.ArrayList;
import java.util.HashMap;

public class EventClick implements EventHandler<MouseEvent> {


    HashMap<Integer, StepTextField> fieldMap;
    IUserInterface.EventListener listener;

    public EventClick(HashMap<Integer, StepTextField> fields, IUserInterface.EventListener listener) {
        this.fieldMap = fields;
        this.listener = listener;
    }

    @Override
    public void handle(MouseEvent event) {
        int index = ((AddButton)event.getSource()).index;
        StepNode node =  fieldMap.get(index).getNode();
        listener.onAddChildClick(node);
    }
}
