package parser;

import lexer.Lexer;
import lexer.tokens.IdentToken;

public class IdentExpr extends Factor {

    private String identifier;

    IdentExpr(String identifier) {
        this.identifier = identifier;
    }

    public static IdentExpr parse() {
        String s = ((IdentToken)Parser.t).getAttr();
        Parser.t = Lexer.lex();
        return new IdentExpr(s);

    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return compare instanceof IdentExpr && identifier.equals(((IdentExpr) compare).identifier);
    }
}
