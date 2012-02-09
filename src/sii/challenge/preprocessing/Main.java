package sii.challenge.preprocessing;

import sii.challenge.preprocessing.MultithreadPreprocessor;

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
		System.out.println("Starting Preprocessing...");
		
		(new Thread(new MultithreadPreprocessor(0, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(1, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(2, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(3, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(4, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(5, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(6, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(7, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(8, 10, 5105, 999999999))).start();
		(new Thread(new MultithreadPreprocessor(9, 10, 5105, 999999999))).start();
		
	}

}
