package clauseParser;

import com.sun.tools.corba.se.idl.constExpr.Not;
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
		if(premisses == null || premisses.size()==0){
			//assumption
			if (conclusion.getAssumptions().contains(conclusion.getExpression())) {
				return true;
			}

			//true intro
			if(conclusion.getExpression() instanceof BooleanExpr && ((BooleanExpr) conclusion.getExpression()).value) {
				System.out.println("true-intro");
				return true;
			}

			//excluded middle
			if(conclusion.getExpression() instanceof BinaryExpr
					&& ((BinaryExpr)conclusion.getExpression()).oper instanceof OrToken){
				Expr left = ((BinaryExpr) conclusion.getExpression()).left;
				Expr right = ((BinaryExpr) conclusion.getExpression()).right;

				if ((left instanceof NotExpr && ((NotExpr)left).right.equals(right))
				|| (right instanceof NotExpr && ((NotExpr)right).right.equals(left))) {
					System.out.println("excluded middle");
					return true;
				}
			}
		}

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
				if ((left.equals(clause1.getExpression()) && right.equals(clause2.getExpression())) || (left.equals(clause2.getExpression()) && right.equals(clause1.getExpression()))){
					System.out.println("and-intro");
					return true;
				}
			}

			//imp elim
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& conclusion.getExpression() instanceof BinaryExpr
					&& ((BinaryExpr) conclusion.getExpression()).oper instanceof ImpliesToken
					&& ((clause1.getExpression() instanceof BinaryExpr && ((BinaryExpr) clause1.getExpression()).oper instanceof ImpliesToken)
						|| clause2.getExpression() instanceof BinaryExpr && ((BinaryExpr) clause2.getExpression()).oper instanceof ImpliesToken)

					&& ((clause1.getExpression() instanceof BinaryExpr
							&& ((BinaryExpr) clause1.getExpression()).oper instanceof ImpliesToken
							&& ((BinaryExpr) clause1.getExpression()).left.equals(clause2.getExpression())
							&& ((BinaryExpr) clause1.getExpression()).right.equals(conclusion.getExpression()))
						|| (clause2.getExpression() instanceof BinaryExpr
							&& ((BinaryExpr) clause2.getExpression()).oper instanceof ImpliesToken
							&& ((BinaryExpr) clause2.getExpression()).left.equals(clause1.getExpression())
							&& ((BinaryExpr) clause2.getExpression()).right.equals(conclusion.getExpression())))
				)
				{
					System.out.println("imp-elim");
					return true;
				}

			//neg elim
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& ((clause1.getExpression() instanceof NotExpr
						&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
					|| (clause2.getExpression() instanceof NotExpr
						&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))
			){
				System.out.println("neg-elim");
				return true;
			}

			//neg-intro
			if (clause1.getAssumptions().size()!=0
					&& clause2.getAssumptions().size()!=0
					&& ((clause1.getExpression() instanceof NotExpr
							&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
						|| (clause2.getExpression() instanceof NotExpr
							&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)
						))
					&& conclusion.getExpression() instanceof NotExpr
					&& clause1.getAssumptions().contains(((NotExpr) conclusion.getExpression()).right)
					&& clause2.getAssumptions().contains(((NotExpr) conclusion.getExpression()).right)
					&& !conclusion.getAssumptions().contains(((NotExpr) conclusion.getExpression()).right)
			) {

				Assumptions newAssumptions = new Assumptions();
				Expr P = ((NotExpr) conclusion.getExpression()).right;

				for (Expr assumption : clause1.getAssumptions()) {
					if (!assumption.equals(P)) {
						newAssumptions.getAssumptions().add(assumption);
					}
				}

				for (Expr assumption : clause2.getAssumptions()) {
					if (!assumption.equals(P)) {
						newAssumptions.getAssumptions().add(assumption);
					}
				}

				if (newAssumptions.equals(conclusion.getAssumptionsObject())) {
					System.out.println("neg-intro");
					return true;
				}
			}

		}
	    System.out.println("invalid!");
		return false;
  }

}
