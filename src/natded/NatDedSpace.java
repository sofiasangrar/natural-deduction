package natded;

import lexer.Lexer;
import natded.constants.SpaceState;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

import static natded.NatDedUtilities.randomGoal;

public class NatDedSpace {
    private SpaceState spaceState;
    private StepNode treeState;

    public NatDedSpace(StepNode treeState) {
        this.treeState = treeState;
        this.spaceState = SpaceState.NEW;
    }

    public SpaceState getSpaceState() {
        return spaceState;
    }

    public void setSpaceState(SpaceState spaceState) {
        this.spaceState = spaceState;
    }


    public StepNode getRoot() {
        return treeState;
    }

    public NatDedSpace() {
        String goalString = randomGoal();
        Lexer.setLexString(goalString);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse(0);
        treeState = new StepNode(null, goalString, c, new ArrayList<>());
        this.spaceState = SpaceState.NEW;
    }

}
