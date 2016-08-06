package org.uma.jmetal.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class obtenerParametros {
	
	public static String getParameter(String ParameterName){
		  String valor = "";
		  try{
			  	BufferedReader parametros = new BufferedReader(new FileReader("/home/pablo/Fing/AE/Proyecto/DatosDeTest/parametros"));
			    //BufferedReader parametros = new BufferedReader(new FileReader("/home/enzofabbiani/Desktop/AE/PROYECTO/DatosDeTest/parametros"));
			    String line = parametros.readLine();

			    while (line != null &&  valor.equals("")) {
			       	String[] tokens = line.split(":");
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
}
