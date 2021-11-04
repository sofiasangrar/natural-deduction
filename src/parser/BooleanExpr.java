package parser;

import lexer.Lexer;
import lexer.tokens.FalseToken;
import lexer.tokens.TrueToken;

public final class BooleanExpr extends Expr {
    public boolean value;

    public BooleanExpr(boolean value) {
        this.value = value;
    }

    public static BooleanExpr parse(){
        if (Parser.t instanceof TrueToken) {
            Parser.t = Lexer.lex();
            return new BooleanExpr(true);
        } else if (Parser.t instanceof FalseToken) {
            Parser.t = Lexer.lex();
            return new BooleanExpr(false);
        } else {
            Parser.errorHandle(new TrueToken(0));
            Parser.t = Lexer.lex();
        }
        return null;
    }

    @Override
    public String toString() {
        if (value) return new TrueToken(0).toString(); else return new FalseToken(0).toString();
    }
}