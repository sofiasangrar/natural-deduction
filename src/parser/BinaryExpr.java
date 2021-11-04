package parser;

import lexer.tokens.OperatorToken;

public final class BinaryExpr extends Expr {
    public Expr left;
    public OperatorToken oper;
    public Expr right;

    public BinaryExpr(Expr left, OperatorToken oper, Expr right) {
        this.left = left;
        this.oper = oper;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + oper.toString() + " " + right.toString() + ")";
    }
}