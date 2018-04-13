package jmetal.problems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

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

	protected double[] cost;

	protected int[] satisfaction;

	protected int[][] customerSatisfaction;

	protected int[] customerImportance;

	protected int n_releases;

	protected int n_requirements;

	protected int n_customers;

	protected double[] releaseCost;
	
	protected int[][] precedence;

	//public ReleasePlanningProblem(String filename) throws IOException {
	public ReleasePlanningProblem(String filename) throws IOException {

		//readInstance("src" + File.separator + "instances" + File.separator + filename + ".txt");
		readInstance(filename);
		problemName_ = "ReleasePlanningProblem";
		numberOfVariables_ = n_requirements;
		numberOfObjectives_ = 2; // in fact the problem has a mono-objective
									// formulation, however, extra objectives
									// are used to store value of other metrics
		numberOfConstraints_ = 1;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < numberOfVariables_; i++) {
			upperLimit_[i] = n_releases;
			lowerLimit_[i] = 0;
		}

		try {
			solutionType_ = new IntSolutionType(this);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void readInstance(String FileName) {
		try {
			Scanner scn = new Scanner(new File(FileName));
			StringTokenizer tokens;
			String caracter = "#";
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			tokens = new StringTokenizer(caracter);
			n_requirements = Integer.parseInt(tokens.nextToken().trim());
			n_customers = Integer.parseInt(tokens.nextToken().trim());
			n_releases = Integer.parseInt(tokens.nextToken().trim());
			caracter = scn.nextLine();
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			cost = new double[n_requirements];
			tokens = new StringTokenizer(caracter);
			for(int i = 0; i < n_requirements; i++) {
				cost[i] = Double.parseDouble(tokens.nextToken().trim());
			}
			caracter = scn.nextLine();
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			customerImportance = new int[n_customers];
			tokens = new StringTokenizer(caracter);
			for(int i = 0; i < n_customers; i++) {
				customerImportance[i] = Integer.parseInt(tokens.nextToken().trim());
			}
			caracter = scn.nextLine();
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			
			satisfaction = new int[n_requirements];
			customerSatisfaction = new int[n_requirements][n_customers];
			for(int i = 0; i < n_requirements; i++) {
				tokens = new StringTokenizer(caracter);
				for(int j = 0; j < n_customers; j++) {
					customerSatisfaction[i][j] = Integer.parseInt(tokens.nextToken().trim());
					satisfaction[i] += customerSatisfaction[i][j]*customerImportance[j];
				}
				caracter = scn.nextLine();
			}
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			risk = new int[n_requirements];
			tokens = new StringTokenizer(caracter);
			for(int i = 0; i < n_requirements; i++) {
				risk[i] = Integer.parseInt(tokens.nextToken().trim());
			}
			caracter = scn.nextLine();
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}	
			
			precedence = new int[n_requirements][n_requirements];
			for(int i = 0; i < n_requirements; i++) {
				tokens = new StringTokenizer(caracter);
				for(int j = 0; j < n_requirements; j++) {
					precedence[i][j] = Integer.parseInt(tokens.nextToken().trim());
				}
				caracter = scn.nextLine();
			}
			
			while(caracter.contains("#")) {
				caracter = scn.nextLine();
			}
			
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
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

		for (int i = 0; i < n_requirements; i++) {
			int gene = (int) individual[i].getValue();
			if (gene == 0)
				continue;

			solutionScore += (double) satisfaction[i] * (n_releases - gene + 1);

		}
		return solutionScore;
	}

	public double calculateRisk(Solution solution) throws JMException {
		double solutionScore = 0;
		Variable[] individual = solution.getDecisionVariables();

		for (int i = 0; i < n_releases; i++) {
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