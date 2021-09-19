package natded.computationLogic;

import natded.constants.SpaceState;
import natded.problemDomain.NatDedSpace;
import natded.problemDomain.StepNode;

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
     * @param grid
     * @return
     */
    public static boolean fieldsAreNotFilled(StepNode tree) {
        //TODO - traverse tree
        return false;
    }

    public static boolean stepIsUnassigned(StepNode tree) {
        //TODO - traverse tree
        return false;
    }

    /**
     * Checks the if the current complete or incomplete state of the game is still a valid state of a Sudoku game,
     * relative to columns, rows, and squares.
     *
     * @param
     * @return
     */
    public static boolean proofIsInvalid(StepNode tree) {
        //TODO - proof check
        return false;
    }


    public static boolean fieldIsInvalid(StepNode tree) {
        //TODO - syntax check
        return false;
    }
}
