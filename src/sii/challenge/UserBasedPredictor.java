package sii.challenge;

import java.util.List;

import sii.challenge.domain.Movie;
import sii.challenge.domain.MovieRating;
import sii.challenge.domain.User;

public class UserBasedPredictor implements IPredictor {

	public UserBasedPredictor(MovieRating[][] movieratingmatrix, List<User> users, List<Movie> movies)
	{
		
	}
	
	@Override
	public float PredictRating(User user, Movie movie, long timestamp) {
		// TODO Auto-generated method stub
		return 0;
	}

}
