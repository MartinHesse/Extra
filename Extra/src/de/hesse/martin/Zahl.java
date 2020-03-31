package de.hesse.martin;

public class Zahl {
	final private int zahl;
	final private int negativeFelder;
	private int besetzteFelder;
	final private int punkte;
	
	public Zahl (int zahl){
		this.zahl = zahl;
		if (zahl == 2 || zahl == 12){
			negativeFelder = 3;
			this.punkte = 80;
		} else if (zahl == 3 || zahl == 11){
			negativeFelder = 3;
			this.punkte = 70;
		} else if (zahl == 4 || zahl == 10){
			negativeFelder = 4;
			this.punkte = 60;
		} else if (zahl == 5 || zahl == 9){
			negativeFelder = 4;
			this.punkte = 50;
		} else if (zahl == 6 || zahl == 8){
			negativeFelder = 5;
			this.punkte = 40;
		} else if (zahl == 7){
			negativeFelder = 5;
			this.punkte = 30;
		} else {
			negativeFelder = 0;
			this.punkte = 0;
		}
		this.besetzteFelder = 0;
	}

	public int getZahl() {
		return zahl;
	}

	public int getBesetzteFelder() {
		return besetzteFelder;
	}
	
	public void setBesetzteFelder(int besetzteFelder) {
		this.besetzteFelder = besetzteFelder;
	}

	public void increaseBesetzteFelder() {
		this.besetzteFelder += 1;
	}

	public int getNegativeFelder() {
		return negativeFelder;
	}

	public int getPunkte() {
		return punkte;
	}
	
}
