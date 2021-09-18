package natded.computationLogic;

import natded.problemDomain.StepNode;

import java.util.ArrayList;

public class NatDedUtilities {
    private static int index = -1;
    /**
     * Copy the values from one sudoku grid into another
     *
     * Note: O(n^2) Runtime Complexity
     * @param oldTree
     */
    public static StepNode copyTree(StepNode oldTree) {
        //TODO - deep copy parsed input
        StepNode newTree = new StepNode(null, oldTree.getInput(), oldTree.getParsedInput(), new ArrayList<>());
        for (int i = 0; i < oldTree.getChildren().size(); i++) {
            StepNode newChild = copyTree(oldTree.getChildren().get(i));
            newChild.setParent(newTree);
            newTree.addChild(newChild);
        }

        return newTree;
    }

    /**
     * Copy the values from one sudoku grid into another
     *
     * Note: O(n^2) Runtime Complexity
     * @param oldTree
     */
    public static void copyTreeIntoNewTree(StepNode oldTree, StepNode newTree) {
        //TODO - deep copy parsed input
        newTree = new StepNode(null, oldTree.getInput(), oldTree.getParsedInput(), new ArrayList<>());
        for (int i = 0; i < oldTree.getChildren().size(); i++) {
            StepNode newChild = new StepNode();
            copyTreeIntoNewTree(oldTree.getChildren().get(i), newChild);
            newChild.setParent(newTree);
            newTree.addChild(newChild);
        }
    }

    public static int nextIndex(){
        return ++index;
    }
}
