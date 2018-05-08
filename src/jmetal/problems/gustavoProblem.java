package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

public class gustavoProblem extends Problem{
	
	public gustavoProblem(int n_var) {

		problemName_ = "ReleasePlanningProblem";
		numberOfVariables_ = n_var;
		numberOfObjectives_ = 2; // in fact the problem has a mono-objective
									// formulation, however, extra objectives
									// are used to store value of other metrics
		numberOfConstraints_ = 1;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < numberOfVariables_; i++) {
			upperLimit_[i] = 1;
			lowerLimit_[i] = 0;
		}

		try {
			solutionType_ = new IntSolutionType(this);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
	    int    counterOnes = 0   ;
	    Variable[] individual = solution.getDecisionVariables();
	    
	    for (int i = 0; i < individual.length; i++) {
			if(individual[i].getValue()==1)
				counterOnes++;
		}
	    
	    solution.setObjective(0, func1(counterOnes));
	    solution.setObjective(1, -func2(counterOnes));
		
	}

	private double func1(int X) {
		return (X-1)*(X-1) + 1;
	}
	
	private double func2(int X) {
		return X*X + 1;
	}

}
