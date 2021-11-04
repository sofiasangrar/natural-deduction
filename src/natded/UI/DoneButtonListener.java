package natded.UI;

import natded.UI.logic.ControlLogic;
import natded.problemDomain.StepNode;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class DoneButtonListener implements MouseListener {

    ControlLogic listener;

    public DoneButtonListener(ControlLogic listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        listener.onFinishedClick();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        listener.onFinishedClick();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
