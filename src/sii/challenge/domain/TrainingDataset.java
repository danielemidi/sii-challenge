package sii.challenge.domain;

import java.util.ArrayList;
import java.util.List;

public class TrainingDataset {

	private List<User> users;
	private List<Movie> movies;
	MovieRating[][] movieratingmatrix;
	
	
	public TrainingDataset()
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
