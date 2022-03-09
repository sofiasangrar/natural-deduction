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

	/**
	 * parse full proof
	 * @param node root node of tree
	 * @return parsed proof
	 */
	public static Proof parse(StepNode node){
		Lexer.setLexString(node.getInput());
		Parser.clearError();
		Parser.t = Lexer.lex();
		node.setParsedInput(Clause.parse());
		node.setIncorrectSyntax(Parser.error);
		if (Parser.exception!=null){
			displayException(node.getUIElement(), Parser.exception);
		}

		//parse subtrees
		for (int i = 0; i < node.getChildren().size(); i++) {
			parse(node.getChildren().get(i));
		}
		return new Proof(node);
	}

	/**
	 * validate full proof
	 */
	public boolean isValid() {
		isValid = true;
		return isValid(root);
	}

	/**
	 * recursively check whether the proof is valid
	 * @return whether or not the proof is valid
	 */
	private boolean isValid(StepNode root) {
		//do not bother parsing proof steps if the syntax is invalid
		if (root.hasIncorrectSyntax()) {
			//displayException(root.getUIElement(), new SyntaxError());
			isValid=false;
		} else if (!checkStep(root)) {
			isValid = false;
		}

		//check validity of antecedent steps
		for (int i = 0; i < root.getChildren().size(); i++) {
			if (!isValid(root.getChildren().get(i))) {
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * display exception ot the relevant ui element
	 * @param ui ui element of node
	 * @param e exception to display
	 */
	private static void displayException(LeafNode ui, RuntimeException e) {
		if (ui!=null) {
			ui.displayException(e);
		}
	}

	/**
	 * check validity of a single step
	 * @param node node to check
	 * @return whether or not the step is valid
	 */
	public static boolean checkStep(StepNode node){
		//a step is defined as the node and itc children (the antecedents)
		for (StepNode child : node.getChildren()) {
			//if any child has incorrect syntax, do not check the step
			if (child.hasIncorrectSyntax()) {
				displayException(node.getUIElement(), new RuleException("Predecessors have incorrect syntax."));
				return false;
			}
		}
		//try to resolve current step with a pre-defined proof step, and compare with step provided in justification box
		Step stepGiven = determineStep(node.getPremisses(), node.getParsedInput());
		return handleStep(node.getUIElement(), node.getPremisses(), node.getParsedInput(), node.getStep(), stepGiven);

	}

	/**
	 * check whether the step matches the assigned step in the justification and provide error messages if not
	 * @param ui ui element of current step node
	 * @param premises list of premises in the step
	 * @param conclusion conclusion of proof step
	 * @param expected value provided in justification box
	 * @param actual type of step that the proof actually performs
	 * @return whether or not the step is valid
	 */
	public static boolean handleStep(LeafNode ui, ArrayList<Clause> premises, Clause conclusion, Step expected, Step actual) {
		//if the proof step actually being performed is unassigned, if means the step is invalid
		if (actual.equals(UNASSIGNED)) {
			try {
				findErrors(premises, conclusion, expected);
			} catch (RuntimeException e) {
				displayException(ui, e);
			}
			return false;
		}

		try {
			//if the step is valid, but does not match the assigned proof step
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

	/**
	 * determine the justification that should accompany a proof step
	 * @param premisses list of premisses of step
	 * @param conclusion conclusion of step
	 * @return the name of the step it appears to be
	 */
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

			//false intro
			if (clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject()) &&
					clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject()) &&
					((clause1.getExpression()instanceof NotExpr && clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right)) ||
							(clause2.getExpression()instanceof NotExpr && clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right))) &&
					conclusion.getExpression() instanceof BooleanExpr &&
					!((BooleanExpr) conclusion.getExpression()).value){
				return FALSE_INTRO;
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
						P = ((Disj)orClause.getExpression()).left;
						Q = ((Disj)orClause.getExpression()).right;
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
		//if no matching rule is found, it is "unassigned"
		return UNASSIGNED;
	}

	/**
	 * locate the place where the user may have made an error
	 * @param premisses premisses of step
	 * @param conclusion conclusion of step
	 * @param step step justification assigned by user
	 */
	private static void findErrors(ArrayList<Clause> premisses, Clause conclusion, Step step){
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
					throw new RuleException(ASSUMPTION, "conclusion does not contain any antecedent expression");
				}
				break;

			case TRUE_INTRO:
				if (size != 0) {
					throw new PremiseNumberException(TRUE_INTRO, 0, size);
				}
				if(!(conclusion.getExpression() instanceof BooleanExpr && ((BooleanExpr) conclusion.getExpression()).value)) {
					throw new NothingIntroducedException(TRUE_INTRO, "True");
				}
				break;

			case EXCL_MIDDLE:
				if (size != 0) {
					throw new PremiseNumberException(EXCL_MIDDLE, 0, size);
				}
				if(!conclusion.getExpression().isDisj()){
					throw new RuleException(EXCL_MIDDLE, "conclusion is not a disjunction");
				} else {
					right = ((Disj) conclusion.getExpression()).right;
					left = ((Disj) conclusion.getExpression()).left;
					if (!(right instanceof NotExpr && ((NotExpr)right).right.equals(left))) {
						throw new RuleException(EXCL_MIDDLE, "RHS of disjunction is not a negation of LHS");
					}
				}
				break;

			case AND_ELIM:
				if (size!=1) {
					throw new PremiseNumberException(AND_ELIM, 1, size);
				}
				clause1 = premisses.get(0);
				if (!clause1.getExpression().isConj()) {
					throw new NothingToEliminateException(AND_ELIM, "conjunction");
				}
				if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					throw new AssumptionsMismatchException();
				}
				left = ((Conj)clause1.getExpression()).left;
				right = ((Conj)clause1.getExpression()).right;
				if (!(conclusion.getExpression().equals(left) || conclusion.getExpression().equals(right))) {
					throw new RuleException(AND_ELIM, "conclusion does not match either side of conjunction");

				}

				break;

			case IMP_INTRO:
				if (!conclusion.getExpression().isImpl()) {
					throw new NothingIntroducedException(IMP_INTRO, "implication");
				}
				if (size!=1) {
					throw new PremiseNumberException(IMP_INTRO, 1, size);
				} else {
					clause1 = premisses.get(0);
					if (clause1.getAssumptions().size()==0) {
						throw new RuleException(IMP_INTRO, "no expression discharge in premise");
					}
					if (clause1.getAssumptions().size() - 1 > conclusion.getAssumptions().size()) {
						throw new RuleException(IMP_INTRO, "conclusion has too few antecedents");
					}
					if (clause1.getAssumptions().size() - 1 < conclusion.getAssumptions().size()) {
						throw new RuleException(IMP_INTRO, "conclusion has too many antecedents");
					}
					if (conclusion.getExpression().isImpl()) {
						if(!conclusion.getExpression().right.equals(clause1.getExpression())) {
							throw new RuleException(IMP_INTRO, "RHS of implication does not match conclusion of premise");
						}
						left = conclusion.getExpression().left;
						if (!(clause1.getAssumptions().contains(left))) {
							throw new RuleException(IMP_INTRO, "LHS of implication does not appear in premise's antecedents");
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
					throw new NothingIntroducedException(OR_INTRO, "disjunction");
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
							throw new RuleException(OR_INTRO, "neither side of disjunction appears in premise's conclusion");
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
						throw new NothingToEliminateException(FALSE_ELIM, "False");
					}
				}
				break;

			case NEG_INTRO:
				if (!(conclusion.getExpression() instanceof NotExpr)) {
					throw new NothingIntroducedException(NEG_INTRO, "negation");
				}
				if (size==1) {
					clause1 = premisses.get(0);
					if(!(clause1.getExpression() instanceof BooleanExpr && !(((BooleanExpr) clause1.getExpression()).value))) {
						throw new RuleException(NEG_INTRO, "RHS of premise is not 'F'");
					}
					if (!(clause1.getAssumptions().contains(((NotExpr)conclusion.getExpression()).right))) {
						throw new RuleException(NEG_INTRO, "conclusion does not appear in premise's antecedents");
					}
					Assumptions newAssumptions = new Assumptions(conclusion.getAssumptions());
					newAssumptions.getAssumptions().add(((NotExpr) conclusion.getExpression()).right);
					if (!(clause1.getAssumptionsObject().equals(newAssumptions))) {
						throw new AssumptionsMismatchException();
					}

					if (clause1.getAssumptions().size() - 1 > conclusion.getAssumptions().size()) {
						throw new RuleException(NEG_INTRO, "conclusion has too few antecedents");
					}
					if (clause1.getAssumptions().size() - 1 < conclusion.getAssumptions().size()) {
						throw new RuleException(NEG_INTRO, "conclusion has too many antecedents");
					}

				//negation introduction has two forms
				} else if (size==2) {
					clause1 = premisses.get(0);
					clause2 = premisses.get(1);
					if (!((clause1.getExpression() instanceof NotExpr
							&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
							|| (clause2.getExpression() instanceof NotExpr
							&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)
					))) {
						throw new RuleException(NEG_INTRO, "conclusions of premises are not negations of each other");
					}
					if (conclusion.getExpression() instanceof NotExpr) {
						Expr P = ((NotExpr) conclusion.getExpression()).right;
						if(!(clause1.getAssumptions().contains(P))) {
							throw new RuleException(NEG_INTRO, "premise 1 does not contain negation of conclusion as an antecedent");
						}
						if (!(clause2.getAssumptions().contains(P))) {
							throw new RuleException(NEG_INTRO, "premise 1 does not contain negation of conclusion as an antecedent");
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
					throw new RuleException(NEG_INTRO, "the number of premises is incorrect");
				}
				break;

			case AND_INTRO:
				if (!conclusion.getExpression().isConj()) {
					throw new NothingIntroducedException(AND_INTRO, "conjunction");
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
							throw new RuleException(AND_INTRO, "expressions in conjunction do not feature in premises");
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
						throw new NothingToEliminateException(IMP_ELIM, "implication");
					} else {
						if ( clause1.getExpression().isImpl()
								&& clause1.getExpression().left.equals(clause2.getExpression())
								&& !clause1.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException(IMP_ELIM, "conclusion does not match RHS of implication");
						}
						if ( clause1.getExpression().isImpl()
								&& !clause1.getExpression().left.equals(clause2.getExpression())
								&& clause1.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException(IMP_ELIM, "LHS of implication does not match other premise");
						}
						if (clause2.getExpression().isImpl()
								&& clause2.getExpression().left.equals(clause1.getExpression())
								&& !clause2.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException(IMP_ELIM, "conclusion does not match RHS of implication");

						}
						if (clause2.getExpression().isImpl()
								&& !clause2.getExpression().left.equals(clause1.getExpression())
								&& clause2.getExpression().right.equals(conclusion.getExpression())) {
							throw new RuleException(IMP_ELIM, "LHS of implication does not match other premise");
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
						throw new NothingToEliminateException(NEG_ELIM,"negation");
					} else if (!((clause1.getExpression() instanceof NotExpr
								&& clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right))
								|| (clause2.getExpression() instanceof NotExpr
								&& clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))) {
						throw new RuleException(NEG_ELIM, "conclusion of one premise is not negation of the other");
					}

				}
				break;

			case FALSE_INTRO:
				if (size!=2){
					throw new PremiseNumberException(FALSE_INTRO, 2, size);
				}
				clause1 = premisses.get(0);
				clause2 = premisses.get(1);

				if (!clause1.getAssumptionsObject().equals(clause2.getAssumptionsObject())){
					throw new AssumptionsMismatchException();
				}
				if (!clause1.getAssumptionsObject().equals(conclusion.getAssumptionsObject())) {
					throw new AssumptionsMismatchException();
				}
				if(!((clause1.getExpression()instanceof NotExpr && clause2.getExpression().equals(((NotExpr) clause1.getExpression()).right)) ||
								(clause2.getExpression()instanceof NotExpr && clause1.getExpression().equals(((NotExpr) clause2.getExpression()).right)))) {
					throw new RuleException(FALSE_INTRO, "conclusion of one premise is not negation of the other");
				}

				if(!(conclusion.getExpression() instanceof BooleanExpr &&
						!((BooleanExpr) conclusion.getExpression()).value)){
					throw new NothingIntroducedException(FALSE_INTRO, "False");
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
						throw new NothingToEliminateException(OR_ELIM, "disjunction");
					} else {
						P = ((Disj)orClause.getExpression()).left;
						Q = ((Disj)orClause.getExpression()).right;
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
							throw new RuleException(OR_ELIM, "conclusion does not match conclusions of premises");
						}

					}

					break;

				}
		}

	}

}

