package org.uma.jmetal.solution.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.impl.BusProblemStop;

public class BusProblemLine {
	private int linea;
	private int k;
	private List<BusProblemStop> paradas = new LinkedList<BusProblemStop>();
	
	public BusProblemLine(int linea, int k){
		this.linea = linea;
		this.k = k;
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
		if(k > 50)
			this.k = 50;
		else if(k < 0)
			this.k = 0;
		else
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
	
	public void print(){
		System.out.println("Linea=" + Integer.toString(linea));
		System.out.println("Libres=" + Integer.toString(k));
		System.out.print("Paradas=<");
		
		Iterator<BusProblemStop> it = paradas.listIterator();
		
		while(it.hasNext()){
			BusProblemStop elem = it.next();
			System.out.print("(" + elem.getParada() + "," +  Integer.toString(elem.getSuben()) + "," + Integer.toString(elem.getBajan()) + ") ");
		}
		System.out.println(">");
	}

}
