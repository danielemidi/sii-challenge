package sii.challenge;

import sii.challenge.testing.MAETester;

/**
 * 
 * - riceve in input il file di Test e il nome del file di Output
 * - parsa il file di Test
 * - ottiene i dati dal DB
 * - crea Recommender passandogli il repository da cui prendere i dati del training set
 * - lancia il Recommender sul test set e attende le predizioni di risultato
 * - scrive le predizioni nel file di Output
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Starting Main...");
		
		MAETester maetester = new MAETester();
		maetester.runTest();
		
	}

}
