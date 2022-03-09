package parser;

import lexer.Lexer;
import lexer.tokens.RParenToken;

public final class BracketedExpr extends Factor {
    public Expr expr;

    public BracketedExpr(Expr expr) {
        this.expr = expr;
    }

    /**
     * parse next tokens as a bracketed expression
     * @return new bracketed expression with inner expression also parsed
     */
    public static BracketedExpr parse(){
        Parser.t = Lexer.lex();
        Expr expr = Expr.parse();

        //left bracke must be closed by right bracket
        if (!(Parser.t instanceof RParenToken)) {
            Parser.errorHandle();
            return new BracketedExpr(expr);
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