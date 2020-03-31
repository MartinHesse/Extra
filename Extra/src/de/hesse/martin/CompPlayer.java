package de.hesse.martin;

/*
 * Dieser Spieler versucht:
 * 1.: möglichst wenig neue Zahlenreihen anzufangen
 * 2.: Zahlenreihen nahe der Mitte anzufangen
 * 
 */

public class CompPlayer extends Player{
	
	public CompPlayer(){};
	
	public CompPlayer(String name) {
		this.name = name;
	}
	
	private int[] zahlen = new int[4];
	private int[] currZahlen = new int[4];
	private int zp1 = 0;
	private int zp2 = 0;
	private int currzp1 = 0;
	private int currzp2 = 0;
	private int exZahl = 0;
	private int currExZahl = 0;
	private int neueZahlenreihen = 0;
	private int currNeueZahlenreihen = 0;
	private int absZuMitte = 0;
	private int currAbsZuMitte = 0;
	
	private int[] validExZahlen = new int[5];
	private int eingegebeneZahlen = 0;
	private boolean extraZahlEingeben = false;
	private boolean freieExtraZahlWahl = false;
	
	@Override
	protected int pickExZahl(int[] würfel, Würfel[] ganzeWürfel) {
		extraZahlEingeben = true;
		freieExtraZahlWahl = true;
		Hilfe.clear1Array(validExZahlen);
		if (this.sp.indexOfFirstFreeExZahl() == -1){
			freieExtraZahlWahl = false;
			for (int i = 0; i < würfel.length; i++) {
				if (this.sp.doesExistExZahl(würfel[i])) validExZahlen[i] = würfel[i];
			}
		}
		tryZahlen(würfel);
//		System.out.println("Ex-Zahl ausgesucht: " + exZahl);
		return exZahl;
	}
	
	private void generateZahlen() {
		currNeueZahlenreihen = 0;
		currzp1 = currZahlen[0] + currZahlen[1];
		currzp2 = currZahlen[2] + currZahlen[3];
		currAbsZuMitte = Math.abs(currzp1 - 7) + Math.abs(currzp2 - 7);
		if (!this.sp.doesExistZahl(currzp1)) currNeueZahlenreihen++;
		if (currzp1 != currzp2 && !this.sp.doesExistZahl(currzp2)) currNeueZahlenreihen++;
		if (currNeueZahlenreihen < neueZahlenreihen || currNeueZahlenreihen == neueZahlenreihen && currAbsZuMitte < absZuMitte) currZuAllgÜbernehmen();
	}
	
	private void currZuAllgÜbernehmen(){
		neueZahlenreihen = currNeueZahlenreihen;
		zp1 = currzp1;
		zp2 = currzp2;
		absZuMitte = currAbsZuMitte;
		Hilfe.copy1Array(zahlen, currZahlen);
		if (extraZahlEingeben) exZahl = currExZahl;
	}
	
	private void tryZahlen(int[] würfel) {
		neueZahlenreihen = Integer.MAX_VALUE;
		absZuMitte = Integer.MAX_VALUE;
		for (int i = 0; i < validExZahlen.length; i++) {
			if(!extraZahlEingeben || validExZahlen[i] != 0 || freieExtraZahlWahl){
				int k = 0;
				currZahlen = new int[4];
				currExZahl = würfel[i];
				for (int j = 0; j < currZahlen.length; j++) {
					if (k == i) k++;
					currZahlen[j] = würfel[k];
					k++;
				}
				generateZahlen();
				Hilfe.swap(currZahlen, 1, 2);
				generateZahlen();
				Hilfe.swap(currZahlen, 1, 3);
				generateZahlen();				
			}
		}
	}

	@Override
	protected int pickZahlen(int[] würfel, Würfel[] ganzeWürfel) {
		if (!extraZahlEingeben) {
			tryZahlen(würfel);
			extraZahlEingeben = true;
		}
//		System.out.println("Zahl " + eingegebeneZahlen + " eingegeben: " + zahlen[eingegebeneZahlen]);
		if (eingegebeneZahlen == 3) {
			int tmp = zahlen[3];
			Hilfe.clear1Array(validExZahlen);
			freieExtraZahlWahl = false;
			eingegebeneZahlen = 0;
			Hilfe.clear1Array(zahlen);
			exZahl = 0;
			zp1 = 0;
			zp2 = 0;
			currzp1 = 0;
			currzp2 = 0;
			neueZahlenreihen = 0;
			currNeueZahlenreihen = 0;
			currExZahl = 0;
			Hilfe.clear1Array(currZahlen);
			absZuMitte = 0;
			currAbsZuMitte = 0;
			extraZahlEingeben = false;
			return tmp;
		}
		eingegebeneZahlen ++;
		return zahlen[eingegebeneZahlen - 1];
	}
}