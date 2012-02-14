package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * Predictor che usa i bias dei dati disponibili
 *
 */
public class SimpleBiasPredictor implements IPredictor {

	private final IRepository repository;
	
	private float overallAverageRating = 2.5F;
	
	public SimpleBiasPredictor(IRepository repository)
	{
		this.repository = repository;
		try {
			this.overallAverageRating = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies", new int[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			// deviazione dalla media globale della media dei voti dell'utente
			float useravg = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where userID=?", new Object[]{userid});
			float userbias = useravg>0 ? useravg - this.overallAverageRating : 0;
			
			// deviazione dalla media globale della media dei voti per il film
			float movieavg = this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where movieID=?", new Object[]{movieid});
			float itembias = movieavg>0 ? movieavg - this.overallAverageRating : 0;
			
			return this.overallAverageRating + userbias + itembias;
			
		} catch (Exception e) {
			return 0;
		}
	}

}
