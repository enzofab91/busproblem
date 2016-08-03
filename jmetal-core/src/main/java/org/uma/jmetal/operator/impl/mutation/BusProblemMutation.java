package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.util.JMetalException;

@SuppressWarnings("serial")
public class BusProblemMutation implements MutationOperator<BusSolution> {
	
	private double probability;
	
	public BusProblemMutation(double probability) {
		if (probability < 0) {
		      throw new JMetalException("Mutation probability is negative: " + probability) ;
		}
		this.probability = probability;
	}

	@Override
	public BusSolution execute(BusSolution source) {
		// TODO Auto-generated method stub
		return null;
	}

}
