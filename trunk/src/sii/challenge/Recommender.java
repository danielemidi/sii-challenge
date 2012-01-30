package sii.challenge;

import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.*;
import sii.challenge.prediction.*;
import sii.challenge.repository.Repository;

/**
 * 
 * - riceve in input Training set e Test set
 * - crea le strutture dati per il Predictor
 * - crea l'IPredictor (scelto magari da file di Configurazione)
 * - per ogni tupla del Test set:
 *   - lancia l'IPredictor e attende i risultati
 * - restituisce i risultati
 *
 */
public class Recommender {

	private IPredictor predictor;
	
	public Recommender(Repository repository)
	{
		System.out.println("R - Creating Predictor(s)...");
		//this.predictor = new DumbPredictor();
		this.predictor = new DumbUserPredictor(repository);
	}
	
	public List<MovieRating> recommend(List<MovieRating> input)
	{
		int i = 1;
		int c = input.size();
		System.out.println("R - Recommending...");
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		for(MovieRating mr : input)
		{
			MovieRating pmr = new MovieRating(
					mr.getUserId(), 
					mr.getMovieId(), 
					mr.getTimestamp(), 
					this.predictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp())
			);
			ratings.add(pmr);
			if (mr.getRating() > 0) {
				System.out.println("\t" + i + "/" + c + "; " +
								   "Predictor: "+this.predictor.getClass().getName() + "; " +
								   "Expected: " + mr.getRating() + "; " +
								   "Actual: " + pmr.getRating() + "; " +
								   "Error: " + Math.abs(mr.getRating()-pmr.getRating()) + ". ");
			}
			i++;
		}
		return ratings;
	}
	
}
