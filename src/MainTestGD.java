import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAPC;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ReleasePlanningProblemRaphael;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import tests.config;
import tests.referenceFront;

public class MainTestGD {

	public static void main(String[] args) throws ClassNotFoundException, IOException, JMException {

		config();
		Problem problem = new ReleasePlanningProblemRaphael("data-set-1");
		QualityIndicator indicators = new QualityIndicator(problem, "ReferenceFront.txt");
		SolutionSet population = null;

		for (int i = 0; i < 1; i++) {
			
			System.out.println("EXEC::: "+i);
			population = nsgaStandard(problem);
			Solution solution = config.referencePoint(population, config.PreferencesP1);
			Solution solution2 = config.referencePoint(population, config.PreferencesP2);

			SolutionSet seletedPoints = new SolutionSet(2);
			seletedPoints.add(solution);
			seletedPoints.add(solution2);

			config.min[0] = solution.getObjective(0);
			config.min[1] = solution2.getObjective(1);
			config.max[0] = solution2.getObjective(0);
			config.max[1] = solution.getObjective(1);

//			SolutionSet populationSelect = nsgaSelectFront(population);
//			recordMetric("metrics/GD_nsga.txt", indicators.getGD(populationSelect));
			
			
			population = nsgaCone(problem);
			recordMetric("metrics/GD_cone.txt", indicators.getGD(population));
		
//			population = nsgaCone2(problem,populationSelect);
//			recordMetric("metrics/GD_cone2.txt", indicators.getGD(population));
			
			
		}
	}

	
	public static void recordMetric(String path, double value) {

		try {
			BufferedWriter escrever = new BufferedWriter(new FileWriter(path, true));
			escrever.append(value+"");
			escrever.newLine();
			escrever.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void config() {
		config.engine = new Prolog();
		try {
			config.engine.setTheory(new Theory(new FileInputStream("prolog/programa.pl")));
		} catch (InvalidTheoryException | IOException e) {
			e.printStackTrace();
		}

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
		algorithm.setInputParameter("maxEvaluations", 10000);

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

		algorithm = new NSGAPC(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 10000);

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

	public static SolutionSet nsgaCone2(Problem problem,SolutionSet initialPopulation) throws IOException, JMException, ClassNotFoundException {

		NSGAPC algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap parameters; // Operator parameters
		QualityIndicator indicators; // Object to get quality indicators

		algorithm = new NSGAPC(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 10000);

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
		SolutionSet population = algorithm.execute(initialPopulation);

		return population;

	}
	
	public static SolutionSet nsgaSelectFront(SolutionSet population) {

		SolutionSet SelPopulation = new SolutionSet(100);

		for (int i = 0; i < population.size(); i++) {

			Solution solution = population.get(i);

			Prolog engine = new Prolog();
			try {
				engine.setTheory(new Theory(new FileInputStream("prolog/programa.pl")));
			} catch (InvalidTheoryException | IOException e) {
				e.printStackTrace();
			}

			try {
				SolveInfo info = engine.solve(montarString(solution));
				if (info.toString().equals("yes.")) {
					SelPopulation.add(solution);
				}

			} catch (MalformedGoalException e) {
				e.printStackTrace();
			}

		}

		return SelPopulation;

	}

	static String montarString(Solution solution) {
		String s;
		return s = "validar_regiao(" + (int) solution.getObjective(0) + "," + (int) solution.getObjective(1) + ","
				+ config.festrela[0] + "," + config.festrela[1] + "," + config.min[0] + "," + config.min[1] + ","
				+ config.max[0] + "," + config.max[1] + ").";

	}

}
