package de.hesse.martin;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name) {
		this.name = name;
	}
	
	public HumanPlayer(){};
	
	@Override
	public int pickExZahl(int[]w�rfel, W�rfel[]ganzeW�rfel) {
		System.out.println("Bitte eine Extra-Zahl angeben!");
		int exZahl = Hilfe.intEinlesen(1, ganzeW�rfel[0].getW�rfelseiten());
		return exZahl;
	}
	
	@Override
	public int pickZahlen(int[]w�rfel, W�rfel[]ganzeW�rfel) {
		System.out.println("Bitte eine Zahl des Zahlenpaares eingeben!");
		int zahl = Hilfe.intEinlesen(1, ganzeW�rfel[0].getW�rfelseiten());
		return zahl;
	}	
}
