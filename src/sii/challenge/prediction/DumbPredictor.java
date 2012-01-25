package sii.challenge.prediction;

import sii.challenge.domain.Movie;
import sii.challenge.domain.User;

public class DumbPredictor implements IPredictor {

	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		return 2.5F;
	}

}
