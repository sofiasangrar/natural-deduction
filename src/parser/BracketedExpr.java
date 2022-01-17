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
            System.out.println("Expected ')' but received " + Parser.t.expectedString() + ".");
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
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return this.unbracket().equals(compare);
    }

}