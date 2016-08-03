package org.uma.jmetal.problem;

import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.BusSolution;

public interface BusProblem extends Problem<BusSolution> {
	
	public SDTSubenBajan[][] getMatrizPasajeros();
	public float[][] getMatrizDistancia();
	public Map<Integer, Integer> getCorrelacion();
	public int getCantidadDeParadas();
	public Map<Integer,List<Integer>> getOrdenParadas();
	public int getCantidadMaximaPasajeros();
	
}
