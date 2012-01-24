package sii.challenge.repository;

import java.util.List;

public interface UserDAO {

	public List<String> doRetriveFavoriteActors();
	public List<String> doRetriveFavoriteDirectors();
	public List<String> doRetriveFavoriteGenres();
	public List<String> doRetriveFavoriteCountries();
	public List<String> doRetriveFavoriteYears();
	
}
