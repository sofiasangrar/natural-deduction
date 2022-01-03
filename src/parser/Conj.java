package parser;

import lexer.Lexer;
import lexer.tokens.AndToken;
import lexer.tokens.OrToken;

public class Conj extends Disj {
    Factor left;
    public Conj right;
    boolean hasright;

    Conj(){

    }

    Conj(Factor left){
        this.left = left;
        this.right = null;
        hasright = false;
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
        return left.toString() + " ^ " + right.toString();
    }
}
