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
		//this.predictor = new ItemBasedPredictor(repository);
		this.predictor = new SimpleTimeDependentBiasPredictor(repository);
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
		float p1;
		float f;
		boolean p1missing;
		
		int pc = 0; int fc = 0;
		
		for(MovieRating mr : input)
		{
			exp =  mr.getRating();
			
			p1 = this.predictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
			p1missing = p1==0;

			f = this.fallbackpredictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());

			if(!p1missing) p = (p1+f)/2; // calcola la predizione come media aritmetica di itembased e dumbuser
			else 	       p = f;
			
			p1 = .5F*Math.round(p1/.5);
			//this.printStats(i, c, this.predictor, exp, p1);
			f = .5F*Math.round(f/.5);
			//this.printStats(i, c, this.fallbackpredictor, exp, f);
			
			float perr = Math.abs(exp-p1);
			float ferr = Math.abs(exp-f);
			if(perr<ferr){
				System.out.println(i + "/" + c + ": \tP:"+perr+" *\t\tF:"+ferr + (p1missing?"\t\t!":""));
				pc++;
			}else{
				System.out.println(i + "/" + c + ": \tP:"+perr+"  \t\tF:"+ferr + " *" + (p1missing?"\t\t!":""));
				fc++;
			}
			
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
