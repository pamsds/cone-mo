package jmetal.problems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.InstanceReader;
import jmetal.util.JMException;

/**
 * The Release Planning Problem Class
 * 
 * @author Thiago Nascimento
 * @since 2014-07-30
 * @version 1.0
 *
 */
public class ReleasePlanningProblemRaphael extends Problem {

	protected int[] risk;

	protected int[] cost;

	protected int[] satisfaction;

	protected int[][] customerSatisfaction;

	protected int[] customerImportance;

	protected int releases;

	protected int requirements;

	protected int customers;

	protected int[] releaseCost;

	protected InstanceReader reader;

	private String filename;


	private String scenario;

	private int[][] precedence;

	
	private String[] reqDescriptions;

	public String getFilename() {
		return filename;
	}

	public String getScenario() {
		return scenario;
	}


	public ReleasePlanningProblemRaphael(String filename) throws IOException {

		this.filename = filename;
		this.reader = new InstanceReader("src/instances/" + filename + ".rp");

		reader.open();

		readParameters();
		readCustomerImportance();
		readRiskAndCost();
		readCustomerSatisfaction();
		readReleaseCost();
		precedence = reader.readIntMatrix(requirements, requirements, " ");
		readDescriptions();
		reader.close();

		problemName_ = "ReleasePlanningProblem";
		numberOfVariables_ = getRequirements();
		numberOfObjectives_ = 2; // in fact the problem has a mono-objective
									// formulation, however, extra objectives
									// are used to store value of other metrics
		numberOfConstraints_ = 1;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < numberOfVariables_; i++) {
			upperLimit_[i] = getReleases();
			lowerLimit_[i] = 0;
		}

		try {
			solutionType_ = new IntSolutionType(this);
		} catch (Exception e) {
			System.out.println(e);
		}	
		
	}

	private void readDescriptions() {
		reqDescriptions = new String[requirements];

		for (int i = 0; i < reqDescriptions.length; i++) {
			reqDescriptions[i] = i + ". " + reader.readLine();
		}

	}

	private void readRiskAndCost() {
		this.risk = new int[requirements];
		this.cost = new int[requirements];

		int[][] info = reader.readIntMatrix(requirements, 2, " ");

		for (int i = 0; i < requirements; i++) {
			this.cost[i] = info[i][0];
			this.risk[i] = info[i][1];
		}
	}

	private void readReleaseCost() {
		this.releaseCost = reader.readIntVector(" ");
	}

	private void readCustomerSatisfaction() {
		this.satisfaction = new int[requirements];
		this.customerSatisfaction = reader.readIntMatrix(customers, requirements, " ");

		for (int i = 0; i < requirements; i++) {
			for (int j = 0; j < customers; j++) {
				satisfaction[i] += customerImportance[j] * customerSatisfaction[j][i];
			}
		}
	}

	private void readParameters() {
		int[] params = reader.readIntVector(" ");

		this.releases = params[0];
		this.requirements = params[1];
		this.customers = params[2];
	}

	private void readCustomerImportance() {
		this.customerImportance = reader.readIntVector(" ");
	}

	/**
	 * Return number of Releases
	 * 
	 * @return number of Releases
	 */
	public int getReleases() {
		return releases;
	}

	/**
	 * Return number of Requirements
	 * 
	 * @return number of Requirements
	 */
	public int getRequirements() {
		return requirements;
	}

	/**
	 * Return number of customers
	 * 
	 * @return number of customers
	 */
	public int getCustomers() {
		return customers;
	}

	/**
	 * Return sum all requirement score
	 * 
	 * @return all release scores
	 */
	public int getSumRequirementScores() {
		int value = 0;

		for (int i = 0; i < requirements; i++) {
			value += satisfaction[i];
		}

		return value;
	}

	/**
	 * Get sum all requirement cost
	 * 
	 * @return all cost
	 */
	public int getCost() {
		int value = 0;

		for (int i = 0; i < requirements; i++) {
			value += cost[i];
		}

		return value;
	}

	/**
	 * Return the Requirement Score
	 * 
	 * @param i
	 *            Requirement ID
	 * @return The Requirement Score
	 */
	public int getRequirementScore(int i) {
		if (i < 0 || i + 1 > requirements) {
			throw new IllegalArgumentException("requirement id not found");
		}

		return satisfaction[i];
	}

	/**
	 * Return the Requirement Risk
	 * 
	 * @param i
	 *            Requirement ID
	 * @return The Requirement Risk
	 */
	public int getRisk(int i) {
		if (i < 0 || i + 1 > requirements) {
			throw new IllegalArgumentException("requirement id not found");
		}

		return risk[i];
	}

	/**
	 * 
	 * @param solution
	 * @return
	 * @throws JMException
	 */
	public double getRisk(Solution solution) throws JMException {
		double sumRisks = 0;
		Variable[] variables = solution.getDecisionVariables();
		for (int i = 0; i < variables.length; i++) {
			try {
				sumRisks += getRisk(i) * (Double) variables[i].getValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sumRisks;
	}

	public double getImportance(Solution solution) throws JMException {
		double sumImportances = 0;
		Variable[] variables = solution.getDecisionVariables();
		for (int i = 0; i < variables.length; i++) {
			sumImportances = (double) satisfaction[i] * (getReleases() - (Double) variables[i].getValue() + 1);
		}
		return sumImportances;
	}

	/**
	 * Return the Requirement Cost
	 * 
	 * @param i
	 *            Requirement ID
	 * @return The Requirement Cost
	 */
	public int getCost(int i) {
		if (i < 0 || i + 1 > requirements) {
			throw new IllegalArgumentException("requirement id not found" + i);
		}

		return cost[i];
	}

	/**
	 * Return the release cost
	 * 
	 * @param i
	 *            Release ID
	 * @return The Release Cost
	 */
	public int getBudget(int i) {
		if (i < 0 || i > releases) {
			throw new IllegalArgumentException("release id not found" + i);
		}

		return releaseCost[i - 1];
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

		for (int i = 0; i < getRequirements(); i++) {
			int gene = (int) individual[i].getValue();
			if (gene == 0)
				continue;

			solutionScore += (double) satisfaction[i] * (getReleases() - gene + 1);

		}
		return solutionScore;
	}

	public double calculateRisk(Solution solution) throws JMException {
		double solutionScore = 0;
		Variable[] individual = solution.getDecisionVariables();

		for (int i = 0; i < getRequirements(); i++) {
			int gene = (int) individual[i].getValue();
			if (gene == 0)
				continue;

			solutionScore += getRisk(i) * gene;

		}
		return solutionScore;
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		double solutionSatisfaction = 0;
		double solutionRisk = 0;
		solutionSatisfaction = calculateSatisfaction(solution);
		solutionRisk = calculateRisk(solution);

		solution.setObjective(0, solutionRisk); // score metric
		solution.setObjective(1, -solutionSatisfaction); // objective
															// of
															// the
															// formulation


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


	public String[] getReqDescriptions() {
		return reqDescriptions;
	}

	public void setReqDescriptions(String[] reqDescriptions) {
		this.reqDescriptions = reqDescriptions;
	}

} // ReleasePlanningProblem