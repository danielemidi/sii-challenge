package sii.challenge;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import sii.challenge.domain.*;
import sii.challenge.prediction.*;
import sii.challenge.repository.IRepository;

/**
 * Implementa l'oggetto principale che effettua le predizioni.
 * Vengono creati una serie di predittori, usati in cascata con tecniche di fallback qualora il precedente non trovi predizioni attendibili.
 * 
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class Recommender implements Callable<List<MovieRating>>, IRecommender {

	private List<MovieRating> inputlist;
	
	private IPredictor[] predictors;
	
	/**
	 * Costruttore
	 * @param repository
	 * @param input
	 */
	public Recommender(IRepository repository, List<MovieRating> input) {
		this(repository);
		
		this.inputlist = input;
	}
	
	/**
	 * Inizializza il recommendere con una lista di predittori da utilizzare per l'applicazione.
	 * I predittori adottati sono:ExistingRatingPredictor, MatrixFactorizationPredictor, SimpleBiasPredictor
	 * @param repository
	 */
	public Recommender(IRepository repository)
	{
		System.out.println("R - Creating Predictor(s)...");
		this.predictors = new IPredictor[]{
			new ExistingRatingPredictor(repository),
			new MatrixFactorizationPredictor(repository),
			new SimpleTimeDependentBiasPredictor(repository)
		};
	}
	
	/**
	 * Effettua la predizione adottando i predittori che sono stati assegnati al recommender, effettuando un arrotondamento di questi valori
	 * per rapportarsi alla scala di voti da 0.5 a 5 con passo di 0.5.
	 * @param input: lista di movie rating che devono essere raccomandati e di cui si vuole conoresce il rating
	 * @return una lista di oggetti MovieRating contentente i rating relative alle tuple passate in input
	 * @throws Exception
	 */
	public List<MovieRating> recommend(List<MovieRating> input) throws Exception
	{
		float p = 0;
		
		System.out.println("R - Recommending...");
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		
		for(MovieRating mr : input)
		{
			for(IPredictor predictor : this.predictors)
			{
				p = predictor.PredictRating(mr.getUserId(), mr.getMovieId(), mr.getTimestamp());
				if(p>0) break;
			}
			
			// predizione finale
			p = .5F*Math.round(p/.5);
			
			if(p<0) p = 3F;
			
			MovieRating pmr = new MovieRating(
				mr.getUserId(), 
				mr.getMovieId(), 
				mr.getTimestamp(), 
				p
			);
			ratings.add(pmr);
		}
		
		return ratings;
	}

	@Override
	public List<MovieRating> call() throws Exception {
		if(this.inputlist == null) throw new Exception("Input list not initialized.");
		
		return this.recommend(this.inputlist);
	}
	
}
