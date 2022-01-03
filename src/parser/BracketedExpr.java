package parser;

import lexer.Lexer;
import lexer.tokens.NotToken;
import lexer.tokens.RParenToken;

public final class BracketedExpr extends Factor {
    public Expr expr;

    public BracketedExpr(Expr expr) {
        this.expr = expr;
    }

    public static BracketedExpr parse(){
        Parser.t = Lexer.lex();
        Expr expr = Expr.parse();
        if (!(Parser.t instanceof RParenToken)) {
            System.out.println("Error occurred at character " + Parser.t.getStartingColumnOfToken() + ":");
            System.out.println("Expected ')' but received " + Parser.t.getString() + ".");
            Parser.error = true;
            Parser.t = Lexer.lex();
            return null;
        }
        Parser.t = Lexer.lex();
        return new BracketedExpr(expr);
    }

    @Override
    public String toString() {
        return "(" + expr.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BracketedExpr && ((BracketedExpr)obj).right.equals(this.expr);
    }
}