import java.io.IOException;
import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.IPNSGAII;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAII_main;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ReleasePlanningProblem;
import jmetal.problems.gustavoProblem;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import tests.config;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException, JMException {
		
		// String file = "/home/pamella/eclipse-workspace/cone-mo/src/instances/instance_bagnall2001_example.txt";
		String file = "/home/raphael/eclipse-workspace/cone-mo/src/instances/instance_bagnall2001_example.txt";
		// String file = "C:\\Users\\Raphael\\eclipse-workspace\\cone-mo\\src\\instances\\instance_bagnall2001_example.txt";

		//Problem problem = new ReleasePlanningProblem(file);
		Problem problem = new gustavoProblem(10);
		
		SolutionSet population = nsgaStandard(problem);
		
	    population.printVariablesToFile("VAR_NSGA.txt");    
	    population.printObjectivesToFile("FUN_NSGA.txt");
		
		Solution solution = config.referencePoint(population, config.PreferencesP1);
		config.point1[0] = solution.getObjective(0);
		config.point1[1] = solution.getObjective(1);

		solution = config.referencePoint(population, config.PreferencesP2);
		config.point2[0] = solution.getObjective(0);
		config.point2[1] = solution.getObjective(1);
		
		population = nsgaCone(problem);
		
	    population.printVariablesToFile("VAR_IPNSGA.txt");    
	    population.printObjectivesToFile("FUN_IPNSGA.txt");
	}

	public static SolutionSet nsgaStandard(Problem problem) throws IOException, JMException, ClassNotFoundException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap parameters; // Operator parameters

		QualityIndicator indicators; // Object to get quality indicators
		algorithm = new NSGAII(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 50000);

		// Mutation and Crossover for Integer codification
		parameters = new HashMap();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

		parameters = new HashMap();
		parameters.put("probability", 0.05);
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

		// Selection Operator
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		return population;

	}

	public static SolutionSet nsgaCone(Problem problem) throws IOException, JMException, ClassNotFoundException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap parameters; // Operator parameters
		QualityIndicator indicators; // Object to get quality indicators


		algorithm = new IPNSGAII(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 2500);

		// Mutation and Crossover for Integer codification
		parameters = new HashMap();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

		parameters = new HashMap();
		parameters.put("probability", 0.05);
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

		// Selection Operator
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		return population;

	}

}
