package parser;

import lexer.Lexer;
import lexer.tokens.CommaToken;
import lexer.tokens.EmptyToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Assumptions {

	private List<Expr> assumptions = new ArrayList<>();
	public Assumptions(List<Expr> assumptions){
		this.assumptions = assumptions;
	}

	public Assumptions() {
		List<Expr> assumptions = new ArrayList<>();
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
			return new EmptyToken(0).toString();
		} else {
			String returnstring = assumptions.get(0).toString();
			for (int i = 1; i < assumptions.size(); i ++) {
				returnstring+=", " + assumptions.get(i).toString();
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

		for (int i = 0; i < list1.size(); i++) {
			boolean found = false;
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).equals(list2.get(j))){
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
}
