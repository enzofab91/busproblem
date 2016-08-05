package org.uma.jmetal.solution.impl;

public class BusProblemStop {
	private int suben;
	private int bajan;
	private int parada;
	private int offset;
	private static int MAX_K = 10000;
	
	public BusProblemStop(int suben, int bajan, int parada, int offset){
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
	
	public void setOffset(int offset){
		this.offset = offset;
	}
	
	public void setSuben(int suben){
		this.suben = suben;
	}
	
	public void setBajan(int bajan){
		this.bajan = bajan;
	}
	
	public static int getMAX_K(){
		return ++MAX_K;
	}
	

}
