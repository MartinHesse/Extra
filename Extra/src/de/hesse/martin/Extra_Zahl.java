package de.hesse.martin;

public class Extra_Zahl {
	private int zahl;
	private int freieFelder;
	
	public Extra_Zahl(int zahl, int freieFelder){
		this.zahl = zahl;
		this.freieFelder = freieFelder;
	}
	
	public void decreaseFreieFelder (){
		this.freieFelder -= 1;
	}

	public int getFreieFelder() {
		return freieFelder;
	}
	
	public boolean enden(){
		return (freieFelder == 0);
	}

	public int getZahl() {
		return zahl;
	}
	
}
