package parser;

import lexer.Lexer;
import lexer.tokens.AndToken;
import lexer.tokens.OrToken;

public class Conj extends Disj {
    public Factor left;
    public Conj right;
    public boolean hasright;

    Conj(){

    }

    Conj(Factor left, Conj right){
        this.left = left;
        this.right = right;
        hasright = true;
    }

    public static Conj parse() {
        Factor left = Factor.parse();
        if (Parser.t instanceof AndToken) {
            Parser.t = Lexer.lex();
            Conj right = Conj.parse();
            return new Conj(left, right);
        }
        return left;
    }

    @Override
    public String toString() {
        return left.toString() + " " + AndToken.getString() +" " + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return compare instanceof Conj && left.equals(((Conj)compare).left) && right.equals(((Conj)compare).right);
    }
}
