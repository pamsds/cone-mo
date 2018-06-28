import java.io.FileInputStream;
import java.io.IOException;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.problems.ReleasePlanningProblemRaphael;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;
import tests.config;
import tests.referenceFront;

public class MainReference {

	public static void main(String[] args) throws IOException, ClassNotFoundException, JMException {

		 Problem problem = new ReleasePlanningProblemRaphael("data-set-1");
		 referenceFront front = new referenceFront();
		 SolutionSet population = front.main(problem);
		 population.printObjectivesToFile("ReferenceFront.txt");
		 
		 //frontBreak();

		
		
	}

	
	public static void frontBreak() {
		
		MetricsUtil utils_ = new jmetal.qualityIndicator.util.MetricsUtil() ;
		SolutionSet Truefront = utils_.readNonDominatedSolutionSet("ReferenceFront.txt");
		Truefront.setCapacity(8400);

		Solution solution = config.referencePoint(Truefront, config.PreferencesP1);
		Solution solution2 = config.referencePoint(Truefront, config.PreferencesP2);

		SolutionSet seletedPoints = new SolutionSet(2);
		seletedPoints.add(solution);
		seletedPoints.add(solution2);

		config.min[0] = solution.getObjective(0);
		config.min[1] = solution2.getObjective(1);
		config.max[0] = solution2.getObjective(0);
		config.max[1] = solution.getObjective(1);

		SolutionSet populationSelect = nsgaSelectFront(Truefront);
		populationSelect.printObjectivesToFile("ReferenceFrontSAT.txt");
	
	}
	
	public static SolutionSet nsgaSelectFront(SolutionSet population) {

		SolutionSet SelPopulation = new SolutionSet(8400);

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
