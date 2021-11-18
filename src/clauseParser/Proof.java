package clauseParser;

import lexer.Lexer;
import lexer.tokens.AndToken;
import lexer.tokens.ImpliesToken;
import lexer.tokens.OrToken;
import parser.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Proof{

	ArrayList<Clause> clauses;

	// Constructor
	private Proof(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}
	
  public static Proof parse(Scanner scanner, int clauses) {
		ArrayList<Clause> clauseList = new ArrayList<>();
		for (int i = 0; i < clauses; i++) {
			Lexer.setLexString(scanner.nextLine());
			Parser.t = Lexer.lex();
			Parser.t = Lexer.lex();
			Clause c = Clause.parse(i);
			clauseList.add(c);
		}
    return new Proof(clauseList);
  }

  public int size(){
		return clauses.size();
  }

  public static void check(Proof proof){
		if (!proof.checkFirstClause()){
			System.out.println("Assumption not found at clause 1");
		}
		for (int i = 1; i < proof.size(); i++) {
			ArrayList<Clause> clauses = new ArrayList<>();
			clauses.add(proof.clauses.get(i-1));
			Clause current = proof.clauses.get(i);
			checkStep(clauses, current);
		}
  }

  public boolean checkFirstClause(){
		return (clauses.get(0).getAssumptions().contains(clauses.get(0).getExpression())
				|| (clauses.get(0).getExpression() instanceof BooleanExpr && ((BooleanExpr)clauses.get(0).getExpression()).value))
				//law of excluded middle
	  ;
  }

  public static boolean checkStep(ArrayList<Clause> premisses, Clause conclusion){
		if (premisses.size()==1) {
			Clause clause = premisses.get(0);

	  	//and elim
		  if (clause.getExpression() instanceof BinaryExpr && ((BinaryExpr) clause.getExpression()).oper instanceof AndToken) {
		  	Expr left = ((BinaryExpr) clause.getExpression()).left;
		  	Expr right = ((BinaryExpr) clause.getExpression()).right;
		  	if ((conclusion.getExpression().equals(left) || conclusion.getExpression().equals(right)) && clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
				System.out.println("and-elim");
				return true;
			}
		  }

		  //imp intro
		  if (clause.getAssumptions().size()!=0 &&
				  conclusion.getExpression() instanceof BinaryExpr &&
				  ((BinaryExpr) conclusion.getExpression()).oper instanceof ImpliesToken &&
				  clause.getAssumptions().size() - 1 == conclusion.getAssumptions().size()) {

				Expr left = ((BinaryExpr) conclusion.getExpression()).left;
				if (clause.getAssumptions().contains(left)
						&& !conclusion.getAssumptions().contains(left)) {
					ArrayList<Expr> assumptions = new ArrayList<>(conclusion.getAssumptions());
					assumptions.add(((BinaryExpr) conclusion.getExpression()).left);
					Assumptions newAssumptions = new Assumptions(assumptions);
					if (newAssumptions.equals(clause.getAssumptionsObject()) && ((BinaryExpr) conclusion.getExpression()).right.equals(clause.getExpression())) {
						System.out.println("imp-intro");
						return true;
					}
				}
		  }

		  //or intro
			if (conclusion.getExpression() instanceof BinaryExpr && ((BinaryExpr) conclusion.getExpression()).oper instanceof OrToken) {
				Expr left = ((BinaryExpr) conclusion.getExpression()).left;
				Expr right = ((BinaryExpr) conclusion.getExpression()).right;
				if ((clause.getExpression().equals(left) || clause.getExpression().equals(right)) && clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					System.out.println("or-intro");
					return true ;
				}
			}

			//false elim
			if (clause.getExpression() instanceof BooleanExpr && !((BooleanExpr)clause.getExpression()).value && clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())){
				System.out.println("false-elim");
				return true;
			}

	  } else if (premisses.size()==2) {
			Clause clause1 = premisses.get(0);
			Clause clause2 = premisses.get(1);

			//and intro
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& conclusion.getExpression() instanceof BinaryExpr
					&& ((BinaryExpr) conclusion.getExpression()).oper instanceof AndToken) {

				Expr left = ((BinaryExpr) conclusion.getExpression()).left;
				Expr right = ((BinaryExpr) conclusion.getExpression()).right;
				if (left.equals(clause1.getExpression()) && right.equals(clause2.getExpression())){
					System.out.println("and-intro");
					return true;
				}
			}

		}
	    System.out.println("invalid!");
		return false;
  }

}
