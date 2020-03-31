package de.hesse.martin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hilfe {
	
	public static int intEinlesen (int min, int max){
		int erg = 0;
		boolean falscheEingabe = true;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		System.out.println("Bitte Zahl zwischen " + min + " und " + max + " eingeben:");
		while (falscheEingabe) {
			while (falscheEingabe) {
				try {
					erg = Integer.parseInt(br.readLine());
					falscheEingabe = false;
				} catch (NumberFormatException | IOException e) {
					System.out.println("Eingabe war fehlerhaft. Bitte neu eingeben!");
					falscheEingabe = true;
				}
			}
			if (min <= erg && erg <= max ){
				falscheEingabe = false;
			} else {
				System.out.println("Die eingegebene Zahl ist entweder zu klein oder zu groß\nBitte eine neue Zahl eingeben (zwischen " + min + " und " + max + ")");
				falscheEingabe = true;
			}
		}
		falscheEingabe = true;
		return erg;
	}

	public static void array1ausgeben(int[] input) {
		for (int i = 0; i < input.length; i++) {
			System.out.print(input[i] + " ");
		}
		System.out.println();
	}
	
	public static void clear1Array(int[] input) {
		for (int j = 0; j < input.length; j++) {
			input[j] = 0;
		}
	}
	
	public static void copy1Array(int[] ziel, int[] vorlage) {
		for (int i = 0; i < ziel.length; i++) {
			ziel[i] = vorlage[i];
		}
	}

	public static void swap(int[] input, int i, int k) {
		int tmp = input[k];
		input[k] = input[i];
		input[i] = tmp;
	}

}
