package natded.UI.logic;

import lexer.Lexer;
import natded.UI.UserInterface;
import natded.computationLogic.Logic;
import natded.constants.SpaceState;
import natded.problemDomain.NatDedSpace;
import natded.problemDomain.StepNode;
import parser.Clause;
import parser.Parser;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

public class ControlLogic {

    //Remember, this could be the real UserInterface, or it could be a test class
    //which implements the same interface!
    private UserInterface view;
    private NatDedSpace space;

    public ControlLogic(NatDedSpace space, UserInterface view) {
        this.space = space;
        this.view = view;
    }

    public void onAddChildClick(StepNode node) {
        //try {
            node.addChild(new StepNode(node, "", null, new ArrayList<>()));
            //NatDedSpace spaceData = storage.getSpaceData();
            //StepNode newTreeState = space.getCopyOfTreeData();
            //todo -
            // hashmap.get(index).setInput(input);

            //space = new NatDedSpace(newTreeState);

            //storage.updateSpaceData(spaceData);

            //either way, update the view
            view.updateView(space.getRoot());

        //} catch (IOException e) {
          //  e.printStackTrace();
            //view.showError(Messages.ERROR);
        //}

    }

    public void onTextInput(StepNode node, String input) {
        //try {
            //NatDedSpace spaceData = storage.getSpaceData();
            //StepNode newTreeState = spaceData.getCopyOfTreeData();
            //todo -
            // hashmap.get(index).setInput(input);

            node.setInput(input);

            //spaceData = new NatDedSpace(newTreeState);

            //storage.updateSpaceData(spaceData);

            //either way, update the view
            //view.updateField(node.getIndex(), input);

        //} catch (IOException e) {
          //  e.printStackTrace();
            //view.showError(Messages.ERROR);
        //}
    }

    public void onFinishedClick() {
      //  try {
           // NatDedSpace spaceData = storage.getSpaceData();
            StepNode root = space.getRoot();
            parseTree(root);
            SpaceState s = Logic.checkForCompletion(root);
            if (s==SpaceState.VALID) {
                view.showDialog("complete");
            } else {
                view.showDialog("invalid");
            }
        //} catch (IOException e) {
        //e.printStackTrace();
        //view.showError(Messages.ERROR);
    //}
    }

    public void parseTree(StepNode node){
        Lexer.setLexString(node.getInput());
        Parser.t = Lexer.lex();
        Clause.parse(node.getIndex());
        for (int i = 0; i < node.getChildren().size(); i++) {
            parseTree(node.getChildren().get(i));
        }
    }
}
