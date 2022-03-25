package parser;

import lexer.Lexer;
import lexer.tokens.CommaToken;
import lexer.tokens.EmptyToken;

import java.util.ArrayList;
import java.util.List;

public class Antecedents {

	private List<Expr> antecedents;

	Antecedents(List<Expr> antecedents){
		this.antecedents = new ArrayList<>(antecedents);
	}

	Antecedents() {
		this.antecedents = new ArrayList<>();
	}

	/**
	 * parse consequent tokens as antecedents
	 * @return new antecedents object from relevant tokens
	 */
	public static Antecedents parse(){
		List<Expr> assumptions = new ArrayList<>();
		assumptions.add(Expr.parse());

		//comma signifies another assumption to add to the list
		while (Parser.t instanceof CommaToken) {
			Parser.t = Lexer.lex();
			assumptions.add(Expr.parse());
		}

		return new Antecedents(assumptions);
	}

	/**
	 * get antecedents list
	 * @return list of antecedents
	 */
	public List<Expr> getAntecedents() {
		return antecedents;
	}

	@Override
	public String toString() {
		if (antecedents.size()==0) {
			return EmptyToken.getString();
		} else {
			String returnstring = antecedents.get(0).toString();
			for (int i = 1; i < antecedents.size(); i ++) {
				returnstring += ", " + antecedents.get(i).toString();
			}
			return returnstring;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Antecedents)) {
			return false;
		}
		Antecedents compare = (Antecedents) obj;
		if (compare.antecedents.size()!= this.antecedents.size()) {
			return false;
		}
		ArrayList<Expr> list1 = new ArrayList<>(this.getAntecedents());
		ArrayList<Expr> list2 = new ArrayList<>(compare.getAntecedents());

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
