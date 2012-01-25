package sii.challenge.prediction;

public class DumbPredictor implements IPredictor {

	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		return 2.5F;
	}

}
