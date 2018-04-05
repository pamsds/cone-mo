package jmetal.problems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;

/**
 * The Release Planning Problem Class
 * 
 * @author Thiago Nascimento
 * @since 2014-07-30
 * @version 1.0
 *
 */
public class ReleasePlanningProblem extends Problem {

	protected int[] risk;

	protected int[] cost;

	protected int[] satisfaction;

	protected int[][] customerSatisfaction;

	protected int[] customerImportance;

	protected int releases;

	protected int requirements;

	protected int customers;

	protected int[] releaseCost;
	
	protected int[][] precedence;

	public ReleasePlanningProblem(String filename) throws IOException {

		problemName_ = "ReleasePlanningProblem";
		numberOfVariables_ = requirements;
		numberOfObjectives_ = 2; // in fact the problem has a mono-objective
									// formulation, however, extra objectives
									// are used to store value of other metrics
		numberOfConstraints_ = 1;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < numberOfVariables_; i++) {
			upperLimit_[i] = releases;
			lowerLimit_[i] = 0;
		}

		try {
			solutionType_ = new IntSolutionType(this);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @param solution
	 * @return The fitness value of a given solution
	 * @throws JMException
	 */
	public double calculateSatisfaction(Solution solution) throws JMException {
		double solutionScore = 0;
		Variable[] individual = solution.getDecisionVariables();

		for (int i = 0; i < requirements; i++) {
			int gene = (int) individual[i].getValue();
			if (gene == 0)
				continue;

			solutionScore += (double) satisfaction[i] * (releases - gene + 1);

		}
		return solutionScore;
	}

	public double calculateRisk(Solution solution) throws JMException {
		double solutionScore = 0;
		Variable[] individual = solution.getDecisionVariables();

		for (int i = 0; i < releases; i++) {
			int gene = (int) individual[i].getValue();
			if (gene == 0)
				continue;

			solutionScore += risk[i] * gene;

		}
		return solutionScore;
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		double solutionSatisfaction = 0;
		double solutionRisk = 0;
		solutionSatisfaction = calculateSatisfaction(solution);
		solutionRisk = calculateRisk(solution);

		solution.setObjective(0, -solutionSatisfaction); 
		solution.setObjective(1, solutionRisk);

	}

	/**
	 * 
	 * @param solution
	 * @return the number of broken precedence constraints of a solution
	 * @throws JMException
	 */
	public int evaluatePrecedences(Solution solution) throws JMException {
		Variable[] variables = solution.getDecisionVariables();
		int counter = 0;
		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getValue() != 0) {
				for (int j = 0; j < variables.length; j++) {
					if (precedence[i][j] == 1 && variables[j].getValue() > variables[i].getValue())
						counter++;
				}
			}
		}
		return counter;
	}

} // ReleasePlanningProblem