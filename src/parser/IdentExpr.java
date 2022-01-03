package parser;

import lexer.Lexer;
import lexer.tokens.IdentToken;

public class IdentExpr extends Factor {

    String identifier;

    IdentExpr(String identifier) {
        this.identifier = identifier;
    }

    IdentExpr() {
    }

    public static IdentExpr parse() {
        String s = ((IdentToken)Parser.t).getAttr();
        Parser.t = Lexer.lex();
        // returns the parsed identExpr
        return new IdentExpr(s);

    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentExpr && obj.toString().equals(identifier);
    }
}
