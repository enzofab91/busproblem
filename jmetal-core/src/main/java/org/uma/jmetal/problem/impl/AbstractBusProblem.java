package org.uma.jmetal.problem.impl;

import org.uma.jmetal.problem.BusProblem;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.impl.DefaultBusProblemSolution;

@SuppressWarnings("serial")
public abstract class AbstractBusProblem 
	extends AbstractGenericProblem<BusSolution>
	implements BusProblem {

	@Override
	public BusSolution createSolution() {
		 return new DefaultBusProblemSolution(this) ;
	}

}
