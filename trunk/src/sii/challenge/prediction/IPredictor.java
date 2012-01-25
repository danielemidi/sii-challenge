package sii.challenge.prediction;

import sii.challenge.domain.*;

public interface IPredictor {

	public float PredictRating(int userid, int movieid, long timestamp);
	
}
