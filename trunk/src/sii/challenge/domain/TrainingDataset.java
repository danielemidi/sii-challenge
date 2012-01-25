package sii.challenge.domain;

import java.util.ArrayList;
import java.util.List;

public class TrainingDataset {

	private List<User> users;
	private List<Movie> movies;
	private RatingMatrix movieratingmatrix;
	
	
	public TrainingDataset()
	{
		this.users = new ArrayList<User>();
		this.movies = new ArrayList<Movie>();
	}


	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}


	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}


	public RatingMatrix getMovieratingmatrix() {
		return movieratingmatrix;
	}
	public void setMovieratingmatrix(RatingMatrix movieratingmatrix) {
		this.movieratingmatrix = movieratingmatrix;
	}
	
}
