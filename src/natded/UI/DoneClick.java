package natded.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import natded.UIListener;

public class DoneClick implements EventHandler<MouseEvent> {

    UIListener listener;

    public DoneClick(UIListener listener) {
        this.listener = listener;
    }

    @Override
    public void handle(MouseEvent event) {
        listener.onFinishedClick();
    }
}
