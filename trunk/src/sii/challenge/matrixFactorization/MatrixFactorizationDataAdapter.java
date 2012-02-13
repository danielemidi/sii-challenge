package sii.challenge.matrixFactorization;

import java.util.*;

import Jama.Matrix;

import sii.challenge.domain.MovieRating;
import sii.challenge.repository.IRepository;

/**
 * Prende i rating dal DB e crea una matrice UxI->R utile alla MatrixFactorization
 *
 */
public class MatrixFactorizationDataAdapter {
	
	public Matrix readAndAdapt(IRepository repository) throws Exception {
		int usercount = (int)repository.getSingleFloatValue("SELECT COUNT(DISTINCT userID) FROM user_ratedmovies", new int[]{});
		int moviecount = (int)repository.getSingleFloatValue("SELECT COUNT(*) FROM movies", new int[]{});
		
		List<MovieRating> ratings = repository.getMovieRatingList("SELECT * from user_ratedmovies", new int[]{});
		
		Map<Integer, Integer> movie2i = new HashMap<Integer, Integer>();
		Map<Integer, Integer> user2j = new HashMap<Integer, Integer>();
		int m = 0;
		int u = 0;
		
		Matrix matrix = new Matrix(moviecount, usercount);
		
		int mi, ui;
		for(MovieRating rating : ratings) {
			if(movie2i.containsKey(rating.getMovieId()))
				mi = movie2i.get(rating.getMovieId());
			else{
				mi = m;
				movie2i.put(rating.getMovieId(), mi);
				m++;
			}
			if(user2j.containsKey(rating.getUserId()))
				ui = user2j.get(rating.getUserId());
			else{
				ui = u;
				user2j.put(rating.getUserId(), ui);
				u++;
			}
			
			matrix.set(mi, ui, rating.getRating());
		}
		
		return matrix;
	}
	
}
