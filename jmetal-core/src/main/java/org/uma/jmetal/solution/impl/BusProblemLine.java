package org.uma.jmetal.solution.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.solution.impl.BusProblemStop;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class BusProblemLine {
	private JMetalRandom randomGenerator = JMetalRandom.getInstance();
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
	
	public void setParadas(List<BusProblemStop> paradas){
		this.paradas = paradas;
	}
	
	public void agregarParada(BusProblemStop stop){
		paradas.add(stop);
	}
	
	public void nuevaParada(int parada, int offset){
		List<BusProblemStop> paradas_nuevas = new LinkedList<BusProblemStop>();
		
		Iterator<BusProblemStop> it = paradas.iterator();
		
		int indice = 0;
		boolean encontre = false;
		
		while(it.hasNext()){
			BusProblemStop bs = it.next();
			paradas_nuevas.add(bs);
			
			//busco el indice
			if(bs.getParada() != parada && !encontre){
				indice++;
				encontre = true;
			}
		}
		
		BusProblemStop bps_new = funcionMagica(paradas_nuevas, indice, offset);
		
		paradas_nuevas.add(indice,bps_new);
		this.paradas = paradas_nuevas;
		
	}
	
	public void quitarParada(int parada){
		Iterator<BusProblemStop> it = paradas.iterator();
				
		boolean encontre = false;
		
		while(it.hasNext() && !encontre){
			BusProblemStop bs = it.next();
			if(bs.getParada() == parada){
				bs.setOffset(-1);
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
	
	private BusProblemStop funcionMagica(List<BusProblemStop> paradas, int indice, int offset){
		//Esta funcion magica se encarga de tomar gente que sube/baja de las paradas siguiente y anterior de la nueva parada
		// que corresponden a la parada misma y la siguiente
		//Ademas se quitan de la parada siguiente/anterior los valores obtenidos de personas que suben y bajan
		//Falta definir la posibilidad de aceptar nueva gente que suba al bus
		
		BusProblemStop parada_siguiente = paradas.get(indice + 1); 
		BusProblemStop parada_anterior = paradas.get(indice);
		
		int quito_anterior_suben = randomGenerator.nextInt(0, parada_anterior.getSuben());
		int quito_anterior_bajan  = randomGenerator.nextInt(0, parada_anterior.getBajan());
		int quito_siguiente_suben = randomGenerator.nextInt(0, parada_siguiente.getSuben());
		int quito_siguiente_bajan = randomGenerator.nextInt(0, parada_siguiente.getBajan());
		//int nueva_gente_sube ????
		
		parada_siguiente.setSuben( parada_siguiente.getSuben() - quito_siguiente_suben);
		parada_siguiente.setBajan(parada_siguiente.getBajan() - quito_siguiente_bajan);
		parada_anterior.setSuben( parada_anterior.getSuben() - quito_anterior_suben);
		parada_anterior.setBajan(parada_anterior.getBajan() - quito_anterior_bajan);
		
		 BusProblemStop bps = new BusProblemStop(quito_anterior_suben + quito_siguiente_suben, 
				 			quito_anterior_bajan + quito_siguiente_bajan, BusProblemStop.getMAX_K(), offset);
		 
		 return bps;
	}

}
