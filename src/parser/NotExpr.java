package parser;

import lexer.Lexer;

import static natded.NatDedUtilities.not;

public class NotExpr extends Factor {
    Expr right;

    private NotExpr(Expr right) {
        this.right = right;
    }

    /**
     * parse a negated expression
     * @return parsed negation
     */
    public static NotExpr parse(){
        Parser.t = Lexer.lex();
        return new NotExpr(Factor.parse());
    }

    @Override
    public String toString() {
        return not + right.toString();
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