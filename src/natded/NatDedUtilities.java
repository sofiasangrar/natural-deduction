package natded;

import javafx.scene.control.Alert;
import lexer.tokens.*;
import natded.constants.Step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NatDedUtilities {

    public static String randomGoal(){
        return deMorgan;
    }

    public static final String and = AndToken.getString();

    public static final String empty = EmptyToken.getString();

    public static final String or = OrToken.getString();

    public static final String impl = ImpliesToken.getString();

    public static final String turnstile = TurnstileToken.getString();

    public static final String not =  NotToken.getString();

    public static final String deMorgan = empty + " " + turnstile + " " + not + "(P "
            + or + " Q) " + impl + " (" + not + "P) "
            + and + " (" + not + "Q)";

    public static final String del =  "/";

    //following examples from https://www.danielclemente.com/logica/dn.en.pdf
    public static final String notPorQ = not + "P " + or + " Q " + turnstile + " P " + impl + " Q";
    public static final String modusPonens = "P " + impl + " Q, " + not + "Q "  + turnstile + " " + not + "P";
    public static final String orAndAnd = "P " + or + " (Q " + and + " R) "  + turnstile + " P " + or + " Q";
    public static final String halfOfOr = "P " + or + " Q, " + not + "P " + turnstile + " Q";
    public static final String falseIsTrue = not + "P " + turnstile + " P " + impl + " Q";

    public static final String[] proofs = {deMorgan, notPorQ, modusPonens, orAndAnd, halfOfOr, falseIsTrue};
    public static final Class[] logicSymbols = {AndToken.class, OrToken.class, EmptyToken.class, ImpliesToken.class, TurnstileToken.class, NotToken.class};

    public static StepNode load(){
        File saveFile = new File("resources/IO/save.txt");
        ArrayList<StepNode> stack = new ArrayList<>();
        try {
            Scanner reader = new Scanner(saveFile);
            if (!reader.hasNextLine()){
                return new StepNode(randomGoal(), Step.UNASSIGNED);
            }
            String line = reader.nextLine();
            if (line.trim().equals("")){
                return new StepNode(randomGoal(), Step.UNASSIGNED);
            }

            StepNode root = getNodeFromString(line);
            stack.add(root);

            StepNode parent;
            int level = 0;
            while (reader.hasNextLine()){

                line = reader.nextLine();
                int counter = 0;
                while (line.charAt(counter)=='\t'){
                    counter++;
                }
                if (counter<=level) {
                    for (int i = 0; i <= level - counter; i++){
                        stack.remove(stack.size()-1);
                    }


                }
                level=counter;
                parent = stack.get(stack.size()-1);
                StepNode nextNode = getNodeFromString(line);
                parent.addChild(nextNode);
                stack.add(nextNode);
            }
            return root;


        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No load file found.");
            alert.showAndWait();
        }
        return new StepNode(randomGoal(), Step.UNASSIGNED);
    }

    /**
     * turn comma delimited text into a node with a srtep
     * @param string node string
     * @return a proof step
     */
    private static StepNode getNodeFromString(String string){
        String newString = string.trim();
        String[] vals = newString.split(del);
        String content="";
        for (int i = 0; i < vals.length-1; i++){
            content+=vals[i];
        }
        Step step = Step.valueOf(vals[vals.length-1]);
        return new StepNode(content, step);
    }


}
