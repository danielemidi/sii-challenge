package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * La logica adottata � la stessa di quella osservata in SimpleBiasPredictor con la differenza che sono presi in considerazione anche i timestamp
 * @author Daniele Midi, Antonio Tedeschi
 * 
 */
public class SimpleTimeDependentBiasPredictor implements IPredictor {

	private final IRepository repository;
	
	private float overallAverageRating = 2.5F;
	
	/**
	 * Costruttore
	 * @param repository
	 */
	public SimpleTimeDependentBiasPredictor(IRepository repository)
	{
		this.repository = repository;
		try {
			this.overallAverageRating = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies", new int[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Si faccia riferimento alla descrizione della classe e alla descrizione del omonimo metodo in IPredictor
	 * @param userid
	 * @param movieid
	 * @param timestamp
	 * @return
	 */
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			// deviazione dalla media globale della media dei voti dell'utente
			float timeDependentOverallAverageRating = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where timestamp<=?", new Object[]{timestamp});
			if(timeDependentOverallAverageRating<=0) timeDependentOverallAverageRating = this.overallAverageRating;
			
			// deviazione dalla media globale della media dei voti dell'utente
			float userbias = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where userID=? and timestamp<=?", new Object[]{userid, timestamp}) - timeDependentOverallAverageRating;
			
			// deviazione dalla media globale della media dei voti per il film
			float itembias = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where movieID=? and timestamp<=?", new Object[]{movieid, timestamp}) - timeDependentOverallAverageRating;
			
			return this.overallAverageRating + userbias + itembias;
		} catch (Exception e) {
			return 0;
		}
	}

}
