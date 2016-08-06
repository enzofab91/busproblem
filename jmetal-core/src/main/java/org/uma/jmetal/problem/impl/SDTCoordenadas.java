package org.uma.jmetal.problem.impl;

public class SDTCoordenadas {
	private double latitud;
	private double logitud;
	
	public SDTCoordenadas(double latitud, double longitud){
		this.latitud = latitud;
		this.logitud = longitud;
	}
	
	public double getLatitud() {
		return latitud;
	}
	
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	public double getLogitud() {
		return logitud;
	}
	
	public void setLogitud(double logitud) {
		this.logitud = logitud;
	}
}
