import java.io.IOException;

import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.problems.ReleasePlanningProblemRaphael;
import jmetal.util.JMException;
import tests.referenceFront;

public class MainReference {

	public static void main(String[] args) throws IOException, ClassNotFoundException, JMException {
		
		Problem problem = new ReleasePlanningProblemRaphael("data-set-1");
		referenceFront front = new referenceFront();
		
		SolutionSet population = front.main(problem);
		population.printObjectivesToFile("ReferenceFront.txt");

	}

}
