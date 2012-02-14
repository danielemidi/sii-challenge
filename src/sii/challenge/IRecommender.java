package sii.challenge;

import java.util.List;

import sii.challenge.domain.MovieRating;

public interface IRecommender {
	List<MovieRating> recommend(List<MovieRating> input) throws Exception;
}
