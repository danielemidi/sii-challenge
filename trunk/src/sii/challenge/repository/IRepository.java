package sii.challenge.repository;

import java.sql.Connection;
import java.util.List;

import sii.challenge.domain.MovieRating;

public interface IRepository {

	float getSingleFloatValue(String query, int[] args) throws Exception;
	float getSingleFloatValue(String query, int[] args, Connection connection) throws Exception;
	
	float getSingleFloatValue(String query, Object[] args) throws Exception;
	float getSingleFloatValue(String query, Object[] args, Connection connection) throws Exception;

	List<MovieRating> getMovieRatingList(String query, int[] args) throws Exception;
	
}
