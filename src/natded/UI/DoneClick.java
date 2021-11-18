package natded.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import natded.UI.logic.ControlLogic;
import natded.problemDomain.StepNode;

import java.util.HashMap;

public class DoneClick implements EventHandler<MouseEvent> {

    ControlLogic listener;

    public DoneClick(ControlLogic listener) {
        this.listener = listener;
    }

    @Override
    public void handle(MouseEvent event) {
        listener.onFinishedClick();
    }
}
