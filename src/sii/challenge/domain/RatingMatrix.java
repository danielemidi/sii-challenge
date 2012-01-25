package sii.challenge.domain;

import java.util.List;

/**
 * Matrice Movie x User con rating e timestamp
 * @author Daniele
 *
 */
public class RatingMatrix {

	long[][] timestampmatrix;
	float[][] ratingmatrix;
	
	public RatingMatrix(int moviecount, int usercount, List<MovieRating> ratings)
	{
		timestampmatrix = new long[moviecount][usercount];
		ratingmatrix = new float[moviecount][usercount];
		
		for(MovieRating r : ratings) {
			timestampmatrix[r.getMovieId()][r.getUserId()] = r.getTimestamp();
			ratingmatrix[r.getMovieId()][r.getUserId()] = r.getRating();
		}
	}
	
	public long getTimestamp(int movieid, int userid) {
		return timestampmatrix[movieid][userid];
	}
	
	public float getRating(int movieid, int userid) {
		return ratingmatrix[movieid][userid];
	}
	
}
