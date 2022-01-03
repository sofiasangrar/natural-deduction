package parser;

import lexer.Lexer;
import lexer.tokens.ImpliesToken;
import lexer.tokens.OrToken;

public class Disj extends Expr{
    public Conj left;
    public Disj right;
    boolean hasright;

    Disj(){

    }

    Disj(Conj left){
        this.left = left;
        this.right = null;
        hasright = false;
    }

    Disj(Conj left, Disj right){
        this.left = left;
        this.right = right;
        hasright = true;
    }

    public static Disj parse() {
        Conj left = Conj.parse();
        if (Parser.t instanceof OrToken) {
            Parser.t = Lexer.lex();
            Disj right = Disj.parse();
            return new Disj(left, right);
        }
        return left;
    }

    @Override
    public String toString() {
        return left.toString() + " V " + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return compare instanceof Disj && left.equals(((Disj)compare).left) && right.equals(((Disj)compare).right);
    }
}
