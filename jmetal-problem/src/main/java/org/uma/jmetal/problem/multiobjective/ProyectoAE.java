//  BusProblem.java
package org.uma.jmetal.problem.multiobjective;

import org.apache.commons.math3.analysis.function.Sin;
import org.uma.jmetal.problem.impl.AbstractBusProblem;
import org.uma.jmetal.problem.impl.SDTCoordenadas;
import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.impl.BusProblemLine;
import org.uma.jmetal.solution.impl.BusProblemStop;
import org.uma.jmetal.util.obtenerParametros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.lang.Math;

@SuppressWarnings("serial")
public class ProyectoAE extends AbstractBusProblem {
	
  private int cantidadMaximaPasajeros = Integer.parseInt(obtenerParametros.getParameter("CantidadMaximaPasajeros"));
  private int demoraPromedioSubir = Integer.parseInt(obtenerParametros.getParameter("DemoraPromedioSubir"));
  private int demoraPromedioBajar = Integer.parseInt(obtenerParametros.getParameter("DemoraPromedioBajar"));
  
  private int cantLines ;
  private int cantParadas = Integer.parseInt(obtenerParametros.getParameter("CantidadParadas"));
  private SDTSubenBajan[][] Matrizpasajeros;
  private Map<Integer,Integer> correlacion = new HashMap<Integer,Integer>();
  private Map<Integer,List<Integer>> ordenParadas = new HashMap<Integer, List<Integer>>();
  //private float[][] matrizDistancias = new float[cantParadas][cantParadas];
  private Map<Integer, SDTCoordenadas> coordenadas = new HashMap<Integer, SDTCoordenadas>();


  /** Constructor */
  
  public ProyectoAE(){
	//TODO  
  }
  
  public ProyectoAE(String lines)  {
    readProblem(lines);
    showProblem();    
    setNumberOfVariables(this.cantLines);
    setNumberOfObjectives(2);
    setName("BusProblem");
  }

  /** Evaluate() method */
  @Override
  public void evaluate(BusSolution solution) {
	  System.out.println("ENTRO AL EVALUATE");
	  double fitness1;
	  double fitness2;

	  fitness1 = 0.0 ;
	  fitness2 = 0.0 ;
	  
	  BusProblemLine linea;
	  
	  for (int i = 0; i < this.cantLines; i++) {
		  //para cada linea recorro las paradas
		  linea = solution.getVariableValue(i);
		  List<BusProblemStop> paradas = linea.getParadas();
		  
		  for(int j=0; i < paradas.size()-1 ; i++){
			  
			//Me aseguro que la parada existe
			if (paradas.get(j).getOffset() != -1)
				fitness1 += paradas.get(j).getSuben();  
			
			//Busco la siguiente parada
			int t = j + 1;
			while(t < paradas.size() && paradas.get(t).getOffset() != -1){
				
				fitness2 -= calcularDistancia(paradas.get(j).getLatitud(),paradas.get(j).getLongitud(),
											paradas.get(t).getLatitud(),paradas.get(t).getLongitud());
				t++;
			}
		  } 
	  }
	  
	  solution.setObjective(0, fitness1);
	  solution.setObjective(1, fitness2);
	  
  }
  
  public void showProblem(){
	  try{
		  Iterator<Map.Entry<Integer, Integer>> it = this.correlacion.entrySet().iterator();
		  
		  PrintWriter writer = null;
		  while (it.hasNext()) {
		      Map.Entry<Integer, Integer> pair = it.next();
		      
		      //Imprime matriz de pasajeros
		      
		      //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_" +
		     // 	  Integer.toString(pair.getKey()) + "_pasajeros", "UTF-8");
		      writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_" +
		    		  Integer.toString(pair.getKey()) + "_pasajeros", "UTF-8");
			  
		      writer.println("LINEA: " + Integer.toString(pair.getKey()));
		      
		      for(int i = 0; i < this.cantParadas; i++){
		    	  if (this.Matrizpasajeros[pair.getValue()][i] != null)
		    		  writer.print("(" + Integer.toString(this.Matrizpasajeros[pair.getValue()][i].getSuben()) + "," + Integer.toString(this.Matrizpasajeros[pair.getValue()][i].getBajan())+ ") ");
		    	  else
		    		  writer.print("null ");
		      }
		      writer.println();
		      writer.close();
		      
		      //Imprime orden de paradas
		      //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_" +
			  //    	  Integer.toString(pair.getKey()) + "_orden", "UTF-8");
		      
		      writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_" +
		    		  Integer.toString(pair.getKey()) + "_orden", "UTF-8");
		      
		      Iterator<Integer> it2 = this.ordenParadas.get(pair.getKey()).iterator();
		      
		      while(it2.hasNext()){
		    	  writer.println(it2.next());
		      }
		      
		      writer.println();
		      writer.close();
		      
		  }
		  
		  //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_coorndenadas", "UTF-8");
		  writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_coordenadas", "UTF-8");
		  //writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_distancias", "UTF-8");
		  
		  //Iterator<Map.Entry<Integer, SDTCoordenadas>> it3 = this.coordenadas.entrySet().iterator();
		  Iterator<Map.Entry<Integer, SDTCoordenadas>> it3 = this.coordenadas.entrySet().iterator();
		  
		  
		  writer.println("Coordenadas");
		  
		  while(it3.hasNext()){
			  Map.Entry<Integer, SDTCoordenadas> pair = it3.next();
			  
			  int parada = pair.getKey();
			  double X = pair.getValue().getLatitud();
			  double Y = pair.getValue().getLogitud();
			  
			  writer.println(Integer.toString(parada) + ',' + Double.toString(X) + ',' + Double.toString(Y));
			  //it3.next();
		  }
		  
		  
		  writer.close();
		  System.out.println("termino");
	  }
	  catch(Exception e){
		  System.out.println("No anda viejita");
	  }
  }
		
  private void readProblem(String file){
	try{
		//BufferedReader br = new BufferedReader(new FileReader("/home/pablo/Fing/AE/Proyecto/DatosDeTest/" + file));
		BufferedReader br = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/" + file));

		String line = br.readLine();
	    String[] elems = line.split(",");
	    
	    //Seteo la canitdad de variables
	    this.cantLines = elems.length;
	    
	    //Creo la matriz de pasajeros
	    this.Matrizpasajeros = new SDTSubenBajan[this.cantLines][this.cantParadas];
	    
	    for(int i = 0; i < this.cantLines; i++){
	    	//Agrego la correlacion entre numero de linea y posicicon del array
	    	this.correlacion.put(Integer.parseInt(elems[i]), i);
	    	
	    	//Leo las distancas
	    	//BufferedReader distancias = new BufferedReader(new FileReader("/home/pablo/Fing/AE/Proyecto/DatosDeTest/coordenadas"));
	    	BufferedReader distancias = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/coordenadas"));
	    	//BufferedReader distancias = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/" + elems[i]+ "_distancias"));
	    	
	    	line = distancias.readLine();

	        while (line != null) {
	        	String[] coordenada = line.split(",");
	        	this.coordenadas.put(Integer.parseInt(coordenada[0]), new SDTCoordenadas(Double.parseDouble(coordenada[1]), Double.parseDouble(coordenada[2])));
	        	
	            line = distancias.readLine();
	        }
	        
	        distancias.close();
	        
	        //Leo matriz de pasajeros
	        //BufferedReader pasajeros = new BufferedReader(new FileReader("/home/pablo/Fing/AE/Proyecto/DatosDeTest/" + elems[i]+ "_pasajeros"));
	        BufferedReader pasajeros = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/" + elems[i]+ "_pasajeros"));
	        
	        
	    	line = pasajeros.readLine();
	    	this.ordenParadas.put(Integer.parseInt(elems[i]), new LinkedList<Integer>());
	    	
	        while (line != null) {
	        	String[] linea = line.split(",");
	        	SDTSubenBajan SDT = new SDTSubenBajan(Integer.parseInt(linea[1]), Integer.parseInt(linea[2]));
	        	
	        	this.Matrizpasajeros[i][Integer.parseInt(linea[0])] = SDT;
	        	this.ordenParadas.get(Integer.parseInt(elems[i])).add(Integer.parseInt(linea[0]));
	        	
	            line = pasajeros.readLine();
	        }
	        
	        pasajeros.close();
	    	
	    }
	    br.close();
		
	}
	catch(IOException e){
		System.out.println("Archivo no encontrado");
		System.exit(-1);
	}
  }

	@Override
	public SDTSubenBajan[][] getMatrizPasajeros() {
		return Matrizpasajeros;
	}
	
	@Override
	public Map<Integer, Integer> getCorrelacion() {
		return correlacion;
	}

	@Override
	public int getCantidadDeParadas() {
		return cantParadas;
	}

	@Override
	public Map<Integer, List<Integer>> getOrdenParadas() {
		return ordenParadas;
	}

	@Override
	public int getCantidadMaximaPasajeros() {
		return cantidadMaximaPasajeros;
	}

	@Override
	public Map<Integer, SDTCoordenadas> getCoordenadas() {
		return coordenadas;
	}
	
	private double calcularDistancia(double lat1, double lon1, double lat2, double lon2){
		double distancia;
		
		// convert decimal degrees to radians 
	    //lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2]) TODO
	    //haversine formula 
	    double dlon = lon2 - lon1;
	    double dlat = lat2 - lat1;
	    
	    double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    distancia = 6367 * c;
	    
	    return (distancia * 1000);
	}
}
