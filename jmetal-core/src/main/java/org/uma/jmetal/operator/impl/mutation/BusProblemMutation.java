package org.uma.jmetal.operator.impl.mutation;

import java.util.List;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.impl.BusProblemLine;
import org.uma.jmetal.solution.impl.BusProblemStop;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.obtenerParametros;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class BusProblemMutation implements MutationOperator<BusSolution> {
	private double probability;
	private JMetalRandom randomGenerator = JMetalRandom.getInstance();
	
	public BusProblemMutation(double probability) {
		if (probability < 0) {
		      throw new JMetalException("Mutation probability is negative: " + probability) ;
		}
		this.setProbability(probability);
	}

	@Override
	public BusSolution execute(BusSolution solution) {
		if (null == solution) {
	      throw new JMetalException("Null parameter");
	    }
		
		//Verifico si ejecuto o no la mutacion
		if(randomGenerator.nextDouble() <= probability){
		
			//obtenemos que linea al azar modificar
			int linea_mutar = randomGenerator.nextInt(0, solution.getNumberOfVariables()-1);
			BusProblemLine line = solution.getVariableValue(linea_mutar);
			
			//obtenemos las paradas
			List<BusProblemStop> paradas = line.getParadas();
			
			//obtenemos que parada modificar y si se queda donde esta, se saca o se modifica (sin ser la primera y ultima)
			int parada_mutar = randomGenerator.nextInt(1, paradas.size()-1);
			int cantCuadras = Integer.parseInt(obtenerParametros.getParameter("CantidadMaximaCuadras"));
			
			
			//La accion determina que se va a hacer
			//- 0: la parada a mutar se deja como esta
			//- -1: la parada a mutar se quita
			// num: se crea una nueva parada a partir de la parada a mutar y se mueve cantCuadras
			int accion = randomGenerator.nextInt(-1, cantCuadras);
		
			//mutamos la parada si corresponde
			if (accion != 0){
				if(accion == -1){
					line.quitarParada(parada_mutar);
				}
				else{
					line.nuevaParada(parada_mutar,cantCuadras);
				}
			}
			
			solution.setVariableValue(linea_mutar,line);
		
		}
		
		return solution;
		
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
