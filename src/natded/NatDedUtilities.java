package natded;

public class NatDedUtilities {
    private static int index = -1;

    public static int nextIndex(){
        return ++index;
    }

    public static String randomGoal(){
        return "%|-!(P V Q)=>(!P)^(!Q)";
    }
}
