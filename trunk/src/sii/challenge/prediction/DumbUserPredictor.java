package sii.challenge.prediction;

import sii.challenge.repository.KSetRepository;

/**
 * Predictor che fa la media dei voti dell'utente UserID per tutti i movie che ha votato
 * @author Daniele
 *
 */
public class DumbUserPredictor implements IPredictor {

	private final KSetRepository repository;
	
	public DumbUserPredictor(KSetRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			return this.repository.getSingleFloatValue("select avg(rating) from sii_challenge.user_ratedmovies where userID=?", new int[]{userid});
		} catch (Exception e) {
			return 0;
		}
	}

}
