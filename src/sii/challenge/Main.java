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
 * Lancia l'esecuzione del sistema
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class Main {
	
	/**
	 * Legge i path dei file da cui si desidera leggere e in cui si vuole scrivere i rating che sono stati predetti
	 * e lancia l'esecuzione di ParalleRecommender
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		String inputfilename = args[1];
		String outputfilename = args[2];
		
		List<MovieRating> inputList = IOFile.leggiRigaSplit(inputfilename);
		
		IOFile.truncateOutputFile(outputfilename);
		
		/*int taskcount = Runtime.getRuntime().availableProcessors();
		int inputlistsize = inputList.size();
		List<Future<List<MovieRating>>> tasks = new ArrayList<Future<List<MovieRating>>>(taskcount);
		ExecutorService pool = Executors.newFixedThreadPool(taskcount);
		for(int i = 0; i < taskcount; i++)
			tasks.add(pool.submit(new Recommender(new Repository(), inputList.subList(inputlistsize/taskcount*i, inputlistsize/taskcount*(i+1)))));
				
		for(Future<List<MovieRating>> t : tasks) 
			IOFile.appendToOutputFile(t.get(), outputfilename);
			
		pool.shutdown();*/
		
		List<MovieRating> predictions = new ParallelRecommender().recommend(inputList);
		IOFile.appendToOutputFile(predictions, outputfilename);

		System.out.println("Grazie per aver usato il nostro Sistema di Raccomandazione!\nA presto.");
	}

}
