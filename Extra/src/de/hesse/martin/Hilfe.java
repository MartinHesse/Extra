package de.hesse.martin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;

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
	
	public static void setAusgeben(Set<ParameterTupel> in) {
		for (ParameterTupel is : in) {
			array1ausgeben(is.tupel);
		}
	}

	public static void array1ausgeben(int[] input) {
		for (int i = 0; i < input.length; i++) {
			System.out.print(input[i] + " ");
		}
		System.out.println();
	}
	
	public static void array2ausgeben(int[][] in) {
		for (int[] is : in) {
			for (int i : is) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
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
	
	public static int hamming(int in1, int in2) {
		return Math.abs(in1 - in2);
	}
	
	public static int hamming(int[] in1, int[] in2) {
		int out = 0;
		for (int i = 0; i < in1.length; i++) {
			out += hamming(in1[i], in2[i]);
		}
		return out;
	}
	
	public static void write(int[] in, String datName) {
        PrintWriter pWriter = null;
        try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter(datName, true)));
            for (int i : in) {
                pWriter.print(i + " ");				
			}
            pWriter.println();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null){
                pWriter.flush();
                pWriter.close();
            }
        }
	}
	
	public static void setWrite(Set<ParameterTupel> in, String datName) {
		for (ParameterTupel is : in) {
			write(is.tupel, datName);
		}
	}
	
    public static int[][] ladeDatei(String datName) {
    	File file = new File(datName);
    	if (!file.canRead() || !file.isFile()) System.exit(0);
    	BufferedReader in = null;
		int[][] out = null;
    	try {
    		in = new BufferedReader(new FileReader(datName));
        	int lines = 0;
        	try {
    			while (in.readLine() != null) lines++;
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
        	out = new int[lines - 1][5];
        	in.close();
    		in = new BufferedReader(new FileReader(datName));
    		for (int i = 0; i < out.length; i++) {
				out[i] = Main.getParas(in.readLine());
			}
    	} catch (IOException e){
    		e.printStackTrace();
    	} finally {
			if (in != null){
				try{
					in.close();
				} catch (IOException e){}
			}
		}
    	return out;
    }
    
}
