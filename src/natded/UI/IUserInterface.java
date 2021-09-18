package natded.UI;

import natded.problemDomain.StepNode;

public interface IUserInterface {
    //Short is just a smaller version of an "int". Although computers have become very powerful,
    //it is still good practice to use the smallest possible data structure, unless legibility (such as an enum)
    //is a more important concern for the problem in front of you.
    interface EventListener {
        void onAddChildClick(StepNode node);
        void onTextInput(StepNode node, String input);
        void onFinishedClick();
    }


    //View refers to what the user can "View", or "See". In English, the word is both a noun and a verb.
    interface View {
        void setListener(IUserInterface.EventListener listener);

        //update a single square after user input
        void updateField(int index, String input);
        void addChild(int index);

        //update the entire board, such as after game completion or initial execution of the program
        void updateView(StepNode root);
        void showDialog(String message);
        void showError(String message);
    }
}
