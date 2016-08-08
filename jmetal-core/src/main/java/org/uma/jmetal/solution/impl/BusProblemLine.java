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
			
//			//busco el indice
//			if(bs.getParada() != parada && !encontre){
//				indice++;
//				encontre = true;
//			}
		}
		
		BusProblemStop bps_new = funcionMagica(paradas_nuevas, parada, offset);
		
		paradas_nuevas.add(parada,bps_new);
		this.paradas = paradas_nuevas;
		
	}
	
	public void quitarParada(int parada){
		paradas.get(parada).setOffset(-1);
	}
	
	public String print(){
		String str = "";
		
		str += "Linea=" + Integer.toString(linea) + "\n";
////		str += "Paradas=<";
//		
////		System.out.println("Linea=" + Integer.toString(linea));
//		System.out.println("Libres=" + Integer.toString(k));
//		System.out.print("Paradas=<");
		
		Iterator<BusProblemStop> it = paradas.listIterator();
		
		while(it.hasNext()){
			BusProblemStop elem = it.next();
			
			str += "\t(" + elem.getParada() + "," + Integer.toString(elem.getOffset()) + "," + Integer.toString(elem.getSuben()) + "," + Integer.toString(elem.getBajan()) + ")\n";
//			System.out.print("(" + elem.getParada() + "," +  Integer.toString(elem.getSuben()) + "," + Integer.toString(elem.getBajan()) + ") ");
		}
		
//		System.out.println(">");
//		str += ">\n";
		
		return str;
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
		
		//Aca se calculan las coordenadas de la nueva parada a partir de la parada anterior y el offset
		/* http://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters */
		
		//Earth’s radius, sphere
		int R = 6378137;
	
		//Coordinate offsets in radians
		double dLat = (offset*100)/R;
		double dLon = (offset*100)/(R*Math.cos(Math.PI*parada_anterior.getLatitud()/180));
	
		//OffsetPosition, decimal degrees
		double nueva_longitud = parada_anterior.getLatitud() + dLat * 180/Math.PI;
		double nueva_latitud = parada_anterior.getLongitud() + dLon * 180/Math.PI; 
		 
		 BusProblemStop bps = new BusProblemStop(quito_anterior_suben + quito_siguiente_suben, 
				 			quito_anterior_bajan + quito_siguiente_bajan, BusProblemStop.getMAX_K(),
				 			nueva_longitud, nueva_latitud, offset);
		 
		 return bps;
	}
	
	public int getPseudoRandomStop(){
		return paradas.get(randomGenerator.nextInt(0, paradas.size()-1)).getParada();
	}
	
	public BusProblemStop checkBusStopInLine(int nro_parada){
		Iterator<BusProblemStop> it = paradas.listIterator();
		
		BusProblemStop bps = null;
		BusProblemStop bps_aux = null;
		
		while(it.hasNext()){
			bps_aux = it.next();
			
			if(bps_aux.getParada() == nro_parada){
				bps = bps_aux;
				break;
			}
			
		}
		
		return bps;
	}

	public BusProblemLine copy() {
		return new BusProblemLine(this);
	}

	private BusProblemLine(BusProblemLine busProblemLine) {
		this.k = busProblemLine.getK();
		this.linea = busProblemLine.getLine();
		this.randomGenerator = JMetalRandom.getInstance();
		
		//Copio las paradas en limpio
		for(int i = 0; i < busProblemLine.getParadas().size(); i++){
			BusProblemStop bps = new BusProblemStop(busProblemLine.getParadas().get(i).getSuben(), 
													busProblemLine.getParadas().get(i).getBajan(), 
													busProblemLine.getParadas().get(i).getParada(), 
													busProblemLine.getParadas().get(i).getLatitud(), 
													busProblemLine.getParadas().get(i).getLongitud(), 
													busProblemLine.getParadas().get(i).getOffset()
													);
			
			this.agregarParada(bps);			
		}
	}

}
