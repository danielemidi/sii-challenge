package sii.challenge;

import sii.challenge.preprocessing.MultithreadPreprocessor;
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
		
		/*MAETester maetester = new MAETester();
		maetester.runTest();*/
		
		(new Thread(new MultithreadPreprocessor(0, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(1, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(2, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(3, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(4, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(5, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(6, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(7, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(8, 10, 7000, 50000))).start();
		(new Thread(new MultithreadPreprocessor(9, 10, 7000, 50000))).start();
		
	}

}
