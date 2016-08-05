package org.uma.jmetal.operator.impl.crossover;

import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class BusProblemCrossover implements CrossoverOperator<BusSolution> {
	
	private double probability;
	private JMetalRandom randomGenerator = JMetalRandom.getInstance();
	
	public BusProblemCrossover(double probability) {
		if (probability < 0) {
		      throw new JMetalException("Crossover probability is negative: " + probability) ;
		}
		this.setProbability(probability);
	}

	@Override
	public List<BusSolution> execute(List<BusSolution> parents) {
		if (parents == null) {
	      throw new JMetalException("Null parameter") ;
	    } else if (parents.size() != 2) {
	      throw new JMetalException("There must be two parents instead of " + parents.size()) ;
	    }
		
		List<BusSolution> offspring = new LinkedList<BusSolution>();
		offspring.add((BusSolution) parents.get(0).copy());
		offspring.add((BusSolution) parents.get(1).copy());
		
		
		
		//Verifico si tengo o no que cruzar
		if(randomGenerator.nextDouble() <= probability){
			return null;
		}
		else{
			return offspring;
		}
	
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
