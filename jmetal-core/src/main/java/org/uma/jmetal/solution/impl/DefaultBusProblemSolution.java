package org.uma.jmetal.solution.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.uma.jmetal.problem.BusProblem;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.Solution;


@SuppressWarnings("serial")
public class DefaultBusProblemSolution 
	extends AbstractGenericSolution<BusProblemLine, BusProblem>
	implements BusSolution {

	public DefaultBusProblemSolution(BusProblem problem) {
		super(problem);
		crearSolucion(problem);
		initializeObjectiveValues();
	}
	
	
	/* Copy constructor */
	public DefaultBusProblemSolution(DefaultBusProblemSolution solution){
		super(solution.problem);
		
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
	      setVariableValue(i, solution.getVariableValue(i));
	    }

	    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
	      setObjective(i, solution.getObjective(i)) ;
	    }
	}
	
	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}

	@Override
	public DefaultBusProblemSolution copy() {
		return new DefaultBusProblemSolution(this);
	}
	
	private void crearSolucion(BusProblem problem){
		float[][] matrizDistancias = problem.getMatrizDistancia();
		SDTSubenBajan[][] matrizPasajeros = problem.getMatrizPasajeros();
		Map<Integer, Integer> lineas = problem.getCorrelacion();
		
		for (Entry<Integer, Integer> entry : lineas.entrySet()) {
		    int linea = entry.getKey();
		    int posicion = entry.getValue();
		    
		    BusProblemLine paradas = new BusProblemLine(linea,50);
		    
		    for (int i=0; i<problem.getCantidadDeParadas(); i++){
		    	if(matrizPasajeros[posicion][i] != null){
		    		BusProblemStop bps;
		
		    			
	    			if(paradas.getK() >= (matrizPasajeros[posicion][i].getSuben() - matrizPasajeros[posicion][i].getBajan())){
	    				
	    				bps = new BusProblemStop(matrizPasajeros[posicion][i].getSuben(),
	    														matrizPasajeros[posicion][i].getBajan(),
	    														i,
	    														1); 
	    			}
	    			else{
	    				
	    				bps = new BusProblemStop(paradas.getK() + matrizPasajeros[posicion][i].getBajan(),
																matrizPasajeros[posicion][i].getBajan(),
																i,
																1); 
	    			}
	    			
	    			paradas.setK(paradas.getK() - (bps.getSuben() - bps.getBajan()));
		    		paradas.agregarParada(bps);
		    	}
		    }	
		    
			setVariableValue(posicion, paradas);
			
		}		
	}

}
