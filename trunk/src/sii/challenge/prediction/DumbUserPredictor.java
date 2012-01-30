package sii.challenge.prediction;

import sii.challenge.repository.Repository;

public class DumbUserPredictor implements IPredictor {

	private final Repository repository;
	
	public DumbUserPredictor(Repository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		// fa la media dei voti dell'utente UserID per tutti i movie che ha votato
		try {
			float avgrating = this.repository.getAverageUserRating(userid);
			return avgrating;
		} catch (Exception e) {
			return 0;
		}
	}

}
