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

    public static final String turnstile = TurnstileToken.getString();

    public static final String not =  NotToken.getString();

    public static final String deMorgan = empty + " " + turnstile + " " + not + "(P "
            + or + " Q) " + impl + " (" + not + "P) "
            + and + " (" + not + "Q)";

    //following examples from https://www.danielclemente.com/logica/dn.en.pdf
    public static final String notPorQ = not + "P " + or + " Q " + turnstile + " P " + impl + " Q";
    public static final String modusPonens = "P " + impl + " Q, " + not + "Q "  + turnstile + " " + not + "P";
    public static final String orAndAnd = "P " + or + " (Q " + and + " R) "  + turnstile + " P " + or + " Q";
    public static final String halfOfOr = "P " + or + " Q, " + not + "P " + turnstile + " Q";
    public static final String falseIsTrue = not + "P " + turnstile + " P " + impl + " Q";

    public static final String[] proofs = {deMorgan, notPorQ, modusPonens, orAndAnd, halfOfOr, falseIsTrue};
    public static final Class[] logicSymbols = {AndToken.class, OrToken.class, EmptyToken.class, ImpliesToken.class, TurnstileToken.class, NotToken.class};

}
