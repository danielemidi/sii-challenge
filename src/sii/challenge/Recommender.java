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
		
		float exp;
		float p;
		float f;
		boolean pmissing;
		
		int pc = 0; int fc = 0;
		
		for(MovieRating mr : input)
		{
			exp =  mr.getRating();
			
			p = this.predictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
			pmissing = p==0;
			p = .5F*Math.round(p/.5);
			//this.printStats(i, c, this.predictor, exp, p);

			f = this.fallbackpredictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
			f = .5F*Math.round(f/.5);
			//this.printStats(i, c, this.fallbackpredictor, exp, f);
			
			float perr = Math.abs(exp-p);
			float ferr = Math.abs(exp-f);
			if(perr<ferr){
				System.out.println("\tP:"+perr+" *\t\tF:"+ferr + (pmissing?"\t\t!":""));
				pc++;
			}else{
				System.out.println("\tP:"+perr+"  \t\tF:"+ferr + " *" + (pmissing?"\t\t!":""));
				fc++;
			}
			
			if(pmissing) p = f;
			
			MovieRating pmr = new MovieRating(
				mr.getUserId(), 
				mr.getMovieId(), 
				mr.getTimestamp(), 
				.5F*Math.round(p/.5)
			);
			ratings.add(pmr);
			
			i++;
		}
		
		System.out.println("\tPc = " + pc + "; Fc = " + fc);
		
		return ratings;
	}
	
	private void printStats(int i, int c, IPredictor p, float exp, float act)
	{
		System.out.println("\t" + i + "/" + c + "; " +
						   "Predictor: " + p.getClass().getName() + "; " +
						   "Expected: " + exp + "; " +
						   "Actual: " + act + "; " +
						   "Error: " + Math.abs(exp-act) + ". ");
	}
	
}
