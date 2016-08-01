package org.uma.jmetal.solution.impl;

import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.problem.IntegerProblem;

@SuppressWarnings("serial")
public class BusProblemSolution 
	extends AbstractGenericSolution<BusProblemLine, IntegerProblem>
	implements IntegerSolution{

	protected BusProblemSolution(IntegerProblem problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setVariableValue(int index, BusProblemLine value) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	//public String getVariableValueString(int index) {
		// TODO Auto-generated method stub
	//	return null;
	//}

	//@Override
	//public Solution<Integer> copy() {
		// TODO Auto-generated method stub
	//	return null;
	//}

	@Override
	public BusProblemLine getVariableValue(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getLowerBound(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getUpperBound(int index) {
		// TODO Auto-generated method stub
		return null;
	}
}
