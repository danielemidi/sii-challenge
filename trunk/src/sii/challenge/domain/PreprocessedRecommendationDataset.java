package sii.challenge.domain;

import java.util.ArrayList;
import java.util.List;

public class PreprocessedRecommendationDataset {

	private List<User> users;
	private List<Movie> movies;
	
	
	public PreprocessedRecommendationDataset()
	{
		this.users = new ArrayList<User>();
		this.movies = new ArrayList<Movie>();
	}
	
	
	public List<User> getUsers() {
		return users;
	}
	public List<Movie> getMovies() {
		return movies;
	}
	
}
