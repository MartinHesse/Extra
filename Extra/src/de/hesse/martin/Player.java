package de.hesse.martin;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	protected abstract int pickExZahl(int[]w�rfel, W�rfel[] ganzeW�rfel);
	protected abstract int pickZahlen(int [] w�rfel, W�rfel[] ganzeW�rfel);
	
	protected Spielfeld sp = new Spielfeld();
	protected boolean spielStatus = true;
	protected int punkte;
	protected double averagePoints = 0.0;
	protected double stdabw = 0.0;
	protected List<Integer> allPoints = new ArrayList<>();
	protected int games = 0;
	protected String name;
	protected int anzKeineExZahl = 0;

	
	public void reset() {
		games++;
		averagePoints = averagePoints + (1.0 * punkte / games) - (1.0 * averagePoints / games);
		allPoints.add(punkte);
		sp = new Spielfeld();
		spielStatus = true;
		punkte = 0;
		anzKeineExZahl = 0;
	}
	
	public void move(W�rfel [] ganzeW�rfel){
		
//		System.out.println(name + " ist am Zug:");
		
//		spielsituationausgeben();
				
		//Zahlen von den W�rfeln zwischenspeichern
		int[] w�rfel = new int [ganzeW�rfel.length];
		for (int i = 0; i < w�rfel.length; i++) {
			w�rfel[i] = ganzeW�rfel[i].getZahl();
		}
		
		//W�rfelergebnisse werden ausgegeben
//		System.out.print("Es wurde folgendes gew�rfelt: ");
//		Hilfe.array1ausgeben(w�rfel);
		
		//Extra-Zahl einlesen:
		int exZahl = validateExZahl(w�rfel, ganzeW�rfel);
		
		//Extra-Zahl ggf. speichern:
		if(exZahl != 0){
			if(!sp.doesExistExZahl(exZahl)){
				sp.generateExZahl(exZahl);
			}
			sp.decreaseFreieFelderOf(exZahl);				
		}
		
		int[]zahlen = validateZahlen(w�rfel, ganzeW�rfel);
				
		//zahlenpaare berechnen
		int zp1 = zahlen[0] + zahlen[1];
		int zp2 = zahlen[2] + zahlen[3];
		
		//zahlenpaar 1 speichern
		if(!sp.doesExistZahl(zp1)){
			sp.generateZahl(zp1);
		}
		sp.increaseBesetzteFelderOf(zp1);
		
		//zahlenpaar 2 speichern
		if(!sp.doesExistZahl(zp2)){
			sp.generateZahl(zp2);
		}
		sp.increaseBesetzteFelderOf(zp2);
		
//		spielsituationausgeben();
		
		//pr�ft ob Spiel vorbei ist
		if(sp.endet()){
			spielStatus = false;
		}
		
		//berechnet und speichert aktuelle Punkte
		punkte = sp.gesamtpunkte();
	}
	
	public int validateExZahl(int[]w�rfel, W�rfel[] ganzeW�rfel){
		//Variabelen f�r das Einlesen der Extra-Zahl
		boolean exZahlEingeben = false;
		int exZahl = 0;
		
		//Extra-Zahl ggf. eingeben lassen:
		if(sp.indexOfFirstFreeExZahl() != -1 || sp.doesExistExZahl(w�rfel[0]) || sp.doesExistExZahl(w�rfel[1]) || sp.doesExistExZahl(w�rfel[2]) || sp.doesExistExZahl(w�rfel[3]) || sp.doesExistExZahl(w�rfel[4])){
			exZahlEingeben = true;
		}
		if (!exZahlEingeben) {
			anzKeineExZahl++;
		}
		while (exZahlEingeben){
			//Extra-Zahl einlesen
			while (!abgleichenMitW�rfeln(exZahl, w�rfel)) {
				exZahl = pickExZahl(w�rfel, ganzeW�rfel);
			}
			if (sp.indexOfFirstFreeExZahl() != -1){
				exZahlEingeben = false;
			} else {
				//eingegebene Extrazahl pr�fen
				if (sp.doesExistExZahl(exZahl)){
					exZahlEingeben = false;
				}
				if (exZahlEingeben){
					for (int i = 0; i < w�rfel.length; i++) {
						if (w�rfel[i] == -1){
							w�rfel[i] = ganzeW�rfel[i].getZahl();
							exZahl = 0;
						}
					}
				}
			}
		}
		return exZahl;
	}
	
	public int[] validateZahlen(int [] w�rfel, W�rfel[] ganzeW�rfel) {
		//Variabelen f�r das Einlesen der Zahlen(-paare)
		int [] zahlen = {
				0,0,0,0
		};
		//zahlen(-paare) einlesen
		for (int i = 0; i < 4; i++){
			while (zahlen[i] <= 0 || !abgleichenMitW�rfeln(zahlen[i], w�rfel)) {
				zahlen[i] = pickZahlen(w�rfel, ganzeW�rfel);
			}
		}
		return zahlen;
	}

	protected void spielsituationausgeben() {
		int anzahlExtraZahlen;
		if (sp.indexOfFirstFreeExZahl() == -1){
			anzahlExtraZahlen = 3;
		} else {
			anzahlExtraZahlen = sp.indexOfFirstFreeExZahl();
		}
		System.out.println("Spielfeld von " + this.name + ":");
		System.out.println("Extra-Zahlen:");
		System.out.println();
		for (int i = 0; i < anzahlExtraZahlen; i++) {
			System.out.println(sp.getExtraZahlen()[i].getZahl() + ": mit " + sp.getExtraZahlen()[i].getFreieFelder() + " freien Feldern." );
		}
		System.out.println();
		System.out.println("Zahlenfeld:");
		System.out.println();
		for (int i = 2; i <= 12; i++) {
			if(i < 10) {
				System.out.print(" ");
			}
			int freieStellen;
			int negativStellen;
			System.out.print(i + ": ");
			if (i == 2 || i == 3 || i == 11 || i == 12){
				freieStellen = 2;
			} else if (i == 4 || i == 5 || i == 9 || i == 10){
				freieStellen = 1;
			} else{
				freieStellen = 0;
			}
			for (int k = 0; k < freieStellen; k++) {
				System.out.print(" ");
			}
			if (sp.indexOfZahl(i) == -1){
				if (i == 2 || i == 3 || i == 11 || i == 12){
					negativStellen = 3;
				} else if (i == 4 || i == 5 || i == 9 || i == 10){
					negativStellen= 4;
				} else{
					negativStellen = 5;
				}
				for (int k = 0; k < negativStellen; k++) {
					System.out.print("O");
				}
				System.out.print("|      |");
			} else {
				int vordereKreuze;
				int vordereLuecken;
				if (sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder() - sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder() >= 0){
					vordereKreuze = sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder();
					vordereLuecken = 0;
				} else {
					vordereKreuze = sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder();
					vordereLuecken = sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder() - sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder();
				}
				for (int k = 0; k < vordereKreuze; k++) {
					System.out.print("X");
				}
				for (int k = 0; k < vordereLuecken; k++) {
					System.out.print("O");
				}
				System.out.print("|");
				if (sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder() - sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder() >= 0){
					int hintereKereuze;
					int hintereLuecken;
					if (sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder() - sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder() <= 6){
						hintereKereuze = sp.getZahlen()[sp.indexOfZahl(i)].getBesetzteFelder() - sp.getZahlen()[sp.indexOfZahl(i)].getNegativeFelder();
						hintereLuecken = 6 - hintereKereuze;
					} else {
						hintereKereuze = 6;
						hintereLuecken = 0;
					}
					for (int k = 0; k < hintereKereuze; k++) {
						System.out.print("X");
					}
					for (int k = 0; k < hintereLuecken; k++) {
						System.out.print(" ");
					}
					System.out.print("|");
				} else {
					System.out.print("      |");
				}
			}
			System.out.println();
		}
		System.out.println();		
	}

	protected boolean abgleichenMitW�rfeln(int input, int [] w�rfel){
		for (int i = 0; i < w�rfel.length; i++) {
			if(w�rfel[i] == input){
				w�rfel[i] = -1;
				return true;
			}
		}
		return false;
	}

	public boolean getSpielStatus() {
		return spielStatus;
	}

	public int getPunkte() {
		return punkte ;
	}

	public String getName() {
		return name;
	}
	
	public int getAnzKeineExZahl() {
		return anzKeineExZahl;
	}
}
