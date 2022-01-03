package natded.computationLogic;

import clauseParser.Proof;
import lexer.Lexer;
import natded.constants.SpaceState;
import natded.constants.Step;
import natded.problemDomain.NatDedSpace;
import natded.problemDomain.StepNode;
import parser.Clause;
import parser.Parser;

public class Logic {

    public static NatDedSpace getNewSpace() {
        return new NatDedSpace(
                SpaceGenerator.getNewSpace()
        );
    }

    public static SpaceState checkForCompletion(StepNode tree) {
        if (fieldsAreNotFilled(tree)) return SpaceState.INVALID;
        if (proofIsInvalid(tree)) return SpaceState.INVALID;
        if (fieldIsInvalid(tree)) return SpaceState.INVALID;
        return SpaceState.VALID;
    }

    /**
     * Traverse all tiles and determine if any all are not 0.
     * Note: GRID_BOUNDARY = GRID_BOUNDARY
     *
     * @param
     * @return
     */
    public static boolean fieldsAreNotFilled(StepNode tree) {
        boolean fieldsNotFilled = false;
        if (tree.getInput().equals("")) {
            fieldsNotFilled = true;
        }
        for (int i = 0; i < tree.getChildren().size(); i++) {
            if (fieldsAreNotFilled(tree.getChildren().get(i))) {
                fieldsNotFilled = true;
            }
        }
        return fieldsNotFilled;
    }

    public static boolean stepIsUnassigned(StepNode tree) {
        boolean stepUnassigned = false;
        if (tree.getStep()== Step.UNASSIGNED) {
            stepUnassigned = true;
        }
        for (int i = 0; i < tree.getChildren().size(); i++) {
            if (stepIsUnassigned(tree.getChildren().get(i))) {
                stepUnassigned = true;
            }
        }
        return stepUnassigned;
    }

    /**
     * Checks the if the current complete or incomplete state of the game is still a valid state of a Sudoku game,
     * relative to columns, rows, and squares.
     *
     * @param
     * @return
     */
    public static boolean proofIsInvalid(StepNode tree) {
        boolean isInvalid = false;
        isInvalid = !Proof.checkStep(tree.getPremisses(), tree.getParsedInput());
        for (int i = 0; i < tree.getChildren().size(); i++) {
            if (proofIsInvalid(tree.getChildren().get(i))) {
                isInvalid = true;
            }
        }
        return isInvalid;
    }


    public static boolean fieldIsInvalid(StepNode tree) {
        boolean invalidField = false;
        Lexer.setLexString(tree.getInput());
        Parser.t = Lexer.lex();
        Clause c = Clause.parse(tree.getIndex());
        //TODO - return whether error was found, currently just prints
        for (int i = 0; i < tree.getChildren().size(); i++) {
            fieldIsInvalid(tree.getChildren().get(i));
        }
        return invalidField;
    }
}
