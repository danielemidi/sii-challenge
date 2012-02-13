package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * Predictor che fa la media dei voti dell'utente UserID per tutti i movie che ha votato
 * @author Daniele
 *
 */
public class MatrixFactorizationPredictor implements IPredictor {

	private final IRepository repository;
	
	public MatrixFactorizationPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			return this.repository.getSingleFloatValue("select rating from predictionmatrix where userID=? and movieID=?", new int[]{userid, movieid});
		} catch (Exception e) {
			return 0;
		}
	}

}
