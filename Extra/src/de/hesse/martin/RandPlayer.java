package de.hesse.martin;

public class RandPlayer extends Player {

	public RandPlayer(String name) {
		this.name = name;
	}
	
	public RandPlayer(){};
	
	@Override
	protected int pickExZahl(int[] w�rfel, W�rfel[] ganzeW�rfel) {
		return w�rfel[(int) (Math.random() * w�rfel.length)];
	}

	@Override
	protected int pickZahlen(int[] w�rfel, W�rfel[] ganzeW�rfel) {
		return w�rfel[(int) (Math.random() * w�rfel.length)];
	}

}
