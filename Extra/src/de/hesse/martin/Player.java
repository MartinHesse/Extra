package de.hesse.martin;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	protected abstract int pickExZahl(int[]würfel, Würfel[] ganzeWürfel);
	protected abstract int pickZahlen(int [] würfel, Würfel[] ganzeWürfel);
	
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
	
	public void move(Würfel [] ganzeWürfel){
		
//		System.out.println(name + " ist am Zug:");
		
//		spielsituationausgeben();
				
		//Zahlen von den Würfeln zwischenspeichern
		int[] würfel = new int [ganzeWürfel.length];
		for (int i = 0; i < würfel.length; i++) {
			würfel[i] = ganzeWürfel[i].getZahl();
		}
		
		//Würfelergebnisse werden ausgegeben
//		System.out.print("Es wurde folgendes gewürfelt: ");
//		Hilfe.array1ausgeben(würfel);
		
		//Extra-Zahl einlesen:
		int exZahl = validateExZahl(würfel, ganzeWürfel);
		
		//Extra-Zahl ggf. speichern:
		if(exZahl != 0){
			if(!sp.doesExistExZahl(exZahl)){
				sp.generateExZahl(exZahl);
			}
			sp.decreaseFreieFelderOf(exZahl);				
		}
		
		int[]zahlen = validateZahlen(würfel, ganzeWürfel);
				
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
		
		//prüft ob Spiel vorbei ist
		if(sp.endet()){
			spielStatus = false;
		}
		
		//berechnet und speichert aktuelle Punkte
		punkte = sp.gesamtpunkte();
	}
	
	public int validateExZahl(int[]würfel, Würfel[] ganzeWürfel){
		//Variabelen für das Einlesen der Extra-Zahl
		boolean exZahlEingeben = false;
		int exZahl = 0;
		
		//Extra-Zahl ggf. eingeben lassen:
		if(sp.indexOfFirstFreeExZahl() != -1 || sp.doesExistExZahl(würfel[0]) || sp.doesExistExZahl(würfel[1]) || sp.doesExistExZahl(würfel[2]) || sp.doesExistExZahl(würfel[3]) || sp.doesExistExZahl(würfel[4])){
			exZahlEingeben = true;
		}
		if (!exZahlEingeben) {
			anzKeineExZahl++;
		}
		while (exZahlEingeben){
			//Extra-Zahl einlesen
			while (!abgleichenMitWürfeln(exZahl, würfel)) {
				exZahl = pickExZahl(würfel, ganzeWürfel);
			}
			if (sp.indexOfFirstFreeExZahl() != -1){
				exZahlEingeben = false;
			} else {
				//eingegebene Extrazahl prüfen
				if (sp.doesExistExZahl(exZahl)){
					exZahlEingeben = false;
				}
				if (exZahlEingeben){
					for (int i = 0; i < würfel.length; i++) {
						if (würfel[i] == -1){
							würfel[i] = ganzeWürfel[i].getZahl();
							exZahl = 0;
						}
					}
				}
			}
		}
		return exZahl;
	}
	
	public int[] validateZahlen(int [] würfel, Würfel[] ganzeWürfel) {
		//Variabelen für das Einlesen der Zahlen(-paare)
		int [] zahlen = {
				0,0,0,0
		};
		//zahlen(-paare) einlesen
		for (int i = 0; i < 4; i++){
			while (zahlen[i] <= 0 || !abgleichenMitWürfeln(zahlen[i], würfel)) {
				zahlen[i] = pickZahlen(würfel, ganzeWürfel);
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

	protected boolean abgleichenMitWürfeln(int input, int [] würfel){
		for (int i = 0; i < würfel.length; i++) {
			if(würfel[i] == input){
				würfel[i] = -1;
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
