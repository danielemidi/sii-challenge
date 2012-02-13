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
	
	Map<Integer, Integer> movie2j = new HashMap<Integer, Integer>();
	Map<Integer, Integer> user2i = new HashMap<Integer, Integer>();
	Map<Integer, Integer> j2movie = new HashMap<Integer, Integer>();
	Map<Integer, Integer> i2user = new HashMap<Integer, Integer>();
	
	IRepository repository; 
	
	public MatrixFactorizationDataAdapter(IRepository repository){
		this.repository=repository;
	}
	
	public Matrix readAndAdapt() throws Exception {
		int usercount = (int)repository.getSingleFloatValue("SELECT COUNT(DISTINCT userID) FROM user_ratedmovies", new int[]{});
		//int moviecount = (int)repository.getSingleFloatValue("SELECT COUNT(DISTINCT movieID) FROM user_ratedmovies", new int[]{});
		int moviecount = (int)repository.getSingleFloatValue("SELECT COUNT(*) FROM movies", new int[]{});
		
		List<MovieRating> ratings = repository.getMovieRatingList("SELECT * from user_ratedmovies", new int[]{});

		int u = 0;
		int m = 0;
		
		Matrix matrix = new Matrix(usercount, moviecount);
		
		int mi, ui;
		for(MovieRating rating : ratings) {
			
			if(user2i.containsKey(rating.getUserId()))
				ui = user2i.get(rating.getUserId());
			else{
				ui = u;
				user2i.put(rating.getUserId(), ui);
				i2user.put(ui, rating.getUserId());
				u++;
			}
			if(movie2j.containsKey(rating.getMovieId()))
				mi = movie2j.get(rating.getMovieId());
			else{
				mi = m;
				movie2j.put(rating.getMovieId(), mi);
				j2movie.put(mi, rating.getMovieId());
				m++;
			}
			
			matrix.set(ui, mi, rating.getRating());
		}
		
		return matrix;
	}
	
	
	public void adaptAndWrite(Matrix matrix, int offseti, int offsetj) throws Exception {
		String query = "INSERT INTO predictionmatrix (userID, movieID, rating) VALUES (?,?,?)";
		for(int i = offseti; i<matrix.getRowDimension()+offseti; i++)
			for(int j = offsetj; j<matrix.getColumnDimension()+offsetj; j++)
				this.repository.write(query, new Object[]{ i2user.get(i), j2movie.get(j), matrix.get(i-offseti, j-offsetj) });
	}
	
}
