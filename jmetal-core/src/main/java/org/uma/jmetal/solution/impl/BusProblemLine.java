package org.uma.jmetal.solution.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.solution.impl.BusProblemStop;

public class BusProblemLine {
	private int linea;
	private int k;
	private List<BusProblemStop> paradas = new LinkedList<BusProblemStop>();
	
	public int getLie(){
		return linea;
	}
	
	public int getK(){
		return k;
	}
	
	public List<BusProblemStop> getParadas(){
		return paradas;
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
