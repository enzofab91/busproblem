//  BusProblem.java
package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractBusProblem;
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
  private float[][] matrizDistancias = new float[cantParadas][cantParadas];


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
	  double fitness1;
	  double fitness2;

	  fitness1 = 0.0 ;
	  fitness2 = 0.0 ;
	  
	  BusProblemLine linea;
	  
	  for (int i = 0; i < this.cantLines; i++) {
		  //para cada linea recorro las paradas
		  linea = solution.getVariableValue(i);
		  Iterator<BusProblemStop> paradas = linea.getParadas().iterator();
		  Iterator<BusProblemStop> paradas_aux = linea.getParadas().iterator();
		  
		  BusProblemStop parada_siguiente = null;
		  BusProblemStop parada_actual = paradas.next(); //Primer parada
		  BusProblemStop parada_actual_aux = paradas_aux.next();
		  
		  while (paradas.hasNext()){ 
			   
			  //Me aseguro que la parada existe
			  if (parada_actual.getOffset() != -1)
					  fitness1 += parada_actual.getSuben();
			  
			  
			  //Busco la siguiente parada
			  while(paradas.hasNext()){
				  parada_siguiente = paradas.next();
				  
				  fitness2 -= matrizDistancias[parada_actual_aux.getParada()][parada_siguiente.getParada()];
				  
				  if(parada_siguiente.getOffset() != -1)
					  break;
				  
				  parada_actual_aux = paradas_aux.next();
				 
			  }
			  
			  fitness2 -=	(parada_actual.getSuben() * demoraPromedioSubir) + (parada_actual.getBajan() * demoraPromedioBajar);
			  
			  parada_actual = parada_siguiente;
			  
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
		      //	  Integer.toString(pair.getKey()) + "_pasajeros", "UTF-8");
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
		      
		      writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_" +
		    		  Integer.toString(pair.getKey()) + "_orden", "UTF-8");
		      
		      Iterator<Integer> it2 = this.ordenParadas.get(pair.getKey()).iterator();
		      
		      while(it2.hasNext()){
		    	  writer.println(it2.next());
		      }
		      
		      writer.println();
		      writer.close();
		      
		  }
		  
		  //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_distancias", "UTF-8");
		  writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_distancias", "UTF-8");
		  
		  //System.out.println("Matriz de distancias");
		  writer.println("Matriz de distancias");
		  for(int i = 0; i < this.cantParadas; i++){
			  for(int j = 0; j < this.cantParadas; j++){
				  writer.print(Float.toString(this.matrizDistancias[i][j]) + " ");
				  
			  }
			  
			  writer.println();
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
	    	//BufferedReader distancias = new BufferedReader(new FileReader("/home/pablo/Fing/AE/Proyecto/DatosDeTest/" + elems[i]+ "_distancias"));
	    	BufferedReader distancias = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/" + elems[i]+ "_distancias"));
	    	
	    	line = distancias.readLine();

	        while (line != null) {
	        	String[] distancia = line.split(",");
	        	this.matrizDistancias[Integer.parseInt(distancia[0])][Integer.parseInt(distancia[1])] =Float.parseFloat(distancia[2]);
	        	
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
	public float[][] getMatrizDistancia() {
		return matrizDistancias;
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
}
