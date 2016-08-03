package org.uma.jmetal.solution.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.uma.jmetal.problem.BusProblem;
import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.BusSolution;


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
		SDTSubenBajan[][] matrizPasajeros = problem.getMatrizPasajeros();
		Map<Integer, Integer> lineas = problem.getCorrelacion();
		
		for (Entry<Integer, Integer> entry : lineas.entrySet()) {
		    int linea = entry.getKey();
		    int posicion = entry.getValue();
		    
		    BusProblemLine paradas = new BusProblemLine(linea,problem.getCantidadMaximaPasajeros());
		    
		    Iterator<Integer> ordenParadas = problem.getOrdenParadas().get(linea).iterator();
		    
		    while(ordenParadas.hasNext()){
		    	int aux = ordenParadas.next();
		    	
		    	if(matrizPasajeros[posicion][aux] != null){
		    		BusProblemStop bps;
		
		    			
	    			if(paradas.getK() >= (matrizPasajeros[posicion][aux].getSuben() - matrizPasajeros[posicion][aux].getBajan())){
	    				
	    				bps = new BusProblemStop(matrizPasajeros[posicion][aux].getSuben(),
	    														matrizPasajeros[posicion][aux].getBajan(),
	    														aux,
	    														1); 
	    			}
	    			else{
	    				
	    				bps = new BusProblemStop(paradas.getK() + matrizPasajeros[posicion][aux].getBajan(),
																matrizPasajeros[posicion][aux].getBajan(),
																aux,
																1); 
	    			}
	    			
	    			paradas.setK(paradas.getK()  + bps.getBajan());
	    			paradas.setK(paradas.getK()  - bps.getSuben());
	    			
		    		paradas.agregarParada(bps);
		    	}
		    }
		    
			setVariableValue(posicion, paradas);
			paradas.print();
		}		
	}

}
