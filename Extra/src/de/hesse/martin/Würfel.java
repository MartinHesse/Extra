package de.hesse.martin;

public class W�rfel {
	
	private int w�rfelseiten;
	private int zahl = 0;
	
	public W�rfel(int w�rfelseiten){
		this.w�rfelseiten = w�rfelseiten;
	}
	
	public void random(){
		this.zahl = (int) ((Math.random() * this.w�rfelseiten) + 1);
	}

	public int getW�rfelseiten() {
		return w�rfelseiten;
	}

	public int getZahl() {
		return zahl;
	}
}
