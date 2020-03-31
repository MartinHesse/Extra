package de.hesse.martin;

public class RandPlayer extends Player {

	public RandPlayer(String name) {
		this.name = name;
	}
	
	public RandPlayer(){};
	
	@Override
	protected int pickExZahl(int[] würfel, Würfel[] ganzeWürfel) {
		return würfel[(int) (Math.random() * würfel.length)];
	}

	@Override
	protected int pickZahlen(int[] würfel, Würfel[] ganzeWürfel) {
		return würfel[(int) (Math.random() * würfel.length)];
	}

}
