package de.hesse.martin;

public class Würfel {
	
	private int würfelseiten;
	private int zahl = 0;
	
	public Würfel(int würfelseiten){
		this.würfelseiten = würfelseiten;
	}
	
	public void random(){
		this.zahl = (int) ((Math.random() * this.würfelseiten) + 1);
	}

	public int getWürfelseiten() {
		return würfelseiten;
	}

	public int getZahl() {
		return zahl;
	}
}
