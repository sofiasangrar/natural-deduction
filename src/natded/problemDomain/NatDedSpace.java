package natded.problemDomain;

import natded.computationLogic.NatDedUtilities;
import natded.constants.SpaceState;

public class NatDedSpace {
    private final SpaceState spaceState;
    private final StepNode treeState;


    /**
     * I suppose that the most fundamental states required to represent a sudoku game, are an active state and a
     * complete state. The game will start in Active state, and when a Complete state is achieved (based on GameLogic),
     * then a special UI screen will be displayed by the user interface.
     *
     * To avoid Shared Mutable State (Shared change-able data), which causes many problems, I have decided to make this
     * class Immutable (meaning that once I created an instance of it, the values may only be read via getGameState()
     * and getGridState() functions, a.k.a. methods. Each time the gridState changes, a new SudokuGame object is created
     * by taking the old one, applying some functions to each, and generated a new one.
     *
     * @param spaceState I have decided to make the initial potential states of the game to be an ENUM (a set of custom
     *                  constant values which I give legible names to), one of:
     *                  - GameState.Complete
     *                  - GameState.Active
     *
     * @param treeState The state of the sudoku game. If certain conditions are met (all locations in the gridstate
     *                  are filled in with the proper value), GameLogic must change gameState.
     *                  Examples:
     *                  - gridState[1,1] Top left square
     *                  - gridState[3,9] 3rd from the left, bottom row
     *                  - gridState[9,9] Bottom right square
     */
    public NatDedSpace(StepNode treeState) {
        this.treeState = treeState;
        this.spaceState = SpaceState.NEW;
    }

    public SpaceState getSpaceState() {
        return spaceState;
    }

    public StepNode getRoot() {
        return treeState;
    }

    public StepNode getCopyOfTreeData() {
        return NatDedUtilities.copyTree(treeState);
    }

}
