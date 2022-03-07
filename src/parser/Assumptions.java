package parser;

import lexer.Lexer;
import lexer.tokens.CommaToken;
import lexer.tokens.EmptyToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Assumptions {

	private List<Expr> assumptions;

	public Assumptions(List<Expr> assumptions){
		this.assumptions= new ArrayList<>(assumptions);
	}

	public Assumptions() {
		this.assumptions = new ArrayList<>();
	}

	public static Assumptions parse(){
		List<Expr> assumptions = new ArrayList<>();
		assumptions.add(Expr.parse());

		while (Parser.t instanceof CommaToken) {
			Parser.t = Lexer.lex();
			assumptions.add(Expr.parse());
		}

		return new Assumptions(assumptions);
	}

	public List<Expr> getAssumptions() {
		return assumptions;
	}

	@Override
	public String toString() {
		if (assumptions.size()==0) {
			return EmptyToken.getString();
		} else {
			String returnstring = assumptions.get(0).toString();
			for (int i = 1; i < assumptions.size(); i ++) {
				returnstring += ", " + assumptions.get(i).toString();
			}
			return returnstring;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Assumptions)) {
			return false;
		}
		Assumptions compare = (Assumptions) obj;
		if (compare.assumptions.size()!= this.assumptions.size()) {
			return false;
		}
		ArrayList<Expr> list1 = new ArrayList<>(this.getAssumptions());
		ArrayList<Expr> list2 = new ArrayList<>(compare.getAssumptions());

		for (Expr expr : list1) {
			if (!list2.contains(expr)) {
				return false;
			}
		}
		for (Expr expr : list2) {
			if (!list1.contains(expr)) {
				return false;
			}
		}
		return true;
	}
}
