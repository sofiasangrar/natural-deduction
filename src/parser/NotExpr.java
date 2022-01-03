package parser;

import lexer.Lexer;
import lexer.tokens.NotToken;

public final class NotExpr extends Factor {
    public Expr right;

    public NotExpr(Expr right) {
        this.right = right;
    }

    public static NotExpr parse(){
        Parser.t = Lexer.lex();
        return new NotExpr(Factor.parse());
    }

    @Override
    public String toString() {
        return "!" + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return compare instanceof NotExpr && right.equals(((NotExpr) compare).right);
    }

}