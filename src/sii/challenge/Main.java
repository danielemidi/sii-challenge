package sii.challenge;

import sii.challenge.preprocessing.MultithreadPreprocessor;
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
		
		/*(new Thread(new MultithreadPreprocessor(0, 4))).start();
		(new Thread(new MultithreadPreprocessor(1, 4))).start();
		(new Thread(new MultithreadPreprocessor(2, 4))).start();
		(new Thread(new MultithreadPreprocessor(3, 4))).start();*/
		
		Preprocessor preprocessor = new Preprocessor();
		try {
			preprocessor.preprocess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
