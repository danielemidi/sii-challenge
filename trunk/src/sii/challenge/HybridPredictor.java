package sii.challenge;

import java.util.List;

import sii.challenge.domain.Movie;
import sii.challenge.domain.User;

public class HybridPredictor implements IPredictor {

	private float userbasedpredictionweight;
	private float itembasedpredictionweight;
	
	private UserBasedPredictor userbp;
	private ItemBasedPredictor itembp;
	
	// farci passare matrice, lista user e lista movie e passarle agli oggetti che creo
	public HybridPredictor(MovieRating[][] movieratingmatrix, List<User> users, List<Movie> movies) throws Exception
	{
		this.userbp = new UserBasedPredictor(movieratingmatrix, users, movies);
		this.itembp = new ItemBasedPredictor(movieratingmatrix, users, movies);
		
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
