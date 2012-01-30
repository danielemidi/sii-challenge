package sii.challenge;

import sii.challenge.preprocessing.Preprocessor;
import sii.challenge.testing.MAETester;

/**
 * 
 * - riceve in input il file di Test e il nome del file di Output
 * - parsa il file di Test
 * - ottiene i dati dal DB
 * - crea Recommender passandogli i dati del DB come Training set e i dati del file di Test come Test set
 * - lancia il Recommender e attende le predizioni di risultato
 * - scrive le predizioni nel file di Output
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Starting Main...");
		
		//MAETester maetester = new MAETester();
		//maetester.runTest();
		Preprocessor preprocessor = new Preprocessor();
		try {
			preprocessor.preprocess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
