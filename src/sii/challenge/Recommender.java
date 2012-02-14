package sii.challenge;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

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
public class Recommender implements Callable<List<MovieRating>>, IRecommender {

	private List<MovieRating> inputlist;
	
	private IPredictor[] predictors;
	private float[] predictions;
	private float[] predictorErrors;
	private float[] predictorMAEs;
	
	public Recommender(IRepository repository, List<MovieRating> input) {
		this(repository);
		
		this.inputlist = input;
	}
	
	public Recommender(IRepository repository)
	{
		System.out.println("R - Creating Predictor(s)...");
		this.predictors = new IPredictor[]{
			new ExistingRatingPredictor(repository)
			,new MatrixFactorizationPredictor(repository)
			,new SimpleBiasPredictor(repository)
			//,new DumbUserPredictor(repository)
			//,new ItemTagBasedPredictor(repository)
			//,new ItemGenreBasedPredictor(repository)
			//,new SimpleTimeDependentBiasPredictor(repository)
		};
		this.predictions = new float[this.predictors.length];
		this.predictorErrors = new float[this.predictors.length];
		this.predictorMAEs = new float[this.predictors.length];
	}
	
	public List<MovieRating> recommend(List<MovieRating> input) throws Exception
	{
		int i = 1;
		int c = input.size();
		System.out.println("R - Recommending...");
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		
		float exp;
		float p;
		float roundedpred;
		float err;
		float totalerror = 0;
		
		for(int pi = 0; pi<this.predictors.length; pi++)
			System.out.print(this.predictors[pi].getClass().getSimpleName() + "\t");
		System.out.println();
		
		for(MovieRating mr : input)
		{
			exp =  mr.getRating();
			p = 0;
			
			System.out.printf("%5s/%s:\t",i,c);
			for(int pi = 0; pi<this.predictors.length; pi++)
			{
				this.predictions[pi] = this.predictors[pi].PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
				roundedpred = .5F*Math.round(this.predictions[pi]/.5);
				this.predictorErrors[pi] += Math.abs(exp-roundedpred);
				this.predictorMAEs[pi] = this.predictorErrors[pi]/i;
				System.out.printf("%1.1f|%5.1f|%1.8f\t", Math.abs(exp-roundedpred), this.predictorErrors[pi], this.predictorMAEs[pi]);
				p += this.predictions[pi];
			}
			
			// predizione totale
			p = this.predictions[0] > 0 ? this.predictions[0] : ( 
					this.predictions[1] > 0 ? this.predictions[1] : ( 
						this.predictions[2]
					)
				);
			p = .5F*Math.round(p/.5);

			err = Math.abs(exp-p);
			totalerror += err;
			
			System.out.println("]=> P: " + p + "\tERR: " + err + "\tMAE: " + totalerror/i);
			
			if(p<0) p = .5F;
			
			MovieRating pmr = new MovieRating(
				mr.getUserId(), 
				mr.getMovieId(), 
				mr.getTimestamp(), 
				.5F*Math.round(p/.5)
			);
			ratings.add(pmr);
			
			i++;
		}

		System.out.println("Total MAE:");
		for(int pi = 0; pi<this.predictors.length; pi++)
		{
			this.predictorMAEs[pi] = this.predictorErrors[pi]/c;
			System.out.printf("\t\t%s : %1.8f MAE\n", this.predictors[pi].getClass().getSimpleName(), this.predictorMAEs[pi]);
		}
		
		System.out.println();
		
		return ratings;
	}

	@Override
	public List<MovieRating> call() throws Exception {
		if(this.inputlist == null) throw new Exception("Input list not initialized.");
		
		return this.recommend(this.inputlist);
	}
	
}
