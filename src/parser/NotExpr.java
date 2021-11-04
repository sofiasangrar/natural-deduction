package parser;

import lexer.Lexer;
import lexer.tokens.NotToken;

public final class NotExpr extends Expr {
    public Expr right;

    public NotExpr(Expr right) {
        this.right = right;
    }

    public static NotExpr parse(){
        Parser.t = Lexer.lex();
        return new NotExpr(Expr.parse());
    }

    @Override
    public String toString() {
        return new NotToken(0).toString() + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotExpr && ((NotExpr)obj).right.equals(this.right);
    }
}