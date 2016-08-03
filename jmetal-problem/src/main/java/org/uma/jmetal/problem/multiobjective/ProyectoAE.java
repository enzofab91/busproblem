//  BusProblem.java
package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractBusProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.problem.impl.SDTSubenBajan;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ProyectoAE extends AbstractBusProblem {
  private int cantLines ;
  private int dimension;
  private int cantParadas = 7800;
  private SDTSubenBajan[][] Matrizpasajeros;
  private Map<Integer,Integer> correlacion = new HashMap<Integer,Integer>(); 
  private float[][] matrizDistancias = new float[cantParadas][cantParadas];


  /** Constructor */
  
  public ProyectoAE(){
	//TODO  
  }
  
  public ProyectoAE(String lines)  {
    readProblem(lines);
    showProblem();
    System.out.println("CANT MAXIMA PASAJEROS = " + getParameter("CantidadMaximaPasajeros"));
    
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
	  
	  int cantidadMaximaPasajeros = 50; //Integer.parseInt(getParameter("CantidadMaximaPasajeros"));
	  int demoraPromedioSubir = 30; //Integer.parseInt(getParameter("DemoraPromedioSubir"));
	  
	  int[] paradas;
	  //for (int i = 0; i < this.cantLines; i++) {
		  //para cada linea recorro las paradas
		//  paradas = solution.getParadas(i);
		  
		//  for (int j = 0; j < paradas.length -1; j++){  
		//	  if (this.Matrizpasajeros[j][j+1].getSuben() - this.Matrizpasajeros[j][j+1].getBajan() 
		//			  <= cantidadMaximaPasajeros){  
		//		  fitness1 += this.Matrizpasajeros[j][j+1].getSuben() - this.Matrizpasajeros[j][j+1].getBajan();
		//		  fitness2 += this.matrizDistancias[j][j+1] + (this.Matrizpasajeros[j][j+1].getSuben() - this.Matrizpasajeros[j][j+1].getBajan()) *
		//				  demoraPromedioSubir;
		//	  } else {
				  //fitness1 += solution.getAsientosLibres(i);
				  //fitness2 += this.matrizDistancias[j][j+1] + (solution.getAsientosLibres(i) * demoraPromedioSubir);
		//	  }
		//  }
	  //}

	  solution.setObjective(0, fitness1);
	  solution.setObjective(1, fitness2);
  }
  
  public void showProblem(){
	  try{
		  Iterator<Map.Entry<Integer, Integer>> it = this.correlacion.entrySet().iterator();
		  
		  PrintWriter writer = null;
		  while (it.hasNext()) {
		      Map.Entry<Integer, Integer> pair = it.next();
		      
		      //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_" +
		      //	  Integer.toString(pair.getKey()) + "_pasajeros", "UTF-8");
		      writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_" +
		    		  Integer.toString(pair.getKey()) + "_pasajeros", "UTF-8");
			  
			  writer.println("Matrinz de pasajeros");
			  
		      writer.println("LINEA: " + Integer.toString(pair.getKey()));
		      
		      for(int i = 0; i < this.cantParadas; i++){
		    	  if (this.Matrizpasajeros[pair.getValue()][i] != null)
		    		  writer.print("(" + Integer.toString(this.Matrizpasajeros[pair.getValue()][i].getSuben()) + "," + Integer.toString(this.Matrizpasajeros[pair.getValue()][i].getBajan())+ ") ");
		    	  else
		    		  writer.print("null ");
		      }
		      writer.println();
		      writer.close();
		  }
		  
		  //writer = new PrintWriter("/home/pablo/Fing/AE/Proyecto/DatosDeTest/debug_distancias", "UTF-8");
		  writer = new PrintWriter("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/debug_distancias", "UTF-8");
		  
		  //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		  writer.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		  //System.out.println("Matriz de distancias");
		  writer.println("Matriz de distancias");
		  for(int i = 0; i < this.cantParadas; i++){
			  for(int j = 0; j < this.cantParadas; j++){
				  //System.out.print(Float.toString(this.matrizDistancias[i][j]) + " ");
				  writer.print(Float.toString(this.matrizDistancias[i][j]) + " ");
				  
			  }
			  //System.out.println();
			  writer.println();
		  }
		  
		  writer.close();
		  System.out.println("termino");
	  }
	  catch(Exception e){
		  System.out.println("No anda viejita");
	  }
  }
  
  private String getParameter(String ParameterName){
	  String valor = "";
	  try{
		    BufferedReader parametros = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/parametros"));
		    String line = parametros.readLine();

		    while (line != null) {
		       	String[] tokens = line.split("-");
		       	String token = tokens[0].trim();
		       	if (ParameterName.trim().equals(token)){
		       		valor = tokens[1];
		       	}
		        line = parametros.readLine();
		    }
			
			parametros.close();
		}
		catch(IOException e){
			System.out.println("Archivo no encontrado");
			System.exit(-1);
		}
		return valor;
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

	        while (line != null) {
	        	String[] linea = line.split(",");
	        	SDTSubenBajan SDT = new SDTSubenBajan(Integer.parseInt(linea[1]), Integer.parseInt(linea[2]));
	        	
	        	this.Matrizpasajeros[i][Integer.parseInt(linea[0])] = SDT;
	        	
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
}