package natded.problemDomain;

import natded.constants.SpaceState;

public class NatDedSpace {
    private final SpaceState spaceState;
    private final StepNode treeState;

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

}
