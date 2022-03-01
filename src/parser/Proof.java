package parser;

import lexer.Lexer;
import natded.StepNode;
import natded.UI.LeafNode;
import natded.constants.Step;
import natded.exceptions.*;

import java.util.ArrayList;

import static natded.constants.Step.*;

public class Proof{

	private StepNode root;
	private boolean isValid = true;

	Proof(StepNode root) {
		this.root = root;
	}

	public static Proof parse(StepNode node){
		Lexer.setLexString(node.getInput());
		Parser.error = false;
		Parser.t = Lexer.lex();
		node.setParsedInput(Clause.parse());
		node.setIncorrectSyntax(Parser.error);

		for (int i = 0; i < node.getChildren().size(); i++) {
			parse(node.getChildren().get(i));
		}
		return new Proof(node);
	}

	public boolean isValid() {
		isValid = true;
		return isValid(root);
	}

	private boolean isValid(StepNode root) {
		if (root.hasIncorrectSyntax()) {
			displayException(root.getUIElement(), new SyntaxError());
			isValid=false;
		} else if (!checkStep(root)) {
			isValid = false;
		}
		for (int i = 0; i < root.getChildren().size(); i++) {
			if (!isValid(root.getChildren().get(i))) {
				isValid = false;
			}
		}
		return isValid;
	}

	private static void displayException(LeafNode ui, RuntimeException e) {
		if (ui!=null) {
			ui.displayException(e);
		} else {
			System.out.println(e.getMessage());
		}
	}

	public static boolean checkStep(StepNode node){
		for (StepNode child : node.getChildren()) {
			if (child.hasIncorrectSyntax()) {
				displayException(node.getUIElement(), new RuleException("Predecessors have incorrect syntax."));
				return false;
			}
		}
		Step stepGiven = determineStep(node.getPremisses(), node.getParsedInput());
		return handleStep(node.getUIElement(), node.getPremisses(), node.getParsedInput(), node.getStep(), stepGiven);

	}

	public static boolean handleStep(LeafNode ui, ArrayList<Clause> premises, Clause conclusion, Step expected, Step actual) {
		if (actual.equals(UNASSIGNED)) {
			try {
				findErrors(premises, conclusion, expected);
			} catch (RuntimeException e) {
				displayException(ui, e);
			}
			return false;
		}

		try {
			if (expected != actual) {
				throw new JustificationMismatchException(expected, actual);
			}
		} catch (JustificationMismatchException e) {
			displayException(ui, e);
			return false;
		}
		if (ui!=null) {
			ui.resetError();
		}
		return true;
	}

	public static Step determineStep(ArrayList<Clause> premisses, Clause conclusion) {
		if(premisses == null || premisses.size()==0){

			//assumption
			if (conclusion.getAssumptions().contains(conclusion.getExpression())) {
				return ASSUMPTION;
			}

			//true intro
			if(conclusion.getExpression() instanceof BooleanExpr && ((BooleanExpr) conclusion.getExpression()).value) {
				return TRUE_INTRO;
			}

			//excluded middle
			if(conclusion.getExpression().isDisj()){
				Expr left = ((Disj) conclusion.getExpression()).left;
				Expr right = ((Disj) conclusion.getExpression()).right;

				if (right instanceof NotExpr && ((NotExpr)right).right.equals(left)) {
					return EXCL_MIDDLE;

				}
			}
			return UNASSIGNED;
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
					return AND_ELIM;
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
						return IMP_INTRO;
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
					return OR_INTRO;

				}
			}

			//false elim
			if (clause.getExpression() instanceof BooleanExpr && !((BooleanExpr)clause.getExpression()).value && clause.getAssumptionsObject().equals(conclusion.getAssumptionsObject())){
				return FALSE_ELIM;

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
					return NEG_INTRO;
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
					return AND_INTRO;
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
				return IMP_ELIM;

			}

			//neg elim
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())
					&& clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())
					&& ((clause1.getExpression() instanceof NotExpr
					&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
					|| (clause2.getExpression() instanceof NotExpr
					&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))
			){
				return NEG_ELIM;
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
					return NEG_INTRO;

				}
			}

		} else if (premisses.size()==3) {
			//or elim
			Expr R = conclusion.getExpression();
			Clause orClause;
			Clause clauseP = null;
			Clause clauseQ = null;
			Expr P;
			Expr Q;


			for (int i = 0; i < 3; i++) {
				int j = (i + 1) % 3;
				int k = (i + 2) % 3;

				if (premisses.get(i).getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					orClause = premisses.get(i);
					if (premisses.get(i).getExpression().isDisj()) {
						P = orClause.getExpression().left;
						Q = orClause.getExpression().right;
						Assumptions pa = new Assumptions(orClause.getAssumptions());
						Assumptions qa = new Assumptions(orClause.getAssumptions());

						pa.getAssumptions().add(P);
						qa.getAssumptions().add(Q);

						if (premisses.get(j).getAssumptionsObject().equals(pa) && premisses.get(k).getAssumptionsObject().equals(qa)) {
							clauseP = premisses.get(j);
							clauseQ = premisses.get(k);
							break;
						} else if (premisses.get(k).getAssumptionsObject().equals(pa) && premisses.get(j).getAssumptionsObject().equals(qa)) {
							clauseP = premisses.get(k);
							clauseQ = premisses.get(j);
							break;
						}
					}

				}
			}
			if (clauseP==null || clauseQ==null) {
				return UNASSIGNED;
			}
			if (clauseP.getExpression().equals(R) && clauseQ.getExpression().equals(R)) {
				return OR_ELIM;
			}

		}
		return UNASSIGNED;
	}

	private static void checkNumberOfAntecedents(Step step, int expected, int actual, LeafNode ui) {
		try {
			if (actual != 0) {
				throw new PremiseNumberException(step, expected, actual);
			}
		} catch (PremiseNumberException e) {
			displayException(ui, e);
		}
	}

	public static void findErrors(ArrayList<Clause> premisses, Clause conclusion, Step step){
		int size =0;
		if (premisses!=null) {
			size = premisses.size();
		}
		Clause clause1;
		Clause clause2;
		Expr left;
		Expr right;
		switch (step) {
			case UNASSIGNED:
				throw new NoJustificationException();
			case ASSUMPTION:
				if (size != 0) {
					throw new PremiseNumberException(ASSUMPTION, 0, size);
				}
				if (!conclusion.getAssumptions().contains(conclusion.getExpression())) {
					throw new RuleException("RHS must contain an element from LHS");
				}
				break;

			case TRUE_INTRO:
				if (size != 0) {
					throw new PremiseNumberException(TRUE_INTRO, 0, size);
				}
				if(!(conclusion.getExpression() instanceof BooleanExpr && ((BooleanExpr) conclusion.getExpression()).value)) {
					throw new NothingIntroducedException("True");
				}
				break;

			case EXCL_MIDDLE:
				if (size != 0) {
					throw new PremiseNumberException(EXCL_MIDDLE, 0, size);
				}
				if(!conclusion.getExpression().isDisj()){
					throw new RuleException("RHS must be a disjunction");
				} else {
					right = ((Disj) conclusion.getExpression()).right;
					left = ((Disj) conclusion.getExpression()).left;
					if (!(right instanceof NotExpr && ((NotExpr)right).right.equals(left))) {
						throw new RuleException("RHS of disjunction must be negation of LHS");
					}
				}
				break;

			case AND_ELIM:
				if (size!=1) {
					throw new PremiseNumberException(AND_ELIM, 1, size);
				}
				clause1 = premisses.get(0);
				if (!clause1.getExpression().isConj()) {
					throw new NothingToEliminateException("conjunction");
				}
				if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					throw new AssumptionsMismatchException();
				}
				left = ((Conj)clause1.getExpression()).left;
				right = ((Conj)clause1.getExpression()).right;
				if (!(conclusion.getExpression().equals(left) || conclusion.getExpression().equals(right))) {
					throw new RuleException("RHS of conclusion must be taken from conjunction");

				}

				break;

			case IMP_INTRO:
				if (!conclusion.getExpression().isImpl()) {
					throw new NothingIntroducedException("implication");
				}
				if (size!=1) {
					throw new PremiseNumberException(IMP_INTRO, 1, size);
				} else {
					clause1 = premisses.get(0);
					if (clause1.getAssumptions().size()==0) {
						throw new RuleException("LHS of premise must contain assumption to discharge");
					}
					if (clause1.getAssumptions().size() - 1 > conclusion.getAssumptions().size()) {
						throw new RuleException("Premise contains too many assumptions");
					}
					if (clause1.getAssumptions().size() - 1 < conclusion.getAssumptions().size()) {
						throw new RuleException("Premise contains too few assumptions");
					}
					if (conclusion.getExpression().isImpl()) {
						if(!conclusion.getExpression().right.equals(clause1.getExpression())) {
							throw new RuleException("RHS of conclusion does not match RHS of premise");
						}
						left = conclusion.getExpression().left;
						if (!(clause1.getAssumptions().contains(left))) {
							throw new RuleException("Assumption not discharged properly");
						} else {
							ArrayList<Expr> assumptions = new ArrayList<>(conclusion.getAssumptions());
							assumptions.add(conclusion.getExpression().left);
							Assumptions newAssumptions = new Assumptions(assumptions);
							if (!(newAssumptions.equals(clause1.getAssumptionsObject()))){
								throw new AssumptionsMismatchException();
							}
						}
					}
				}
				break;

			case OR_INTRO:
				if (!conclusion.getExpression().isDisj()) {
					throw new NothingIntroducedException("disjunction");
				}
				if (size!=1) {
					throw new PremiseNumberException(OR_INTRO, 1, size);
				} else {
					clause1 = premisses.get(0);
					if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (conclusion.getExpression().isDisj()) {
						left = ((Disj)conclusion.getExpression()).left;
						right = ((Disj)conclusion.getExpression()).right;
						if (!(clause1.getExpression().equals(left) || clause1.getExpression().equals(right))) {
							throw new RuleException("RHS or LHS of disjunction must match RHS of premise");
						}
					}
				}
				break;

			case FALSE_ELIM:
				if (size!=1) {
					throw new PremiseNumberException(FALSE_ELIM, 1, size);
				} else {
					clause1 = premisses.get(0);
					if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if(!(clause1.getExpression() instanceof BooleanExpr && !(((BooleanExpr) clause1.getExpression()).value))) {
						throw new NothingToEliminateException("False");
					}
				}
				break;

			case NEG_INTRO:
				if (!(conclusion.getExpression() instanceof NotExpr)) {
					throw new NothingIntroducedException("negation");
				}
				if (size==1) {
					clause1 = premisses.get(0);
					if(!(clause1.getExpression() instanceof BooleanExpr && !(((BooleanExpr) clause1.getExpression()).value))) {
						throw new RuleException("RHS of premise must be 'F'");
					}
					if (!(clause1.getAssumptions().contains(((NotExpr)conclusion.getExpression()).right))) {
						throw new RuleException("Assumptions of premise do not contain RHS of conclusion");
					}
					Assumptions newAssumptions = new Assumptions(conclusion.getAssumptions());
					newAssumptions.getAssumptions().add(((NotExpr) conclusion.getExpression()).right);
					if (!(clause1.getAssumptionsObject().equals(newAssumptions))) {
						throw new RuleException("Assumptions mismatch");
					}

					if (clause1.getAssumptions().size() - 1 > conclusion.getAssumptions().size()) {
						throw new RuleException("Premise contains too many assumptions");
					}
					if (clause1.getAssumptions().size() - 1 < conclusion.getAssumptions().size()) {
						throw new RuleException("Premise contains too few assumptions");
					}

				} else if (size==2) {
					clause1 = premisses.get(0);
					clause2 = premisses.get(1);
					if (!((clause1.getExpression() instanceof NotExpr
							&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
							|| (clause2.getExpression() instanceof NotExpr
							&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)
					))) {
						throw new RuleException("RHS of one premise must be negation of the other");
					}
					if (conclusion.getExpression() instanceof NotExpr) {
						Expr P = ((NotExpr) conclusion.getExpression()).right;
						if(!(clause1.getAssumptions().contains(P))) {
							throw new RuleException("Premise 1 must contain negation of conclusion's RHS");
						}
						if (!(clause2.getAssumptions().contains(P))) {
							throw new RuleException("Premise 2 must contain negation of conclusion's RHS");
						}
						Assumptions newAssumptions = new Assumptions();

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

						if (!newAssumptions.equals(conclusion.getAssumptionsObject())) {
							throw new AssumptionsMismatchException();
						}
					}
				} else {
					throw new RuleException("Incorrect number of premises");
				}
				break;

			case AND_INTRO:
				if (!conclusion.getExpression().isConj()) {
					throw new NothingIntroducedException("conjunction");
				}
				if (size!=2) {
					throw new PremiseNumberException(AND_INTRO, 2, size);
				} else {
					clause1 = premisses.get(0);
					clause2 = premisses.get(1);
					if (!clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause2.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (conclusion.getExpression().isConj()) {
						left = ((Conj) conclusion.getExpression()).left;
						right = ((Conj) conclusion.getExpression()).right;
						if (!((left.equals(clause1.getExpression()) && right.equals(clause2.getExpression())) || left.equals(clause2.getExpression()) && right.equals(clause1.getExpression()))) {
							throw new RuleException("premises do not match conclusion");
						}
					}
				}
				break;

			case IMP_ELIM:
				if (size!=2) {
					throw new PremiseNumberException(IMP_ELIM, 2, size);
				} else {
					clause1 = premisses.get(0);
					clause2 = premisses.get(1);
					if (!clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause2.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!((clause1.getExpression().isImpl()) || clause2.getExpression().isImpl())) {
						throw new NothingToEliminateException("implication");
					} else {
						if ( clause1.getExpression().isImpl()
								&& clause1.getExpression().left.equals(clause2.getExpression())
								&& !clause1.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException("Conclusion does not match RHS of implication");
						}
						if ( clause1.getExpression().isImpl()
								&& !clause1.getExpression().left.equals(clause2.getExpression())
								&& clause1.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException("LHS of implication does not match other premise");
						}
						if (clause2.getExpression().isImpl()
								&& clause2.getExpression().left.equals(clause1.getExpression())
								&& !clause2.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException("Conclusion does not match RHS of implication");

						}
						if (clause2.getExpression().isImpl()
								&& !clause2.getExpression().left.equals(clause1.getExpression())
								&& clause2.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException("LHS of implication does not match other premise");
						}
					}
				}
				break;

			case NEG_ELIM:
				if (size!=2) {
					throw new PremiseNumberException(NEG_ELIM, 2, size);
				} else {
					clause1 = premisses.get(0);
					clause2 = premisses.get(1);
					if (!clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!clause2.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
						throw new AssumptionsMismatchException();
					}
					if (!(clause1.getExpression() instanceof NotExpr || clause2.getExpression() instanceof NotExpr)) {
						throw new NothingToEliminateException("negation");
					} else if (!((clause1.getExpression() instanceof NotExpr
								&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
								|| (clause2.getExpression() instanceof NotExpr
								&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))) {
						throw new RuleException("Conclusion of one premise must be negation of another");
					}

				}
				break;

			case OR_ELIM:
				if (size!=3) {
					throw new PremiseNumberException(OR_ELIM, 3, size);
				} else {

					//or elim
					Expr R = conclusion.getExpression();
					Clause orClause = null;
					Clause clauseP = null;
					Clause clauseQ = null;
					Expr P;
					Expr Q;

					for (int i = 0; i < 3; i++) {
						if (premisses.get(i).getAssumptionsObject().equals(conclusion.getAssumptionsObject())){
							orClause = premisses.get(i);
						}
					}

					if (orClause==null) {
						throw new AssumptionsMismatchException();
					}
					if (!orClause.getExpression().isDisj()) {
						throw new NothingToEliminateException("disjunction");
					} else {
						P = orClause.getExpression().left;
						Q = orClause.getExpression().right;
						Assumptions pa = new Assumptions(orClause.getAssumptions());
						Assumptions qa = new Assumptions(orClause.getAssumptions());

						pa.getAssumptions().add(P);
						qa.getAssumptions().add(Q);

						for (int i = 0; i < 3; i++) {
							int j = (i + 1) % 3;
							int k = (i + 2) % 3;
							if (premisses.get(j).getAssumptionsObject().equals(pa) && premisses.get(k).getAssumptionsObject().equals(qa)) {
								clauseP = premisses.get(j);
								clauseQ = premisses.get(k);
							} else if (premisses.get(k).getAssumptionsObject().equals(pa) && premisses.get(j).getAssumptionsObject().equals(qa)) {
								clauseP = premisses.get(k);
								clauseQ = premisses.get(j);
							}
						}
						if (clauseP == null || clauseQ == null) {
							throw new AssumptionsMismatchException();
						}

						if (!(clauseP.getExpression().equals(R) && clauseQ.getExpression().equals(R))) {
							throw new RuleException("Conclusion does not match");
						}

					}

					break;

				}
		}

	}

}

