package parser;

import lexer.Lexer;
import lexer.tokens.BooleanToken;
import lexer.tokens.FalseToken;
import lexer.tokens.TrueToken;

public final class BooleanExpr extends Factor {
    boolean value;

    private BooleanExpr(boolean value) {
        this.value = value;
    }

    /**
     * parse next token as a true or false
     * @return new boolean expression, either a true or false token
     */
    public static BooleanExpr parse(){
        if (Parser.t instanceof TrueToken) {
            Parser.t = Lexer.lex();
            return new BooleanExpr(true);
        } else if (Parser.t instanceof FalseToken) {
            Parser.t = Lexer.lex();
            return new BooleanExpr(false);
        } else {
            Parser.errorHandle(BooleanToken.getString());
        }
        return new BooleanExpr(true);
    }

    @Override
    public String toString() {
        if (value) return TrueToken.getString(); else return FalseToken.getString();
    }

    @Override
    public boolean equals(Object obj) {
        Expr compare;
        if (!(obj instanceof Expr)) {
            return false;
        }
        compare = ((Expr)obj).unbracket();
        return compare instanceof BooleanExpr && value == ((BooleanExpr) compare).value;
    }
}