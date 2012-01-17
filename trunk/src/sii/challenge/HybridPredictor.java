package sii.challenge;

import sii.challenge.domain.Movie;
import sii.challenge.domain.User;

public class HybridPredictor implements IPredictor {

	private float userbasedpredictionweight;
	private float itembasedpredictionweight;
	
	private UserBasedPredictor userbp;
	private ItemBasedPredictor itembp;
	
	// farci passare matrice, lista user e lista movie e passarle agli oggetti che creo
	public HybridPredictor() throws Exception
	{
		this.userbp = new UserBasedPredictor();
		this.itembp = new ItemBasedPredictor();
		
		this.userbasedpredictionweight = .5F;
		this.itembasedpredictionweight = .5F;
		
		if(this.userbasedpredictionweight+this.itembasedpredictionweight > 1)
			throw new Exception("Invalid weights.");
		
	}
	
	@Override
	public float PredictRating(User user, Movie movie, long timestamp) {
		
		return this.userbasedpredictionweight * this.userbp.PredictRating(user, movie, timestamp) + 
			   this.itembasedpredictionweight * this.itembp.PredictRating(user, movie, timestamp);
		
	}

}
