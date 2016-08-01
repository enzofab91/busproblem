package org.uma.jmetal.solution.impl;

public class BusProblemStop {
	private int suben;
	private int bajan;
	private int parada;
	private float offset;
	
	public BusProblemStop(int suben, int bajan, int parada, float offset){
		this.suben = suben;
		this.bajan = bajan;
		this.parada = parada;
		this.offset = offset;
	}
	
	public int getSuben(){
		return suben;
	}
	
	public int getBajan(){
		return bajan;
	}
	
	public int getParada(){
		return parada;
	}
	
	public float getOffset(){
		return offset;
	}
	
	public void setOffset(float offset){
		this.offset = offset;
	}

}
