package tests;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;

public class config {

	public static int point1[] = { 2, 2 };
	public static int point2[] = { 5, 5 };
	public static int festrela[] = { -Integer.MAX_VALUE, -Integer.MAX_VALUE };
	public static double[] PreferencesP1 = {0.1,0.9};
	public static double[] PreferencesP2 = {0.9,0.1};
	
	
	private Solution referencePoint(SolutionSet population, double[] solutionPreferences) {

		MetricsUtil utils_ = new jmetal.qualityIndicator.util.MetricsUtil();
		double[] maximumValues = utils_.getMaximumValues(population.writeObjectivesToMatrix(), 2);
		double[] minimumValues = utils_.getMinimumValues(population.writeObjectivesToMatrix(), 2);

		int pos = 0;

		double mi[] = calcMi(solutionPreferences, maximumValues, minimumValues, 2);
		double[] valueFuntion = calcProblem(mi, maximumValues, minimumValues, population);

		double min = Double.MAX_VALUE;
		for (int j = 0; j < valueFuntion.length; j++) {
			if (valueFuntion[j] < min) {
				min = valueFuntion[j];
				pos = j;
			}
		}

		return population.get(pos);

	}

	private double[] calcMi(double[] solutionPreferences, double maximumValues[], double minimumValues[], int tam) {

		double mi[] = new double[tam];

		for (int i = 0; i < mi.length; i++) {
			mi[i] = 1 / ((solutionPreferences[i]) * (maximumValues[i] - minimumValues[i]));
		}

		return mi;

	}

	private double[] calcProblem(double mi[], double maximumValues[], double minimumValues[], SolutionSet population) {

		double valueFuntion[] = new double[population.size()];

		for (int i = 0; i < population.size(); i++) {
			double max = -Double.MAX_VALUE;
			for (int j = 0; j < mi.length; j++) {
				double var = mi[j] * (population.get(i).getObjective(j) - maximumValues[j]);
				if (var > max) {
					max = var;
				}
			}
			valueFuntion[i] = max;
		}

		return valueFuntion;

	}

}