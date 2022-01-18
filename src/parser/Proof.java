package parser;

import lexer.Lexer;
import natded.StepNode;
import natded.constants.Step;

import java.util.ArrayList;

import static natded.constants.Step.*;

public class Proof{

	StepNode root;

	Proof(StepNode root) {
		this.root = root;
	}

	public static Proof parse(StepNode node){
		Lexer.setLexString(node.getInput());
		Parser.t = Lexer.lex();
		node.setParsedInput(Clause.parse());

		for (int i = 0; i < node.getChildren().size(); i++) {
			parse(node.getChildren().get(i));
		}
		return new Proof(node);
	}

	public boolean isValid() {
		return isValid(root);
	}

	private boolean isValid(StepNode root) {
		boolean isValid = Proof.checkStep(root.getPremisses(), root.getParsedInput(), root.getStep());
		if (!isValid) {
			return false;
		}
		for (int i = 0; i < root.getChildren().size(); i++) {
			if (!isValid(root.getChildren().get(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean handleStep(Step expected, Step actual) {
		if(expected!=actual) {
			System.out.println("Expected " + expected + ", received " + actual);
			return false;
		}
		return true;
	}

  public static boolean checkStep(ArrayList<Clause> premisses, Clause conclusion, Step step){

  	if(premisses == null || premisses.size()==0){

  		//assumption
			if (conclusion.getAssumptions().contains(conclusion.getExpression())) {
				//System.out.println("assumption");
				return handleStep(ASSUMPTION, step);
			}

			//true intro
			if(conclusion.getExpression() instanceof BooleanExpr && ((BooleanExpr) conclusion.getExpression()).value) {
				//System.out.println("true-intro");
				return handleStep(TRUE_INTRO, step);
			}

			//excluded middle
			if(conclusion.getExpression().isDisj()){
				Expr left = ((Disj) conclusion.getExpression()).left;
				Expr right = ((Disj) conclusion.getExpression()).right;

				if (right instanceof NotExpr
						&& ((NotExpr)right).right.equals(left)) {
					//System.out.println("excluded middle");
					return handleStep(EXCL_MIDDLE, step);
				}
			}
		}

		if (premisses.size()==1) {
			Clause clause = premisses.get(0);

	  	//and elim
		  if (clause.getExpression().isConj()) {
		  	Expr left = ((Conj)clause.getExpression()).left;
		  	Expr right = ((Conj)clause.getExpression()).right;
		  	if ((conclusion.getExpression().equals(left)
					|| conclusion.getExpression().equals(right))
					&& clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
				//System.out.println("and-elim");
				return handleStep(AND_ELIM, step);
			}
		  }

		  //imp intro
		  if (clause.getAssumptions().size()!=0 &&
				  conclusion.getExpression().isImpl() &&
				  clause.getAssumptions().size() - 1 == conclusion.getAssumptions().size()) {

				Expr left = conclusion.getExpression().left;
				if (clause.getAssumptions().contains(left)
						&& !conclusion.getAssumptions().contains(left)) {
					ArrayList<Expr> assumptions = new ArrayList<>(conclusion.getAssumptions());
					assumptions.add(conclusion.getExpression().left);
					Assumptions newAssumptions = new Assumptions(assumptions);
					if (newAssumptions.equals(clause.getAssumptionsObject()) && conclusion.getExpression().right.equals(clause.getExpression())) {
						//System.out.println("imp-intro");
						return handleStep(IMP_INTRO, step);
					}
				}
		  }

		  //or intro
			if (conclusion.getExpression().isDisj()) {
				Expr left = ((Disj)conclusion.getExpression()).left;
				Expr right = ((Disj)conclusion.getExpression()).right;
				if ((clause.getExpression().equals(left)
						|| clause.getExpression().equals(right))
						&& clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					//System.out.println("or-intro");
					return handleStep(OR_INTRO, step);
				}
			}

			//false elim
			if (clause.getExpression() instanceof BooleanExpr && !((BooleanExpr)clause.getExpression()).value && clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())){
				//System.out.println("false-elim");
				return handleStep(FALSE_ELIM, step);
			}

			//neg intro
			if (clause.getExpression() instanceof BooleanExpr
					&& !((BooleanExpr) clause.getExpression()).value
					&& conclusion.getExpression() instanceof NotExpr
					&& !conclusion.getAssumptions().contains(((NotExpr)conclusion.getExpression()).right)
					&& clause.getAssumptions().contains(((NotExpr)conclusion.getExpression()).right)
					&& clause.getAssumptions().size() == conclusion.getAssumptions().size() + 1
			) {
				Assumptions newAssumptions = new Assumptions(conclusion.getAssumptions());
				newAssumptions.getAssumptions().add(((NotExpr) conclusion.getExpression()).right);
				if (clause.getAssumptionsObject().equals(newAssumptions)) {
					//System.out.println("neg-intro");
					return handleStep(NEG_INTRO, step);
				}
			}

	  } else if (premisses.size()==2) {
			Clause clause1 = premisses.get(0);
			Clause clause2 = premisses.get(1);

			//and intro
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& conclusion.getExpression().isConj()) {

				Expr left = ((Conj) conclusion.getExpression()).left;
				Expr right = ((Conj) conclusion.getExpression()).right;
				if ((left.equals(clause1.getExpression()) && right.equals(clause2.getExpression())) || (left.equals(clause2.getExpression()) && right.equals(clause1.getExpression()))){
					//System.out.println("and-intro");
					return handleStep(AND_INTRO, step);
				}
			}

			//imp elim
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& ((clause1.getExpression().isImpl())
						|| clause2.getExpression().isImpl())

					&& (( clause1.getExpression().isImpl()
							&& clause1.getExpression().left.equals(clause2.getExpression())
							&& clause1.getExpression().right.equals(conclusion.getExpression()))
						|| (clause2.getExpression().isImpl()
							&& clause2.getExpression().left.equals(clause1.getExpression())
							&& clause2.getExpression().right.equals(conclusion.getExpression())))
				)
				{
					//System.out.println("imp-elim");
					return handleStep(IMP_ELIM, step);
				}

			//neg elim
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& ((clause1.getExpression() instanceof NotExpr
						&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
					|| (clause2.getExpression() instanceof NotExpr
						&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))
			){
				//System.out.println("neg-elim");
				return handleStep(NEG_ELIM, step);
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
					if (!assumption.equals(P) && !newAssumptions.getAssumptions().contains(assumption)) {
						newAssumptions.getAssumptions().add(assumption);
					}
				}

				if (newAssumptions.equals(conclusion.getAssumptionsObject())) {
					//System.out.println("neg-intro");
					return handleStep(NEG_INTRO, step);
				}
			}

		} else if (premisses.size()==3) {
			//or elim
			Expr R = conclusion.getExpression();
			Clause orClause = null;
			Clause clauseP = null;
			Clause clauseQ = null;


			for (int i = 0; i < 3; i++) {
				int j = (i+1)%3;
				int k = (i+2)%3;

				if (premisses.get(i).getExpression().isDisj()) {
					if (premisses.get(j).getAssumptions().contains(((Disj) (premisses.get(i).getExpression())).left) &&
							premisses.get(k).getAssumptions().contains(((Disj) (premisses.get(i).getExpression())).right) &&
							premisses.get(j).getExpression().equals(R) &&
							premisses.get(k).getExpression().equals(R)) {

						orClause = premisses.get(i);
						clauseP = premisses.get(j);
						clauseQ = premisses.get(k);
						break;
					} else if (premisses.get(k).getAssumptions().contains(((Disj) (premisses.get(i).getExpression())).left) &&
							premisses.get(j).getAssumptions().contains(((Disj) (premisses.get(i).getExpression())).right) &&
							premisses.get(k).getExpression().equals(R) &&
							premisses.get(j).getExpression().equals(R)) {

						orClause = premisses.get(i);
						clauseP = premisses.get(k);
						clauseQ = premisses.get(j);
						break;
					}
				}
			}

			if (orClause==null){System.out.println("invalid!");
				return false;
			}


			Assumptions gamma = orClause.getAssumptionsObject();
			Assumptions deltaP = new Assumptions(clauseP.getAssumptions());
			Assumptions thetaQ = new Assumptions(clauseQ.getAssumptions());
			Assumptions assumptions = new Assumptions(gamma.getAssumptions());

			for (Expr assumption : deltaP.getAssumptions()) {
				if (!assumptions.getAssumptions().contains(assumption) && !assumption.equals(((Disj)orClause.getExpression()).left)) {
					assumptions.getAssumptions().add(assumption);
				}
			}

			for (Expr assumption : thetaQ.getAssumptions()) {
				if (!assumptions.getAssumptions().contains(assumption) && !assumption.equals(((Disj)orClause.getExpression()).right)) {
					assumptions.getAssumptions().add(assumption);
				}
			}

			if (conclusion.getAssumptionsObject().equals(assumptions)) {
				//System.out.println("or-elim");
				return handleStep(OR_ELIM, step);
			}

		}
	    System.out.println("invalid!");
		return false;
  }

}
