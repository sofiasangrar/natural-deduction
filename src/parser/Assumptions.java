package parser;

import lexer.Lexer;
import lexer.tokens.CommaToken;
import lexer.tokens.EmptyToken;

import java.util.ArrayList;
import java.util.List;

public class Assumptions {

	private List<Expr> assumptions;

	Assumptions(List<Expr> assumptions){
		this.assumptions= new ArrayList<>(assumptions);
	}

	Assumptions() {
		this.assumptions = new ArrayList<>();
	}

	/**
	 * parse consequent tokens as assumptions
	 * @return new assumptions object from relevant tokens
	 */
	public static Assumptions parse(){
		List<Expr> assumptions = new ArrayList<>();
		assumptions.add(Expr.parse());

		//comma signifies another assumption to add to the list
		while (Parser.t instanceof CommaToken) {
			Parser.t = Lexer.lex();
			assumptions.add(Expr.parse());
		}

		return new Assumptions(assumptions);
	}

	/**
	 * get assumptions list
	 * @return list of assumptions
	 */
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

		//lists must be equal (in any order) for the assumption objects to be considered the same.
		//check for set equality by checking if each set is contained in the other
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
