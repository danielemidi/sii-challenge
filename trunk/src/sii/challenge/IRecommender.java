package sii.challenge;

import java.util.List;

import sii.challenge.domain.MovieRating;

/**
 * Interfaccia che deve essere implementata da tutti i recommender che si decide di implementare
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public interface IRecommender {
	List<MovieRating> recommend(List<MovieRating> input) throws Exception;
}
