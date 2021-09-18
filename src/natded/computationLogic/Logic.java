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

    /**
     * Check to see if the incoming state (what the values of each square happen to be) of the game is either Active
     * (i.e. Unsolved) or Complete (i.e. Solved).
     *
     * @param grid A virtual representation of a sudoku puzzle, which may or may not be solved.
     * @return Either GameState.Active or GameState.Complete, based on analysis of solvedSudoku.
     * <p>
     * Rules:
     * - A number may not be repeated among Rows, e.g.:
     * - [0, 0] == [0-8, 1] not allowed
     * - [0, 0] == [3, 4] allowed
     * - A number may not be repeated among Columns, e.g.:
     * - [0-8, 1] == [0, 0] not allowed
     * - [0, 0] == [3, 4] allowed
     * - A number may not be repeated within respective GRID_BOUNDARYxGRID_BOUNDARY regions within the Sudoku Puzzle
     * - [0, 0] == [1, 2] not allowed
     * - [0, 0] == [3, 4] allowed
     */
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

    /**
     * Checks the if the current complete or incomplete state of the game is still a valid state of a Sudoku game,
     * relative to columns, rows, and squares.
     *
     * @param grid
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
