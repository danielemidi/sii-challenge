package sii.challenge;

import java.util.*;
import java.util.concurrent.*;

import sii.challenge.domain.*;
import sii.challenge.repository.*;

/**
 * Permette di parallelizzare il recommender in modo da aumentare le prestazioni in termini di tempi di risposta
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class ParallelRecommender implements IRecommender {
	
	/**
	 * Effettua la raccomandazione dei film si veda il metodo recommend della classe Recommender
	 * @param input: lista di MovieRating di cui si vuole avere la predizione
	 * @return la lista in inpunt in cui l'attributo rating degli oggetti MovieRating è stato aggiornato
	 * @throws Exception
	 */
	public List<MovieRating> recommend(List<MovieRating> input) throws Exception
	{
		List<MovieRating> output = new ArrayList<MovieRating>();
		
		int taskcount = Runtime.getRuntime().availableProcessors();
		int inputlistsize = input.size();
		List<Future<List<MovieRating>>> tasks = new ArrayList<Future<List<MovieRating>>>(taskcount);
		ExecutorService pool = Executors.newFixedThreadPool(taskcount);
		for(int i = 0; i < taskcount; i++)
			tasks.add(pool.submit(new Recommender(new Repository(), input.subList(inputlistsize/taskcount*i, inputlistsize/taskcount*(i+1)))));
				
		for(Future<List<MovieRating>> t : tasks) 
			output.addAll(t.get());
		
		pool.shutdown();
		
		return output;
	}
	
	
}
