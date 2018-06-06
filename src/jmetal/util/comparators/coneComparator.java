package jmetal.util.comparators;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;

import org.fife.ui.rtextarea.ConfigurableCaret;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import jmetal.core.Solution;
import tests.config;

public class coneComparator implements Comparator {
	IConstraintViolationComparator violationConstraintComparator_;
	Prolog engine;
	SolveInfo info1 = null;
	SolveInfo info2 = null;

	/**
	 * Constructor
	 */
	public coneComparator() {
		violationConstraintComparator_ = new OverallConstraintViolationComparator();
		engine = new Prolog();
		try {
			engine.setTheory(new Theory(new FileInputStream("prolog/programa.pl")));
		} catch (InvalidTheoryException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Constructor
	 * 
	 * @param comparator
	 */
	public coneComparator(IConstraintViolationComparator comparator) {
		violationConstraintComparator_ = comparator;
	}

	/**
	 * Compares two solutions.
	 * 
	 * @param object1
	 *            Object representing the first <code>Solution</code>.
	 * @param object2
	 *            Object representing the second <code>Solution</code>.
	 * @return -1, or 0, or 1 if solution1 dominates solution2, both are
	 *         non-dominated, or solution1 is dominated by solution22, respectively.
	 */
	public int compare(Object object1, Object object2) {
		if (object1 == null)
			return 1;
		else if (object2 == null)
			return -1;

		Solution solution1 = (Solution) object1;
		Solution solution2 = (Solution) object2;

		try {
			info1 = engine.solve(montarString(solution1));
			info2 = engine.solve(montarString(solution2));
			
			System.out.println(montarString(solution2));	
			if (info1.toString().equals("yes.")) {
				System.out.println(info1.toString());	
			}

			
		} catch (MalformedGoalException e) {
			e.printStackTrace();
		}
		

		if (info1.toString().equals(info2.toString())) {

			int dominate1; // dominate1 indicates if some objective of solution1
			// dominates the same objective in solution2. dominate2
			int dominate2; // is the complementary of dominate1.

			dominate1 = 0;
			dominate2 = 0;

			int flag; // stores the result of the comparison

			// Applying a dominance Test then
			double value1, value2;
			for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
				value1 = solution1.getObjective(i);
				value2 = solution2.getObjective(i);
				if (value1 < value2) {
					flag = -1;
				} else if (value1 > value2) {
					flag = 1;
				} else {
					flag = 0;
				}

				if (flag == -1) {
					dominate1 = 1;
				}

				if (flag == 1) {
					dominate2 = 1;
				}
			}

			if (dominate1 == dominate2) {
				return 0; // No one dominate the other
			}
			if (dominate1 == 1) {
				return -1; // solution1 dominate
			}
			return 1; // solution2 dominate

		} else {

			if (info1.toString().equals("yes.")) {
				return -1; // solution1 dominate
			} else {
				return 1; // solution2 dominate
			}

		}

	} // compare

	String montarString(Solution solution) {
		String s;
		return s = "validar_regiao(" + (int) solution.getObjective(0) + "," + (int) -solution.getObjective(1) + ","
				+ config.festrela[0] + "," + config.festrela[1] + "," + config.point1[0] + "," + config.point2[0] + ","
				+ config.point1[1] + "," + config.point2[1] + ").";

	}

}
