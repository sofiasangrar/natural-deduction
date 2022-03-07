package natded;

import lexer.tokens.*;

public class NatDedUtilities {

    public static String randomGoal(){
        return deMorgan;
    }

    public static final String and = AndToken.getString();

    public static final String empty = EmptyToken.getString();

    public static final String or = OrToken.getString();

    public static final String impl = ImpliesToken.getString();

    public static final String nd = NDToken.getString();

    public static final String not =  NotToken.getString();

    public static final String deMorgan = empty + " " + nd + " " + not + "(P "
            + or + " Q) " + impl + " (" + not + "P) "
            + and + " (" + not + "Q)";

    //following examples from https://www.danielclemente.com/logica/dn.en.pdf
    public static final String notPorQ = not + "P " + or + " Q " + nd + " P " + impl + " Q";
    public static final String modusPonens = "P " + impl + " Q, " + not + "Q "  + nd + " " + not + "P";
    public static final String orAndAnd = "P " + or + " (Q " + and + " R) "  + nd + " P " + or + " Q";
    public static final String halfOfOr = "P " + or + " Q, " + not + "P " + nd + " Q";
    public static final String falseIsTrue = not + "P " + nd + " P " + impl + " Q";





}
