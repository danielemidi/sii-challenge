package sii.challenge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.MovieRating;
import sii.challenge.domain.RatingMatrix;
import sii.challenge.domain.TrainingDataset;
import sii.challenge.util.DataSource;

public class KSetRepository extends Repository implements IRepository {

	private DataSource dataSource;
	private int K;
	private int KSetSize;
	private int CurrentSetIndex;
	
	private int MovieRatingCount = 0;
	
	public KSetRepository(int K)
	{
		this.dataSource = new DataSource();
		
		try {
			this.MovieRatingCount = (int)this.getSingleFloatValueWithoutQueryRewriting("SELECT COUNT(*) FROM user_ratedmovies", new int[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.K = K;
		this.KSetSize = this.MovieRatingCount / this.K;
	}
	
	
	public void setCurrentSetIndex(int index)
	{
		this.CurrentSetIndex = index;
	}
	

	
	private String getKView()
	{
		int startrowindex = this.KSetSize*this.CurrentSetIndex - 1;
		int ithsetendingindex = this.KSetSize*(this.CurrentSetIndex+1);
		
		return "((SELECT * FROM user_ratedmovies LIMIT 0, "+startrowindex+") UNION (SELECT * FROM user_ratedmovies LIMIT "+ithsetendingindex+", "+this.MovieRatingCount+")) AS KVIEW";
	}
	private String adaptQueryToCurrentKSet(String query)
	{
		return query.replaceAll("user_ratedmovies", this.getKView());
	}
	


	public float getSingleFloatValue(String query, int[] args, Connection connection) throws Exception
	{
		//query = adaptQueryToCurrentKSet(query);
		return super.getSingleFloatValue(query, args, connection);
	}
	public float getSingleFloatValueWithoutQueryRewriting(String query, int[] args) throws Exception
	{
		Connection connection = null;
		float result = 0;
		
		try {
			connection = this.dataSource.getConnection();
			result = super.getSingleFloatValue(query, args, connection);
		} catch(Exception e) {
			
		} finally {
			try {
				if (connection != null) connection.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
		
		return result;
	}
	
	
	public List<MovieRating> getMovieRatingListFromTrainingSet(String query, int[] args) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;

		query = adaptQueryToCurrentKSet(query);
		
		try {
			statement = connection.prepareStatement(query);
			for(int i = 0; i<args.length; i++) statement.setInt(i+1, args[i]);
			result = statement.executeQuery();

			while (result.next()) {
				MovieRating rating = new MovieRating(
						result.getInt("userID"),
						result.getInt("movieID"),
						result.getLong("timestamp"),
						result.getFloat("rating")
				);
				ratings.add(rating);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
		
		return ratings;
	}
	
	
	
	/**
	 * Restituisce l'i-esimo set della tabella degli UserRatings, divisa in K set.
	 * @param k number of set in which to split the whole table
	 * @param i zero-based index of the set to retrieve
	 * @return
	 * @throws Exception 
	 */
	public List<MovieRating> getTestSet() throws Exception {
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;
		String query = "select * from user_ratedmovies order by movieid limit ?, ?";

		int ksetsize = this.MovieRatingCount / this.K;
		int startrowindex = ksetsize*this.CurrentSetIndex;
		
		System.out.println("REP - TestSet goes from " + startrowindex + " to " + (startrowindex+ksetsize) + ".");
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, startrowindex);
			statement.setInt(2, ksetsize);
			result = statement.executeQuery();

			while (result.next()) {
				MovieRating rating = new MovieRating(
						result.getInt("userID"),
						result.getInt("movieID"),
						result.getLong("timestamp"),
						result.getFloat("rating")
				);
				ratings.add(rating);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
		
		return ratings;
	}
	
}
