package natded;

import lexer.tokens.*;

public class NatDedUtilities {

    public static String randomGoal(){
        return EmptyToken.getString() + " " + NDToken.getString() + " " + NotToken.getString() + "(P "
                + OrToken.getString() + " Q) " + ImpliesToken.getString() + " (" + NotToken.getString() + "P) "
                + AndToken.getString() + " (" + NotToken.getString() + "Q)";
    }

    public static final String and = AndToken.getString();

    public static final String empty = EmptyToken.getString();

    public static final String or = OrToken.getString();

    public static final String impl = ImpliesToken.getString();

    public static final String nd = NDToken.getString();

    public static final String not =  NotToken.getString();

}
