package sii.challenge;

import sii.challenge.domain.Movie;
import sii.challenge.domain.User;

public class DumbPredictor implements IPredictor {

	@Override
	public float PredictRating(User user, Movie movie, long timestamp) {
		return 2.5F;
	}

}
