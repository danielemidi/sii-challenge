package sii.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sii.challenge.domain.MovieRating;
import sii.challenge.repository.Repository;
import sii.challenge.util.IOFile;


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

	public static void main(String[] args) throws Exception {
		
		String inputfilename = args[1];
		String outputfilename = args[2];
		
		List<MovieRating> inputList = IOFile.leggiRigaSplit(inputfilename);
		/*Repository repository = new Repository();
		Recommender recommender = new Recommender(repository);		
		List<MovieRating> predictions = recommender.recommend(inputList);*/
		
		IOFile.truncateOutputFile(outputfilename);
		
		int taskcount = Runtime.getRuntime().availableProcessors();
		int inputlistsize = inputList.size();
		List<Future<List<MovieRating>>> tasks = new ArrayList<Future<List<MovieRating>>>(taskcount);
		ExecutorService pool = Executors.newFixedThreadPool(taskcount);
		for(int i = 0; i < taskcount; i++)
			tasks.add(pool.submit(new Recommender(new Repository(), inputList.subList(inputlistsize/taskcount*i, inputlistsize/taskcount*(i+1)))));
				
		for(Future<List<MovieRating>> t : tasks) 
			IOFile.appendToOutputFile(t.get(), outputfilename);
		
		pool.shutdown();

		System.out.println("Grazie per aver usato il nostro Sistema di Raccomandazione!\nA presto.");
	}

}
