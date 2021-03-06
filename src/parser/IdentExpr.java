package parser;

import lexer.Lexer;
import lexer.tokens.IdentToken;

public class IdentExpr extends Factor {

    private String identifier;

    IdentExpr(String identifier) {
        this.identifier = identifier;
    }

    /**
     * parse identifier
     * @return parsed identifier
     */
    public static IdentExpr parse() {
        if (!(Parser.t instanceof IdentToken)){
            Parser.errorHandle("an identifier");
            return new IdentExpr("");
        }
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
