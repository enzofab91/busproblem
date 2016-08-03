package org.uma.jmetal.operator.impl.crossover;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.util.JMetalException;

@SuppressWarnings("serial")
public class BusProblemCrossover implements CrossoverOperator<BusSolution> {
	
	private double probability;
	
	public BusProblemCrossover(double probability) {
		if (probability < 0) {
		      throw new JMetalException("Crossover probability is negative: " + probability) ;
		}
		this.probability = probability;
	}

	@Override
	public List<BusSolution> execute(List<BusSolution> parents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfParents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
