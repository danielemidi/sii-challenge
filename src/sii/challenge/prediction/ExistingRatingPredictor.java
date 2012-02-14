package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * Predictor che fa la media dei voti dell'utente UserID per tutti i movie che ha votato
 * @author Daniele
 *
 */
public class ExistingRatingPredictor implements IPredictor {

	private final IRepository repository;
	
	public ExistingRatingPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			return this.repository.getSingleFloatValue("select rating from user_ratedmovies where userID=? and movieID=? and timestamp=?", new Object[]{userid, movieid, timestamp});
		} catch (Exception e) {
			return 0;
		}
	}

}
