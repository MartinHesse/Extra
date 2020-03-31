package de.hesse.martin;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name) {
		this.name = name;
	}
	
	public HumanPlayer(){};
	
	@Override
	public int pickExZahl(int[]würfel, Würfel[]ganzeWürfel) {
		System.out.println("Bitte eine Extra-Zahl angeben!");
		int exZahl = Hilfe.intEinlesen(1, ganzeWürfel[0].getWürfelseiten());
		return exZahl;
	}
	
	@Override
	public int pickZahlen(int[]würfel, Würfel[]ganzeWürfel) {
		System.out.println("Bitte eine Zahl des Zahlenpaares eingeben!");
		int zahl = Hilfe.intEinlesen(1, ganzeWürfel[0].getWürfelseiten());
		return zahl;
	}	
}
