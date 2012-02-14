package sii.challenge;

import java.util.*;
import java.util.concurrent.*;

import sii.challenge.domain.*;
import sii.challenge.repository.*;

public class ParallelRecommender implements IRecommender {
	
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
