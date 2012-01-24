package sii.challenge.repository;

import java.util.List;

public interface UserDAO {

	public List<String> doRetriveFavoriteActors() throws Exception;
	public List<String> doRetriveFavoriteDirectors() throws Exception;
	public List<String> doRetriveFavoriteGenres() throws Exception;
	public List<String> doRetriveFavoriteCountries() throws Exception;
	public List<String> doRetriveFavoriteYears() throws Exception;
	
}
