package org.uma.jmetal.solution.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.solution.impl.BusProblemStop;

public class BusProblemLine {
	private int linea;
	private int k;
	private List<BusProblemStop> paradas = new LinkedList<BusProblemStop>();
	
	public BusProblemLine(int linea, int k){
		
	}
	
	public int getLine(){
		return linea;
	}
	
	public int getK(){
		return k;
	}
	
	public List<BusProblemStop> getParadas(){
		return paradas;
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public void agregarParada(BusProblemStop stop){
		paradas.add(stop);
	}
	
	public void moverParada(int parada, float offset){
		Iterator<BusProblemStop> it = paradas.iterator();
		
		boolean encontre = false;
		
		while(it.hasNext() && !encontre){
			BusProblemStop bs = it.next();
			if(bs.getParada() == parada){
				bs.setOffset(offset);
				encontre = true;
			}
		}
	}

}
