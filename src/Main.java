import java.io.FileInputStream;
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
import jmetal.problems.ReleasePlanningProblem;
import jmetal.problems.ReleasePlanningProblemRaphael;
import jmetal.problems.gustavoProblem;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import tests.config;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException, JMException {

		config();
		Problem problem = new ReleasePlanningProblemRaphael("data-set-1");
		// Problem problem = new gustavoProblem(1000);

		SolutionSet population = nsgaStandard(problem);
		population.printObjectivesToFile("NSGA.txt");
		// population.printVariablesToFile("NSGA.txt");

		Solution solution = config.referencePoint(population, config.PreferencesP1);
		Solution solution2 = config.referencePoint(population, config.PreferencesP2);

		SolutionSet seletedPoints = new SolutionSet(2);
		seletedPoints.add(solution);
		seletedPoints.add(solution2);
		seletedPoints.printObjectivesToFile("points.txt");

		config.min[0] = solution.getObjective(0);
		config.min[1] = solution2.getObjective(1);
		config.max[0] = solution2.getObjective(0);
		config.max[1] = solution.getObjective(1);

		SolutionSet populationSelect = nsgaSelectFront(population);
		populationSelect.printObjectivesToFile("NSGA_SEL.txt");
		// populationSelect.printVariablesToFile("VAR_NSGA_SEL.txt");

		population = nsgaCone(problem);
		population.printObjectivesToFile("NSGAPC.txt");
		// population.printVariablesToFile("VAR_NSGAPC.txt");

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
		algorithm.setInputParameter("maxEvaluations", 3000);

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
		algorithm.setInputParameter("maxEvaluations", 3000);

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
				// System.out.println(montarString(solution));
				// System.out.println(info.toString().equals("yes."));

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
