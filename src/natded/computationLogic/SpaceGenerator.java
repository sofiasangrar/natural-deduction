package natded.computationLogic;

import natded.problemDomain.StepNode;

import java.util.ArrayList;

public class SpaceGenerator {

    public static StepNode getNewSpace() {
        //TODO - get goal from list?

        StepNode goal = new StepNode(null, "∅⊢¬(P∨Q)→(¬P)∧(¬Q)", null, new ArrayList<>());
        return goal;
    }

}
