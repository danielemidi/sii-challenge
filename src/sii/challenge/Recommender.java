package sii.challenge;

import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.*;
import sii.challenge.prediction.*;

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
	
	public Recommender(TrainingDataset recommendationdataset)
	{
		//this.predictor = new HybridPredictor(recommendationdataset);
		this.predictor = new DumbPredictor();
	}
	
	public List<MovieRating> recommend(List<MovieRating> input)
	{
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
		}
		return ratings;
	}
	
}
