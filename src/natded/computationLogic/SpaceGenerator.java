package natded.computationLogic;

import lexer.Lexer;
import natded.problemDomain.StepNode;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

public class SpaceGenerator {

    public static StepNode getNewSpace() {
        //TODO - get goal from list?

        String goalString = "%|-!(PVQ)=>(!P)^(!Q)";
        Lexer.setLexString(goalString);
        Parser.t = Lexer.lex();
        Clause c = Clause.parse(0);
        StepNode goal = new StepNode(null, goalString, c, new ArrayList<>());
        return goal;
    }

}
