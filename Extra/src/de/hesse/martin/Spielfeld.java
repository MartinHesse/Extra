package de.hesse.martin;

public class Spielfeld {
	
	private int [] faktor = {
		0,1,2,3,5,7	
	};

	private Zahl [] zahlen = new Zahl [11];
	
	private Extra_Zahl [] extraZahlen = new Extra_Zahl[3];
		
	public boolean doesExistZahl (int zahl){
		return !(indexOfZahl(zahl) == -1);
	}
	
	public boolean doesExistExZahl (int exZahl){
		return !(indexOfExZahl(exZahl) == -1);
	}
	
	public boolean doesExistExZahl (int[] exZahlen) {
		boolean check = false;
		for (int exZahl : exZahlen) {
			if (doesExistExZahl(exZahl)) check = true;
		}
		return check;
	}

	public boolean doesExistZahl (int[] Zahlen) {
		boolean check = false;
		for (int Zahl : Zahlen) {
			if (doesExistZahl(Zahl)) check = true;
		}
		return check;
	}
	
	public int indexOfFirstFreeZahl(){
		for (int i = 0; i < zahlen.length; i++) {
			if (zahlen[i] == null){
				return i;
			}
		}
		return -1;
	}

	public int indexOfFirstFreeExZahl(){
		for (int i = 0; i < extraZahlen.length; i++) {
			if (extraZahlen[i] == null){
				return i;
			}
		}
		return -1;
	}
	
	public int indexOfZahl(int zahl){
		for (int i = 0; i < zahlen.length; i++) {
			if (zahlen[i] != null){
				if (zahlen[i].getZahl() == zahl){
					return i;
				}				
			}
		}
		return -1;
	}
	
	public int indexOfExZahl(int exZahl){
		for (int i = 0; i < extraZahlen.length; i++) {
			if (extraZahlen[i] != null){
				if (extraZahlen[i].getZahl() == exZahl){
					return i;
				}				
			}
		}
		return -1;
	}

	public void generateZahl (int zahl){
		if (indexOfFirstFreeZahl() != -1){
			zahlen[indexOfFirstFreeZahl()] = new Zahl(zahl);			
		} else {
			System.out.println("Schon alle Zahlen generiert!");
		}
	}
	
	public void generateExZahl(int zahl) {
		int freieFelder = 9 - indexOfFirstFreeExZahl();
		extraZahlen[indexOfFirstFreeExZahl()] = new Extra_Zahl(zahl, freieFelder);
	}
	
	public int punkteOfZahl(int zahl){
		if (!doesExistZahl(zahl)){
			return 0;
		}
		Zahl tmp = zahlen[indexOfZahl(zahl)];
		int kreuze = tmp.getBesetzteFelder() - tmp.getNegativeFelder() - 1;
		if (kreuze < 0){
			return -200;
		} else if (kreuze >= 6){
			return 7 * tmp.getPunkte();
		} else {
			return faktor[kreuze] * tmp.getPunkte();
		}
	}
	
	public int gesamtpunkte() {
		int count = 0;
		int i = 0;
		while (i < 11 && zahlen[i] != null) {
			count += punkteOfZahl(zahlen[i].getZahl());
			i++;
		}
		return count;
	}
	
	public int getBesetzteFelderOf(int zahl) {
		return zahlen[indexOfZahl(zahl)].getBesetzteFelder();
	}

	public void increaseBesetzteFelderOf(int zahl) {
		zahlen[indexOfZahl(zahl)].increaseBesetzteFelder();
	}

	public int getNegativeFelderOf(int zahl) {
		return zahlen[indexOfZahl(zahl)].getNegativeFelder();
	}

	public int getPunkteOf(int zahl) {
		switch (zahl) {
		case 2: case 12:
			return 100;
		case 3: case 11:
			return 70;
		case 4: case 10:
			return 60;
		case 5: case 9:
			return 50;
		case 6: case 8:
			return 40;
		case 7:
			return 30;
		}
		return 0;
	}
	
	public void decreaseFreieFelderOf (int exZahl){
		extraZahlen[indexOfExZahl(exZahl)].decreaseFreieFelder();
	}

	public int getFreieFelderOf(int exZahl) {
		return extraZahlen[indexOfExZahl(exZahl)].getFreieFelder();
	}
	
	public boolean endetExZahl(int exZahl){
		return extraZahlen[indexOfExZahl(exZahl)].enden();
	}
	
	public boolean endet(){
		for (int i = 0; i < extraZahlen.length; i++) {
			if (extraZahlen[i] != null){
				if (extraZahlen[i].enden()){
					return true;
				}
			}
		}
		return false;
	}
	
	public Extra_Zahl[] getExtraZahlen(){
		return extraZahlen;
	}
	
	public Zahl[] getZahlen(){
		return zahlen;
	}

}
