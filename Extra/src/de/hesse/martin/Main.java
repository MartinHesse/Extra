package de.hesse.martin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Main {
	public static Würfel [] würfel = new Würfel [5];
	public static List<Player> players = new LinkedList<>();
	
	public static int totalPlayers;
	public static int totalHumanPlayers;
	public static int totalCompPlayers;
	public static int totalRandPlayers;
	public static int totalPenaltyPlayers;
	
	public static long sumTotalTime = 0;
	
	public static int finishedPlayers = 0;
	
	public static void main(String[] args) {
		final int amountGames = 200000;
		preGame1();
		long timeStart = 0;
		long timeEnd = 0;
		for (int i = 0; i < amountGames; i++){
			if (i % 100 == 0 && i != 0){
				timeEnd = System.currentTimeMillis();
				System.out.println("Spiel Nr. " + (i + 1));
				long roundTime = timeEnd - timeStart;
				System.out.println("Time for 100 rounds: " + roundTime + "ms");
			}
			game();
			for (Player pl : players) {
				pl.reset();
			}
			if (i % 100 == 0){
				timeStart = System.currentTimeMillis();
			}
		}
		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				double avgp1 = o1.averagePoints;
				double avgp2 = o2.averagePoints;
				if (avgp1 == avgp2) return 0;
				if (avgp1 < avgp2) return 1;
				else return -1;
			}
		});
		for (Player p : players) {
			//Durchschnittspunkte:
			double avgPoints = p.averagePoints;
			//Standardabweichung:
			double stdabw = 0;
			for (int i : p.allPoints) {
				stdabw += Math.pow((avgPoints - i), 2);
			}
			stdabw /= p.allPoints.size();
			stdabw = Math.sqrt(stdabw);
			p.stdabw = stdabw;
			//Unsicherheit:
			double unsicherheit = stdabw / Math.sqrt(p.allPoints.size());
			System.out.println("Durchschnittspunkte von " + p.name + ": " + avgPoints + ", Std-Abweichung: " + stdabw + ", Unsicherheit: " + unsicherheit);
	        PrintWriter pWriter = null;
	        try {
	            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("endergebnis.txt", true)));
	            pWriter.println("Durchschnittspunkte von " + p.name + ": " + avgPoints + ", Std-Abweichung: " + stdabw + ", Unsicherheit: " + unsicherheit);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } finally {
	            if (pWriter != null){
	                pWriter.flush();
	                pWriter.close();
	            }
	        }
		}
	}
	
	public static void preGame1() {
		players.clear();
		totalPlayers = 0;
		for (int i = 0; i < würfel.length; i++) würfel[i] = new Würfel(6);
		int[][] paras = Hilfe.ladeDatei("gute_Paras.txt");
		for (int[] is : paras) {
			players.add(new PenaltyPlayer("Pl.:" + is[0] + "," + is[1] + "," + is[2] + "," + is[3] + "," + is[4], is[0], is[1], is[2], is[3], is[4]));
			totalPlayers++;
		}
		totalPenaltyPlayers = totalPlayers;
	}
	
	public static void preGame2() {
		players.clear();
		totalPlayers = 0;
		for (int i = 0; i < würfel.length; i++) würfel[i] = new Würfel(6);
		players.add(new PenaltyPlayer("test", 8, 3, 1, 0, 0));
		totalPlayers = totalPenaltyPlayers = 1;
	}
	
	public static void preGameStandard() {
		players.clear();
		finishedPlayers = 0;
		System.out.println("Mit wie vielen menschlichen Spielern soll gespielt werden (bis zu 6)");
		totalHumanPlayers = Hilfe.intEinlesen(0, 6);
		System.out.println("Mit wie vielen zufälligen Spielern soll gespielt werden (bis zu 100)");
		totalRandPlayers = Hilfe.intEinlesen(0, 100);
		System.out.println("Mit wie vielen Computer-Spielern soll gespielt werden (bis zu 1)");
		totalCompPlayers = Hilfe.intEinlesen(0, 1);
		System.out.println("Mit wie vielen Penalty-Spielern soll gespielt werden (bis zu 1)");
		totalPenaltyPlayers = Hilfe.intEinlesen(0, 1);
		totalPlayers = totalHumanPlayers + totalCompPlayers + totalRandPlayers + totalPenaltyPlayers;
		for (int i = 0; i < würfel.length; i++) würfel[i] = new Würfel(6);
		for (int i = 0; i < totalHumanPlayers; i++) players.add(new HumanPlayer("Human Player Nr. " + (i + 1)));
		for (int i = 0; i < totalRandPlayers; i++) players.add(new RandPlayer("Rand Player Nr. " + (i + 1 + totalHumanPlayers)));
		for (int i = 0; i < totalCompPlayers; i++) players.add(new CompPlayer("Comp Player Nr. " + (i + 1 + totalHumanPlayers + totalRandPlayers)));
		for (int i = 0; i < totalPenaltyPlayers; i++) players.add(new PenaltyPlayer("Penalty Player Nr. " + (i + 1 + totalHumanPlayers + totalRandPlayers + totalCompPlayers)));		
	}
	
	public static void game(){
		finishedPlayers = 0;
		int round = 0;
		while (finishedPlayers != totalPlayers) {
			round++;
//			System.out.println("Runde " + round);
			würfeln();
			for (Player currPlayer : players) {
				if(currPlayer.getSpielStatus()){
					currPlayer.move(würfel);
					if (!currPlayer.getSpielStatus()) {
						finishedPlayers++;
					}
				}
			}
		}
		gameEnd();
	}
	
	public static void gameEnd() {
//		System.out.println("Das Spiel ist vorbei!");
		int max = Integer.MIN_VALUE;
		int sum = 0;
		LinkedList<Player> winner = new LinkedList<>();
		for (Player currPlayer : players) {
			int currPlayerPunkte = currPlayer.getPunkte();
			sum += currPlayerPunkte;
			if(max < currPlayerPunkte){
				max = currPlayerPunkte;
				winner.clear();
				winner.add(currPlayer);
			} else if (max == currPlayerPunkte) {
				winner.add(currPlayer);
			}
//			System.out.println(currPlayer.getName() + " hat " + currPlayerPunkte + " Punkte erreicht. Durfte " + currPlayer.getAnzKeineExZahl() + " mal keine Extra-Zahl eingeben");
		}
//		System.out.println("Es gab " + winner.size() + " Gewinner.");
/*		System.out.print(winner.pop().getName());
		if(winner.size() != 0){
			while (winner.size() > 1) {
				System.out.print(", " + winner.pop().getName());
			}
			System.out.print(" und " + winner.pop().getName());
			System.out.println(" haben mit " + max + " Punkten gewonnen.");					
		} else {
			System.out.println(" hat mit " + max + " Punkten gewonnen.");					
		}*/
//		System.out.println("Durchschnittspunkte: " + (sum / players.size()));
	}
	
	public static void würfeln() {
		for (int i = 0; i < würfel.length; i++) {
			würfel[i].random();
		}
	}
	
    public static int[] getParas(String input) {
		int[] output = new int[5];
		for (int i = 0; i < output.length; i++) {
			output[i] = Character.getNumericValue(input.charAt(2 * i));
		}
		return output;
	}
    
}
