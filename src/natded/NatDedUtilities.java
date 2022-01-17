package natded;

import lexer.tokens.*;

public class NatDedUtilities {
    private static int index = -1;

    public static int nextIndex(){
        return ++index;
    }

    public static String randomGoal(){
        return EmptyToken.getString() + " " + NDToken.getString() + " " + NotToken.getString() + "(P "
                + OrToken.getString() + " Q) " + ImpliesToken.getString() + " (" + NotToken.getString() + "P) "
                + AndToken.getString() + " (" + NotToken.getString() + "Q)";
    }
}
