package tests;

import java.io.IOException;
import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.ibea.IBEA;
import jmetal.metaheuristics.mocell.MOCell;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.Ranking;

public class referenceFront {

	public SolutionSet main(Problem problem) throws IOException, ClassNotFoundException, JMException {

		SolutionSet union = new SolutionSet(1000);

		for (int i = 0; i < 30; i++) {
			System.out.println("EXE::: "+i);

			System.out.println("NSGA");
			SolutionSet front1 = NSGA(problem);
			System.out.println("MOCELL");
			SolutionSet front2 = MOCELL(problem);
			System.out.println("IBEA");
			SolutionSet front3 = IBEA(problem);
			System.out.println("SPEA");
			SolutionSet front4 = SPEA(problem);


			union = union.union(front1);
			union = union.union(front2);
			union = union.union(front3);
			union = union.union(front4);
		}

		Ranking ranking = new Ranking(union);

		return ranking.getSubfront(0);

	}

	public static SolutionSet NSGA(Problem problem) throws ClassNotFoundException, JMException, IOException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters
		algorithm = new NSGAII(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 1000000);

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
		selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		return population;

	} // main

	public static SolutionSet MOCELL(Problem problem) throws IOException, ClassNotFoundException, JMException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters

		algorithm = new MOCell(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("archiveSize", 100);
		algorithm.setInputParameter("maxEvaluations", 1000000);
		algorithm.setInputParameter("feedBack", 20);

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
		selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		SolutionSet population = algorithm.execute();

		return population;

	}// main

	public static SolutionSet IBEA(Problem problem) throws JMException, ClassNotFoundException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters
		algorithm = new IBEA(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("archiveSize", 100);
		algorithm.setInputParameter("maxEvaluations", 1000000);

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
		selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		return population;

	}

	public static SolutionSet SPEA(Problem problem) throws JMException, ClassNotFoundException {

		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters
		algorithm = new SPEA2(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("archiveSize", 100);
		algorithm.setInputParameter("maxEvaluations", 1000000);

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
		selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the algorithm
		SolutionSet population = algorithm.execute();

		return population;

	}

}
