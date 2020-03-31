package de.hesse.martin;

public class PenaltyPlayer extends Player {

	private int eingegebeneZahlen = 0;
	
	//Variabelen um die Penalty zu justieren:
	private int ezF = 1; //Extra-Zahl-Faktor(1)
	private int nrF = 1; //Neue-Reihe-Faktor(1)
	private int rpbF = 1; //Reihe-ins-Positive-bringen-Faktor(1)
	private int nkF = 1; //Normales-Kreuz-Faktor(1)
	private int nbF = 1; //Negativer-Bereich-Faktor(1)
	
	private boolean extraZahlEingeben = false;
	private boolean freieExtraZahlWahl = false;
	private int[] validExZahlen = new int[5];
	private int moveCount = 0;
	private boolean lastMove = false;
	
	private int exZahl = 0;
	private int currExZahl = 0;
	private int[] zahlen = new int[4];
	private int[] currZahlen = new int[4];
	private int penalty = Integer.MAX_VALUE;
	private int currPenalty = 0;
	
	@Override
	public void reset(){
		games++;
		averagePoints = averagePoints + (1.0 * punkte / games) - (1.0 * averagePoints / games);
		sp = new Spielfeld();
		spielStatus = true;
		punkte = 0;
		anzKeineExZahl = 0;
		moveCount = 0;
		lastMove = false;
	}
		
	public PenaltyPlayer() {};
	
	public PenaltyPlayer(String name){
		this.name = name;
	}
	
	public PenaltyPlayer (String name, int ezF, int nrF, int rpbF, int nkF, int nbF){
		this.name = name;
		this.ezF = ezF;
		this.nrF = nrF;
		this.rpbF = rpbF;
		this.nkF = nkF;
		this.nbF = nbF;
	}

	@Override
	protected int pickExZahl(int[] würfel, Würfel[] ganzeWürfel) {
		extraZahlEingeben  = true;
		freieExtraZahlWahl = true;
		Hilfe.clear1Array(validExZahlen);
		if (this.sp.indexOfFirstFreeExZahl() == -1){
			freieExtraZahlWahl = false;
			for (int i = 0; i < würfel.length; i++) {
				if (this.sp.doesExistExZahl(würfel[i])) validExZahlen[i] = würfel[i];
			}
		}
		tryZahlen(würfel);
//		System.out.println("Ex-Zahl ausgesucht: " + exZahl );
		return exZahl;
	}

	private void tryZahlen(int[] würfel) {
		for (int i = 0; i < validExZahlen.length; i++) {
			currPenalty = 0;
			if (freieExtraZahlWahl || validExZahlen[i] != 0 || !extraZahlEingeben) { //ist die aktuelle Zahl (würfel[i]) eine gültige Extra-Zahl
				//ggf. currExZahl bearbeiten
				int currExPenalty = 0;
				int currExFreieFelder = 0;
				lastMove = false;
				if (extraZahlEingeben) {
					currExZahl = würfel[i];
					if (this.sp.doesExistExZahl(currExZahl)) currExFreieFelder = this.sp.getExtraZahlen()[this.sp.indexOfExZahl(currExZahl)].getFreieFelder();
					else currExFreieFelder = 9 - this.sp.indexOfFirstFreeExZahl();
					currExFreieFelder--;
					if (currExFreieFelder == 0) lastMove = true;
					calcExPenalty(currExFreieFelder);
					currExPenalty = currPenalty;
				}
				//currZahlen[] erstellen
				int k = 0;
				currZahlen = new int[4];
				for (int j = 0; j < currZahlen.length; j++) {
					if (k == i) k++;
					currZahlen[j] = würfel[k];
					k++;
				}
				//Zahlenpaarkombination 1 testen
				calcZahlenPenalty();
//				versuchAusgeben();
				if(currPenalty < penalty) currZuAllgÜbernehmen();
				//Zahlenpaarkombination 2 testen
				Hilfe.swap(currZahlen, 1, 2);
				currPenalty = currExPenalty;				
				calcZahlenPenalty();
//				versuchAusgeben();
				if(currPenalty < penalty) currZuAllgÜbernehmen();
				//Zahlenpaarkombination 3 testen
				Hilfe.swap(currZahlen, 1, 3);
				currPenalty = currExPenalty;
				calcZahlenPenalty();
//				versuchAusgeben();
				if (currPenalty < penalty) currZuAllgÜbernehmen();
			}			
		}
	}
	
	private void versuchAusgeben() {
		System.out.print("Extra-Zahl: " + currExZahl + "; Zahlen: ");
		Hilfe.array1ausgeben(currZahlen);
		System.out.println("Penalty: " + currPenalty);		
	}

	private void currZuAllgÜbernehmen() {
		penalty = currPenalty;
		Hilfe.copy1Array(zahlen, currZahlen);
		if (extraZahlEingeben) exZahl = currExZahl;
	}
	
	private int nrp(int zp){
		int tmp = (int) (nrF * (-Math.exp(-((moveCount / 3) - 5)) + 175 + (0.25 * this.sp.getPunkteOf(zp))));
		if (lastMove){
			tmp += nrF * 100;
		}
		return tmp;
	}
	
	private int rpbp() {
		int tmp = -(int) (rpbF * (0.25 * Math.pow(moveCount, 2) + 100));
		if (tmp < -200) tmp = -200;
		return tmp;
	}
	
	private int nkp(int zp) {
		int tmp = -(int) (nkF * (0.25 * moveCount + this.sp.getPunkteOf(zp) - 5));
		if (tmp < -this.sp.getPunkteOf(zp)) tmp = -this.sp.getPunkteOf(zp);
		return tmp;
	}
	
	private int nbp(int zp, int kr) {
		int tmp = -(int) (nbF * (-Math.exp(moveCount - 18 - kr) + this.sp.getPunkteOf(zp)));
		if (lastMove || tmp < 0) tmp = 0;
		return tmp;
	}

	private void calcZahlenPenalty() {
		//Zahlenpaare zwischenspeichern
		int zp1 = currZahlen[0] + currZahlen[1];
		int zp2 = currZahlen[2] + currZahlen[3];
		//zp1 existiert nicht:
		if (!this.sp.doesExistZahl(zp1)){
			currPenalty += nrp(zp1);
			if (zp1 == zp2){
				int kr = 0;
				switch (zp2) {
				case 2: case 3: case 11: case 12:
					kr = -2;
					break;
				case 4: case 5: case 9: case 10:
					kr = -3;
					break;
				case 6: case 7: case 8:
					kr = -4;
					break;
				}
				calcZp2Penalty(zp2, true,kr);
			} else {
				boolean zp2exist = false;
				int kr = 0;
				if (this.sp.doesExistZahl(zp2)){
					zp2exist = true;
					kr = this.sp.getBesetzteFelderOf(zp2) - this.sp.getNegativeFelderOf(zp2);
				}
				calcZp2Penalty(zp2, zp2exist, kr);
			}
		} else {
			int kr = this.sp.getBesetzteFelderOf(zp1) - this.sp.getNegativeFelderOf(zp1);
			//zp1 wird ins Positive gebracht:
			if(kr == 0){
				currPenalty += rpbp();
				if (zp1 == zp2){
					calcZp2Penalty(zp2, true, kr + 1);
				} else {
					boolean zp2exist = false;
					kr = 0;
					if (this.sp.doesExistZahl(zp2)){
						zp2exist = true;
						kr = this.sp.getBesetzteFelderOf(zp2) - this.sp.getNegativeFelderOf(zp2);
					}
					calcZp2Penalty(zp2, zp2exist, kr);					
				}
			//zp1 geht eine Wertungsstufe hoch:	
			} else if (1 <= kr && kr <= 3) {
				currPenalty += nkp(zp1);
				if (zp1 == zp2){
					calcZp2Penalty(zp2, true, kr + 1);
				} else {
					boolean zp2exist = false;
					kr = 0;
					if (this.sp.doesExistZahl(zp2)){
						zp2exist = true;
						kr = this.sp.getBesetzteFelderOf(zp2) - this.sp.getNegativeFelderOf(zp2);
					}
					calcZp2Penalty(zp2, zp2exist, kr);					
				}
			//zp1 geht zwei Wertungstufen hoch:
			} else if (4 <= kr && kr <= 5){
				currPenalty += 2 * nkp(zp1);
				if (zp1 == zp2){
					calcZp2Penalty(zp2, true, kr + 1);
				} else {
					boolean zp2exist = false;
					kr = 0;
					if (this.sp.doesExistZahl(zp2)){
						zp2exist = true;
						kr = this.sp.getBesetzteFelderOf(zp2) - this.sp.getNegativeFelderOf(zp2);
					}
					calcZp2Penalty(zp2, zp2exist, kr);					
				}
			//zp1 bleibt im negativen Bereich:
			} else if (kr <= -1){
				currPenalty += nbp(zp1, kr);
				if (zp1 == zp2){
					calcZp2Penalty(zp2, true, kr + 1);
				} else {
					boolean zp2exist = false;
					kr = 0;
					if (this.sp.doesExistZahl(zp2)){
						zp2exist = true;
						kr = this.sp.getBesetzteFelderOf(zp2) - this.sp.getNegativeFelderOf(zp2);
					}
					calcZp2Penalty(zp2, zp2exist, kr);					
				}				
			}
		}
	}

	private void calcZp2Penalty(int zp, boolean zpExistiert, int kr) {
		if(!zpExistiert){
			currPenalty += nrp(zp);			
		} else if(kr == 0){
			currPenalty += rpbp();			
		} else if(1 <= kr && kr <= 3){
			currPenalty += nkp(zp);
		} else if (4 <= kr && kr <= 5) {
			currPenalty += 2 * nkp(zp);
		} else if (kr <= -1) {
			currPenalty += nbp(zp, kr);
		}
	}

	private void calcExPenalty(int freieFelder) {
		currPenalty = (int) (ezF * (25 + 75 * Math.pow(2, -freieFelder)));
		if (freieFelder == 0) currPenalty += 100;
	}

	@Override
	protected int pickZahlen(int[] würfel, Würfel[] ganzeWürfel) {
		if (!extraZahlEingeben) {
			tryZahlen(würfel);
			extraZahlEingeben = true;
		}
//		System.out.println("Zahl " + eingegebeneZahlen + " eingegeben: " + zahlen [eingegebeneZahlen]);
		if (eingegebeneZahlen == 3) {
			int tmp = zahlen[3];
			eingegebeneZahlen = 0;
			extraZahlEingeben = false;
			freieExtraZahlWahl = false;
			moveCount++;
			Hilfe.clear1Array(validExZahlen);
			exZahl = 0;
			currExZahl = 0;
			Hilfe.clear1Array(zahlen);
			Hilfe.clear1Array(currZahlen);
			penalty = Integer.MAX_VALUE;
			currPenalty = 0;
			return tmp;
		}
		eingegebeneZahlen ++;
		return zahlen[eingegebeneZahlen - 1];
	}
}
