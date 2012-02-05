package sii.challenge;

import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.*;
import sii.challenge.prediction.*;
import sii.challenge.repository.IRepository;

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
	private IPredictor fallbackpredictor;
	
	public Recommender(IRepository repository)
	{
		System.out.println("R - Creating Predictor(s)...");
		//this.predictor = new DumbPredictor();
		this.predictor = new ItemBasedPredictor(repository);
		this.fallbackpredictor = new DumbUserPredictor(repository);
	}
	
	public List<MovieRating> recommend(List<MovieRating> input)
	{
		int i = 1;
		int c = input.size();
		System.out.println("R - Recommending...");
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		for(MovieRating mr : input)
		{
			float exp =  mr.getRating();
			
			float p = this.predictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
			this.printStats(i, c, this.predictor, exp, p);

			float f = this.fallbackpredictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
			this.printStats(i, c, this.fallbackpredictor, exp, f);
			
			if(p==0) p = f;
			
			/*if(p==0) {
				System.err.print("*");
				p = f;
			} else {
				System.out.print(".");
			}
			if(i % 150 == 0) System.out.println();*/
			
			MovieRating pmr = new MovieRating(
				mr.getUserId(), 
				mr.getMovieId(), 
				mr.getTimestamp(), 
				.5F*Math.round(p/.5)
			);
			ratings.add(pmr);
			
			/*System.out.println("\t" + i + "/" + c + "; " +
							   "Predictor: "+this.predictor.getClass().getName() + "; " +
							   "Expected: " + mr.getRating() + "; " +
							   "Actual: " + pmr.getRating() + "; " +
							   "Error: " + Math.abs(mr.getRating()-pmr.getRating()) + ". ");*/
			i++;
		}
		return ratings;
	}
	
	private void printStats(int i, int c, IPredictor p, float exp, float act)
	{
		System.out.println("\t" + i + "/" + c + "; " +
						   "Predictor: "+p.getClass().getName() + "; " +
						   "Expected: " + exp + "; " +
						   "Actual: " + act + "; " +
						   "Error: " + Math.abs(exp-act) + ". ");
	}
	
}
