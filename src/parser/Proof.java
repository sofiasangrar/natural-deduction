package parser;

import natded.StepNode;
import natded.UI.Node;
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
		node.parse();

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
			isValid=false;
		} else if (!checkStep(root)) {
			isValid = false;
		}

		//check validity of premises
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
	private static void displayException(Node ui, RuntimeException e) {
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
		//a step is defined as the node and itc children (the premises)
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
	public static boolean handleStep(Node ui, ArrayList<Sequent> premises, Sequent conclusion, Step expected, Step actual) {
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
	public static Step determineStep(ArrayList<Sequent> premisses, Sequent conclusion) {
		if(premisses == null || premisses.size()==0){

			//assumption
			if (conclusion.getAntecedents().contains(conclusion.getConclusion())) {
				return ASSUMPTION;
			}

			//true intro
			if(conclusion.getConclusion() instanceof BooleanExpr && ((BooleanExpr) conclusion.getConclusion()).value) {
				return TRUE_INTRO;
			}

			//excluded middle
			if(conclusion.getConclusion().isDisj()){
				Expr left = ((Disj) conclusion.getConclusion()).left;
				Expr right = ((Disj) conclusion.getConclusion()).right;

				if (right instanceof NotExpr && ((NotExpr)right).right.equals(left)) {
					return EXCL_MIDDLE;

				}
			}
			return UNASSIGNED;
		}

		if (premisses.size()==1) {
			Sequent sequent = premisses.get(0);

			//and elim
			if (sequent.getConclusion().isConj()) {
				Expr left = ((Conj) sequent.getConclusion()).left;
				Expr right = ((Conj) sequent.getConclusion()).right;
				if ((conclusion.getConclusion().equals(left)
						|| conclusion.getConclusion().equals(right))
						&& sequent.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
					return AND_ELIM;
				}
			}

			//imp intro
			if (sequent.getAntecedents().size()!=0 &&
					conclusion.getConclusion().isImpl() &&
					sequent.getAntecedents().size() - 1 == conclusion.getAntecedents().size()) {

				Expr left = conclusion.getConclusion().left;
				if (sequent.getAntecedents().contains(left)
						&& !conclusion.getAntecedents().contains(left)) {
					ArrayList<Expr> assumptions = new ArrayList<>(conclusion.getAntecedents());
					assumptions.add(conclusion.getConclusion().left);
					Antecedents newAntecedents = new Antecedents(assumptions);
					if (newAntecedents.equals(sequent.getAntecedentsObject()) && conclusion.getConclusion().right.equals(sequent.getConclusion())) {
						return IMP_INTRO;
					}
				}
			}

			//or intro
			if (conclusion.getConclusion().isDisj()) {
				Expr left = ((Disj)conclusion.getConclusion()).left;
				Expr right = ((Disj)conclusion.getConclusion()).right;
				if ((sequent.getConclusion().equals(left)
						|| sequent.getConclusion().equals(right))
						&& sequent.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
					return OR_INTRO;

				}
			}

			//false elim
			if (sequent.getConclusion() instanceof BooleanExpr && !((BooleanExpr) sequent.getConclusion()).value && sequent.getAntecedentsObject().equals(conclusion.getAntecedentsObject())){
				return FALSE_ELIM;

			}

			//neg intro
			if (sequent.getConclusion() instanceof BooleanExpr
					&& !((BooleanExpr) sequent.getConclusion()).value
					&& conclusion.getConclusion() instanceof NotExpr
					&& !conclusion.getAntecedents().contains(((NotExpr)conclusion.getConclusion()).right)
					&& sequent.getAntecedents().contains(((NotExpr)conclusion.getConclusion()).right)
					&& sequent.getAntecedents().size() == conclusion.getAntecedents().size() + 1
			) {
				Antecedents newAntecedents = new Antecedents(conclusion.getAntecedents());
				newAntecedents.getAntecedents().add(((NotExpr) conclusion.getConclusion()).right);
				if (sequent.getAntecedentsObject().equals(newAntecedents)) {
					return NEG_INTRO;
				}
			}

		} else if (premisses.size()==2) {
			Sequent sequent1 = premisses.get(0);
			Sequent sequent2 = premisses.get(1);

			//and intro
			if (sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())
					&& sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())
					&& conclusion.getConclusion().isConj()) {

				Expr left = ((Conj) conclusion.getConclusion()).left;
				Expr right = ((Conj) conclusion.getConclusion()).right;
				if ((left.equals(sequent1.getConclusion()) && right.equals(sequent2.getConclusion())) || (left.equals(sequent2.getConclusion()) && right.equals(sequent1.getConclusion()))){
					return AND_INTRO;
				}
			}

			//imp elim
			if (sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())
					&& sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())
					&& ((sequent1.getConclusion().isImpl())
					|| sequent2.getConclusion().isImpl())

					&& (( sequent1.getConclusion().isImpl()
					&& sequent1.getConclusion().left.equals(sequent2.getConclusion())
					&& sequent1.getConclusion().right.equals(conclusion.getConclusion()))
					|| (sequent2.getConclusion().isImpl()
					&& sequent2.getConclusion().left.equals(sequent1.getConclusion())
					&& sequent2.getConclusion().right.equals(conclusion.getConclusion())))
			)
			{
				return IMP_ELIM;

			}

			//neg elim
			if (sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())
					&& sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())
					&& ((sequent1.getConclusion() instanceof NotExpr
					&& sequent2.getConclusion().equals(((NotExpr) sequent1.getConclusion()).right))
					|| (sequent2.getConclusion() instanceof NotExpr
					&& sequent1.getConclusion().equals(((NotExpr) sequent2.getConclusion()).right)))
			){
				return NEG_ELIM;
			}

			//neg-intro
			if (sequent1.getAntecedents().size()!=0
					&& sequent2.getAntecedents().size()!=0
					&& ((sequent1.getConclusion() instanceof NotExpr
					&& sequent2.getConclusion().equals(((NotExpr) sequent1.getConclusion()).right))
					|| (sequent2.getConclusion() instanceof NotExpr
					&& sequent1.getConclusion().equals(((NotExpr) sequent2.getConclusion()).right)
			))
					&& conclusion.getConclusion() instanceof NotExpr
					&& sequent1.getAntecedents().contains(((NotExpr) conclusion.getConclusion()).right)
					&& sequent2.getAntecedents().contains(((NotExpr) conclusion.getConclusion()).right)
					&& !conclusion.getAntecedents().contains(((NotExpr) conclusion.getConclusion()).right)
			) {

				Antecedents newAntecedents = new Antecedents();
				Expr P = ((NotExpr) conclusion.getConclusion()).right;

				for (Expr assumption : sequent1.getAntecedents()) {
					if (!assumption.equals(P)) {
						newAntecedents.getAntecedents().add(assumption);
					}
				}

				for (Expr assumption : sequent2.getAntecedents()) {
					if (!assumption.equals(P) && !newAntecedents.getAntecedents().contains(assumption)) {
						newAntecedents.getAntecedents().add(assumption);
					}
				}

				if (newAntecedents.equals(conclusion.getAntecedentsObject())) {
					return NEG_INTRO;

				}
			}

		} else if (premisses.size()==3) {
			//or elim
			Expr R = conclusion.getConclusion();
			Sequent orSequent;
			Sequent sequentP = null;
			Sequent sequentQ = null;
			Expr P;
			Expr Q;


			for (int i = 0; i < 3; i++) {
				int j = (i + 1) % 3;
				int k = (i + 2) % 3;

				if (premisses.get(i).getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
					orSequent = premisses.get(i);
					if (premisses.get(i).getConclusion().isDisj()) {
						P = ((Disj) orSequent.getConclusion()).left;
						Q = ((Disj) orSequent.getConclusion()).right;
						Antecedents pa = new Antecedents(orSequent.getAntecedents());
						Antecedents qa = new Antecedents(orSequent.getAntecedents());

						pa.getAntecedents().add(P);
						qa.getAntecedents().add(Q);

						if (premisses.get(j).getAntecedentsObject().equals(pa) && premisses.get(k).getAntecedentsObject().equals(qa)) {
							sequentP = premisses.get(j);
							sequentQ = premisses.get(k);
							break;
						} else if (premisses.get(k).getAntecedentsObject().equals(pa) && premisses.get(j).getAntecedentsObject().equals(qa)) {
							sequentP = premisses.get(k);
							sequentQ = premisses.get(j);
							break;
						}
					}

				}
			}
			if (sequentP ==null || sequentQ ==null) {
				return UNASSIGNED;
			}
			if (sequentP.getConclusion().equals(R) && sequentQ.getConclusion().equals(R)) {
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
	private static void findErrors(ArrayList<Sequent> premisses, Sequent conclusion, Step step){
		int size =0;
		if (premisses!=null) {
			size = premisses.size();
		}
		Sequent sequent1;
		Sequent sequent2;
		Expr left;
		Expr right;
		switch (step) {
			case UNASSIGNED:
				throw new NoJustificationException();
			case ASSUMPTION:
				if (size != 0) {
					throw new PremiseNumberException(ASSUMPTION, 0, size);
				}
				if (!conclusion.getAntecedents().contains(conclusion.getConclusion())) {
					throw new RuleException(ASSUMPTION, "conclusion does not contain any antecedent expression");
				}
				break;

			case TRUE_INTRO:
				if (size != 0) {
					throw new PremiseNumberException(TRUE_INTRO, 0, size);
				}
				if(!(conclusion.getConclusion() instanceof BooleanExpr && ((BooleanExpr) conclusion.getConclusion()).value)) {
					throw new NothingIntroducedException(TRUE_INTRO, "True");
				}
				break;

			case EXCL_MIDDLE:
				if (size != 0) {
					throw new PremiseNumberException(EXCL_MIDDLE, 0, size);
				}
				if(!conclusion.getConclusion().isDisj()){
					throw new RuleException(EXCL_MIDDLE, "conclusion is not a disjunction");
				} else {
					right = ((Disj) conclusion.getConclusion()).right;
					left = ((Disj) conclusion.getConclusion()).left;
					if (!(right instanceof NotExpr && ((NotExpr)right).right.equals(left))) {
						throw new RuleException(EXCL_MIDDLE, "RHS of disjunction is not a negation of LHS");
					}
				}
				break;

			case AND_ELIM:
				if (size!=1) {
					throw new PremiseNumberException(AND_ELIM, 1, size);
				}
				sequent1 = premisses.get(0);
				if (!sequent1.getConclusion().isConj()) {
					throw new NothingToEliminateException(AND_ELIM, "conjunction");
				}
				if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
					throw new AntecedentsMismatchException();
				}
				left = ((Conj) sequent1.getConclusion()).left;
				right = ((Conj) sequent1.getConclusion()).right;
				if (!(conclusion.getConclusion().equals(left) || conclusion.getConclusion().equals(right))) {
					throw new RuleException(AND_ELIM, "conclusion does not match either side of conjunction");

				}

				break;

			case IMP_INTRO:
				if (!conclusion.getConclusion().isImpl()) {
					throw new NothingIntroducedException(IMP_INTRO, "implication");
				}
				if (size!=1) {
					throw new PremiseNumberException(IMP_INTRO, 1, size);
				} else {
					sequent1 = premisses.get(0);
					if (sequent1.getAntecedents().size()==0) {
						throw new RuleException(IMP_INTRO, "no expression discharge in premise");
					}
					if (sequent1.getAntecedents().size() - 1 > conclusion.getAntecedents().size()) {
						throw new RuleException(IMP_INTRO, "conclusion has too few antecedents");
					}
					if (sequent1.getAntecedents().size() - 1 < conclusion.getAntecedents().size()) {
						throw new RuleException(IMP_INTRO, "conclusion has too many antecedents");
					}
					if (conclusion.getConclusion().isImpl()) {
						if(!conclusion.getConclusion().right.equals(sequent1.getConclusion())) {
							throw new RuleException(IMP_INTRO, "RHS of implication does not match conclusion of premise");
						}
						left = conclusion.getConclusion().left;
						if (!(sequent1.getAntecedents().contains(left))) {
							throw new RuleException(IMP_INTRO, "LHS of implication does not appear in premise's antecedents");
						} else {
							ArrayList<Expr> assumptions = new ArrayList<>(conclusion.getAntecedents());
							assumptions.add(conclusion.getConclusion().left);
							Antecedents newAntecedents = new Antecedents(assumptions);
							if (!(newAntecedents.equals(sequent1.getAntecedentsObject()))){
								throw new AntecedentsMismatchException();
							}
						}
					}
				}
				break;

			case OR_INTRO:
				if (!conclusion.getConclusion().isDisj()) {
					throw new NothingIntroducedException(OR_INTRO, "disjunction");
				}
				if (size!=1) {
					throw new PremiseNumberException(OR_INTRO, 1, size);
				} else {
					sequent1 = premisses.get(0);
					if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (conclusion.getConclusion().isDisj()) {
						left = ((Disj)conclusion.getConclusion()).left;
						right = ((Disj)conclusion.getConclusion()).right;
						if (!(sequent1.getConclusion().equals(left) || sequent1.getConclusion().equals(right))) {
							throw new RuleException(OR_INTRO, "neither side of disjunction appears in premise's conclusion");
						}
					}
				}
				break;

			case FALSE_ELIM:
				if (size!=1) {
					throw new PremiseNumberException(FALSE_ELIM, 1, size);
				} else {
					sequent1 = premisses.get(0);
					if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if(!(sequent1.getConclusion() instanceof BooleanExpr && !(((BooleanExpr) sequent1.getConclusion()).value))) {
						throw new NothingToEliminateException(FALSE_ELIM, "False");
					}
				}
				break;

			case NEG_INTRO:
				if (!(conclusion.getConclusion() instanceof NotExpr)) {
					throw new NothingIntroducedException(NEG_INTRO, "negation");
				}
				if (size==1) {
					sequent1 = premisses.get(0);
					if(!(sequent1.getConclusion() instanceof BooleanExpr && !(((BooleanExpr) sequent1.getConclusion()).value))) {
						throw new RuleException(NEG_INTRO, "RHS of premise is not 'F'");
					}
					if (!(sequent1.getAntecedents().contains(((NotExpr)conclusion.getConclusion()).right))) {
						throw new RuleException(NEG_INTRO, "conclusion does not appear in premise's antecedents");
					}
					Antecedents newAntecedents = new Antecedents(conclusion.getAntecedents());
					newAntecedents.getAntecedents().add(((NotExpr) conclusion.getConclusion()).right);
					if (!(sequent1.getAntecedentsObject().equals(newAntecedents))) {
						throw new AntecedentsMismatchException();
					}

					if (sequent1.getAntecedents().size() - 1 > conclusion.getAntecedents().size()) {
						throw new RuleException(NEG_INTRO, "conclusion has too few antecedents");
					}
					if (sequent1.getAntecedents().size() - 1 < conclusion.getAntecedents().size()) {
						throw new RuleException(NEG_INTRO, "conclusion has too many antecedents");
					}

				//negation introduction has two forms
				} else if (size==2) {
					sequent1 = premisses.get(0);
					sequent2 = premisses.get(1);
					if (!((sequent1.getConclusion() instanceof NotExpr
							&& sequent2.getConclusion().equals(((NotExpr) sequent1.getConclusion()).right))
							|| (sequent2.getConclusion() instanceof NotExpr
							&& sequent1.getConclusion().equals(((NotExpr) sequent2.getConclusion()).right)
					))) {
						throw new RuleException(NEG_INTRO, "conclusions of premises are not negations of each other");
					}
					if (conclusion.getConclusion() instanceof NotExpr) {
						Expr P = ((NotExpr) conclusion.getConclusion()).right;
						if(!(sequent1.getAntecedents().contains(P))) {
							throw new RuleException(NEG_INTRO, "premise 1 does not contain negation of conclusion as an antecedent");
						}
						if (!(sequent2.getAntecedents().contains(P))) {
							throw new RuleException(NEG_INTRO, "premise 1 does not contain negation of conclusion as an antecedent");
						}
						Antecedents newAntecedents = new Antecedents();

						for (Expr assumption : sequent1.getAntecedents()) {
							if (!assumption.equals(P)) {
								newAntecedents.getAntecedents().add(assumption);
							}
						}

						for (Expr assumption : sequent2.getAntecedents()) {
							if (!assumption.equals(P) && !newAntecedents.getAntecedents().contains(assumption)) {
								newAntecedents.getAntecedents().add(assumption);
							}
						}

						if (!newAntecedents.equals(conclusion.getAntecedentsObject())) {
							throw new AntecedentsMismatchException();
						}
					}
				} else {
					throw new RuleException(NEG_INTRO, "the number of premises is incorrect");
				}
				break;

			case AND_INTRO:
				if (!conclusion.getConclusion().isConj()) {
					throw new NothingIntroducedException(AND_INTRO, "conjunction");
				}
				if (size!=2) {
					throw new PremiseNumberException(AND_INTRO, 2, size);
				} else {
					sequent1 = premisses.get(0);
					sequent2 = premisses.get(1);
					if (!sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent2.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (conclusion.getConclusion().isConj()) {
						left = ((Conj) conclusion.getConclusion()).left;
						right = ((Conj) conclusion.getConclusion()).right;
						if (!((left.equals(sequent1.getConclusion()) && right.equals(sequent2.getConclusion())) || left.equals(sequent2.getConclusion()) && right.equals(sequent1.getConclusion()))) {
							throw new RuleException(AND_INTRO, "expressions in conjunction do not feature in premises");
						}
					}
				}
				break;

			case IMP_ELIM:
				if (size!=2) {
					throw new PremiseNumberException(IMP_ELIM, 2, size);
				} else {
					sequent1 = premisses.get(0);
					sequent2 = premisses.get(1);
					if (!sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent2.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!((sequent1.getConclusion().isImpl()) || sequent2.getConclusion().isImpl())) {
						throw new NothingToEliminateException(IMP_ELIM, "implication");
					} else {
						if ( sequent1.getConclusion().isImpl()
								&& sequent1.getConclusion().left.equals(sequent2.getConclusion())
								&& !sequent1.getConclusion().right.equals(conclusion.getConclusion())) {
							throw new RuleException(IMP_ELIM, "conclusion does not match RHS of implication");
						}
						if ( sequent1.getConclusion().isImpl()
								&& !sequent1.getConclusion().left.equals(sequent2.getConclusion())
								&& sequent1.getConclusion().right.equals(conclusion.getConclusion())) {
							throw new RuleException(IMP_ELIM, "LHS of implication does not match other premise");
						}
						if (sequent2.getConclusion().isImpl()
								&& sequent2.getConclusion().left.equals(sequent1.getConclusion())
								&& !sequent2.getConclusion().right.equals(conclusion.getConclusion())) {
							throw new RuleException(IMP_ELIM, "conclusion does not match RHS of implication");

						}
						if (sequent2.getConclusion().isImpl()
								&& !sequent2.getConclusion().left.equals(sequent1.getConclusion())
								&& sequent2.getConclusion().right.equals(conclusion.getConclusion())) {
							throw new RuleException(IMP_ELIM, "LHS of implication does not match other premise");
						}
					}
				}
				break;

			case NEG_ELIM:
				if (size!=2) {
					throw new PremiseNumberException(NEG_ELIM, 2, size);
				} else {
					sequent1 = premisses.get(0);
					sequent2 = premisses.get(1);
					if (!sequent1.getAntecedentsObject().equals(sequent2.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent1.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!sequent2.getAntecedentsObject().equals(conclusion.getAntecedentsObject())) {
						throw new AntecedentsMismatchException();
					}
					if (!(sequent1.getConclusion() instanceof NotExpr || sequent2.getConclusion() instanceof NotExpr)) {
						throw new NothingToEliminateException(NEG_ELIM,"negation");
					} else if (!((sequent1.getConclusion() instanceof NotExpr
								&& sequent2.getConclusion().equals(((NotExpr) sequent1.getConclusion()).right))
								|| (sequent2.getConclusion() instanceof NotExpr
								&& sequent1.getConclusion().equals(((NotExpr) sequent2.getConclusion()).right)))) {
						throw new RuleException(NEG_ELIM, "conclusion of one premise is not negation of the other");
					}

				}
				break;

			case OR_ELIM:
				if (size!=3) {
					throw new PremiseNumberException(OR_ELIM, 3, size);
				} else {

					//or elim
					Expr R = conclusion.getConclusion();
					Sequent orSequent = null;
					Sequent sequentP = null;
					Sequent sequentQ = null;
					Expr P;
					Expr Q;

					for (int i = 0; i < 3; i++) {
						if (premisses.get(i).getAntecedentsObject().equals(conclusion.getAntecedentsObject())){
							orSequent = premisses.get(i);
						}
					}

					if (orSequent ==null) {
						throw new AntecedentsMismatchException();
					}
					if (!orSequent.getConclusion().isDisj()) {
						throw new NothingToEliminateException(OR_ELIM, "disjunction");
					} else {
						P = ((Disj) orSequent.getConclusion()).left;
						Q = ((Disj) orSequent.getConclusion()).right;
						Antecedents pa = new Antecedents(orSequent.getAntecedents());
						Antecedents qa = new Antecedents(orSequent.getAntecedents());

						pa.getAntecedents().add(P);
						qa.getAntecedents().add(Q);

						for (int i = 0; i < 3; i++) {
							int j = (i + 1) % 3;
							int k = (i + 2) % 3;
							if (premisses.get(j).getAntecedentsObject().equals(pa) && premisses.get(k).getAntecedentsObject().equals(qa)) {
								sequentP = premisses.get(j);
								sequentQ = premisses.get(k);
							} else if (premisses.get(k).getAntecedentsObject().equals(pa) && premisses.get(j).getAntecedentsObject().equals(qa)) {
								sequentP = premisses.get(k);
								sequentQ = premisses.get(j);
							}
						}
						if (sequentP == null || sequentQ == null) {
							throw new AntecedentsMismatchException();
						}

						if (!(sequentP.getConclusion().equals(R) && sequentQ.getConclusion().equals(R))) {
							throw new RuleException(OR_ELIM, "conclusion does not match conclusions of premises");
						}

					}

					break;

				}
		}

	}

}

