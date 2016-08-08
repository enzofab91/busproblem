package org.uma.jmetal.operator.impl.crossover;

import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.impl.BusProblemLine;
import org.uma.jmetal.solution.impl.BusProblemStop;
import org.uma.jmetal.solution.impl.DefaultBusProblemSolution;
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
		
		BusSolution parent1 = parents.get(0);
		BusSolution parent2 = parents.get(1);
		BusSolution botija = (DefaultBusProblemSolution)parents.get(0).copy();
		
		
		
		//Verifico si tengo o no que cruzar
		if(randomGenerator.nextDouble() <= probability){
			int cantidadLineas = parents.get(0).getNumberOfVariables();
			
			for(int i = 0; i < cantidadLineas; i++){
				
				int parada = 0;
				BusProblemStop bps1 = null;
				BusProblemStop bps2 = null;
				
				boolean encontre = false;
				int cantIntentosMax = 100;
				int iter = 0;
				
				while(!encontre && iter != cantIntentosMax){
					
					iter++;
					
					parada = parent1.getVariableValue(i).getPseudoRandomStop();
					bps2 = parent2.getVariableValue(i).checkBusStopInLine(parada);
					
					if (bps2 != null)
						encontre = true;
				}
				
				if(encontre){
				
					bps1 = parent1.getVariableValue(i).checkBusStopInLine(parada);
					
					BusProblemLine bpl = botija.getVariableValue(i);
					
					if((bpl.getK() + bpl.checkBusStopInLine(parada).getSuben() - bpl.checkBusStopInLine(parada).getBajan()) >= 
						bps1.getSuben() + bps2.getSuben() - (bps1.getBajan() + bps2.getBajan())){
						
						bpl.checkBusStopInLine(parada).setSuben(bps1.getSuben() + bps2.getSuben());
						bpl.checkBusStopInLine(parada).setBajan(bps1.getBajan() + bps2.getBajan());
						bpl.setK(bpl.getK() - (bps1.getSuben() + bps2.getSuben()) + bps1.getBajan() + bps2.getBajan());
					}
				}
				
			}
			
			offspring.add(botija);
		}
		else{
			if(randomGenerator.nextDouble(0, 1) >= 0.5){
				offspring.add(parents.get(0));
			}
			else{
				offspring.add(parents.get(1));
			}
		}
		
		return offspring;
	
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
