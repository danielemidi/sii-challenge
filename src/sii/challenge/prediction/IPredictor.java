package sii.challenge.prediction;

public interface IPredictor {

	public float PredictRating(int userid, int movieid, long timestamp);
	
}
