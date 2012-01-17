package sii.challenge;

import sii.challenge.domain.*;

public interface IPredictor {

	public float PredictRating(User user, Movie movie, long timestamp);
	
}
