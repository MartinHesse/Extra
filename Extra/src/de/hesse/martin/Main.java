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
		final int amountGames = 1000;
		preGame();
		for (int i = 0; i < amountGames; i++){
			long timeStart = System.currentTimeMillis();
			System.out.println("Spiel Nr. " + (i + 1));
			game();
			for (Player pl : players) {
				pl.reset();
			}
			long timeAfterRound = System.currentTimeMillis();
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
			long timeAfterSort = System.currentTimeMillis();
			int count = 0;
			for (Player p : players) {
		        PrintWriter pWriter = null;
		        try {
		            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("ergebnis_" + i + ".txt", true)));
		            pWriter.println("Punkte von " + p.name + ": " + p.averagePoints);
		        } catch (IOException ioe) {
		            ioe.printStackTrace();
		        } finally {
		            if (pWriter != null){
		                pWriter.flush();
		                pWriter.close();
		            }
		        }
		        count++;
		        if (count > 1000) break;
			}
			long timeEnd = System.currentTimeMillis();
			long totalTime = timeEnd - timeStart;
			long roundTime = timeAfterRound - timeStart;
			long sortTime = timeAfterSort - timeAfterRound;
			long writeTime = timeEnd - timeAfterSort;
			System.out.println("Total time elapsed since beginning of this round: " + totalTime + "ms");
			System.out.println("Time elapsed for this round: " + roundTime + "ms");
			System.out.println("Time elapsed for sorting: " + sortTime + "ms");
			System.out.println("Time elapsed for writing: " + writeTime + "ms");
			
			sumTotalTime += totalTime;
			long timeTillEndMsec = sumTotalTime / (i + 1) * (amountGames - i - 1);
			long timeTillEndSec = timeTillEndMsec / 1000;
			long timeTillEndMin = timeTillEndSec / 60;
			System.out.println("Time untill end approx.: " + timeTillEndMin + "min " + (timeTillEndSec - (60 * timeTillEndMin)) + "s");
		}
		for (Player p : players) {
	        PrintWriter pWriter = null;
	        try {
	            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("endergebnis.txt", true)));
	            pWriter.println("Punkte von " + p.name + ": " + p.averagePoints);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } finally {
	            if (pWriter != null){
	                pWriter.flush();
	                pWriter.close();
	            }
	        }
			System.out.println("Punkte von " + p.name + ": " + p.averagePoints);
		}
	}
	
	public static void preGame() {
		players.clear();
		totalPlayers = 0;
		totalPenaltyPlayers = 0;
		final int maxPenalF = 10;
		for (int i = 0; i < würfel.length; i++) würfel[i] = new Würfel(6);
		for (int a = 0; a < maxPenalF; a++) {
			for (int b = 0; b < maxPenalF; b++){
				for (int c = 0; c < maxPenalF; c++) {
					for (int d = 0; d < maxPenalF; d++){
						for (int e = 0; e < maxPenalF; e++){
							players.add(new PenaltyPlayer("Pl.:" + a + ", " + b + ", " + c + ", " + d + ", " + e , a, b, c, d, e));
							totalPlayers++;
						}						
					}
				}				
			}
		}
		totalPenaltyPlayers = totalPlayers;
		System.out.println("Anz. Player: " + totalPlayers);
	}
	
	public static void preGame(int würfelseiten) {
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
		for (int i = 0; i < würfel.length; i++) würfel[i] = new Würfel(würfelseiten);
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
			System.out.println("Runde " + round);
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
		System.out.println("Das Spiel ist vorbei!");
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
		System.out.println("Es gab " + winner.size() + " Gewinner.");
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
		System.out.println("Durchschnittspunkte: " + (sum / players.size()));
	}
	
	public static void würfeln() {
		for (int i = 0; i < würfel.length; i++) {
			würfel[i].random();
		}
	}
}
