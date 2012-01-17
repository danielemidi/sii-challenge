package sii.challenge.domain;

import java.util.Map;

public class User {

	private int id;
	private Map<Integer, Float> movieratings;
	
	private Map<Integer, Integer> directorweights;
	private Map<Integer, Integer> actorweights;
	private Map<Integer, Integer> genreweights;
	private Map<Integer, Integer> countryweights;
	private Map<Integer, Integer> yearweights;
	
	public User(int id) {
		this.id = id;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, Float> getMovieratings() {
		return movieratings;
	}

	public void setMovieratings(Map<Integer, Float> movieratings) {
		this.movieratings = movieratings;
	}

	public Map<Integer, Integer> getDirectorweights() {
		return directorweights;
	}

	public void setDirectorweights(Map<Integer, Integer> directorweights) {
		this.directorweights = directorweights;
	}

	public Map<Integer, Integer> getActorweights() {
		return actorweights;
	}

	public void setActorweights(Map<Integer, Integer> actorweights) {
		this.actorweights = actorweights;
	}

	public Map<Integer, Integer> getGenreweights() {
		return genreweights;
	}

	public void setGenreweights(Map<Integer, Integer> genreweights) {
		this.genreweights = genreweights;
	}

	public Map<Integer, Integer> getCountryweights() {
		return countryweights;
	}

	public void setCountryweights(Map<Integer, Integer> countryweights) {
		this.countryweights = countryweights;
	}

	public Map<Integer, Integer> getYearweights() {
		return yearweights;
	}

	public void setYearweights(Map<Integer, Integer> yearweights) {
		this.yearweights = yearweights;
	}
	
	
	
	
}
