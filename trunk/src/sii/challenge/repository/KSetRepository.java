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

public class KSetRepository {

	private DataSource dataSource;
	private int K;
	private int CurrentSetIndex;
	
	private int UserCount = 0;
	private int MovieRatingCount = 0;
	
	public KSetRepository(int K)
	{
		this.dataSource = new DataSource();
		
		try {
			this.UserCount = this.getUserCount();
			this.MovieRatingCount = this.getMovieRatingCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.K = K;
	}
	
	public void setCurrentSetIndex(int index)
	{
		this.CurrentSetIndex = index;
	}
	
	
	public float getSingleFloatValue(String query, int[] args) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		ResultSet result = null;
		float res = 0;

		try {
			statement = connection.prepareStatement(query);
			for(int i = 0; i<args.length; i++) statement.setInt(i+1, args[i]);
			result = statement.executeQuery();

			while (result.next()){
				res = result.getFloat(1);
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
		
		return res;
	}
	
	public List<MovieRating> getMovieRatingList(String query, int[] args) throws Exception {
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;

		int count = this.MovieRatingCount;
		int ksetsize = count / this.K;
		int startrowindex = ksetsize*this.CurrentSetIndex - 1;
		int ithsetendingindex = ksetsize*(this.CurrentSetIndex+1);
		
		String kview = "((SELECT * FROM user_ratedmovies LIMIT 0, "+startrowindex+") UNION (SELECT * FROM user_ratedmovies LIMIT "+ithsetendingindex+", "+count+"))";
		query = query.replaceAll("user_ratedmoview", kview);
		
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
	
	
	
	
	
	
	
	
	
	
	

	public int getUserCount() throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		ResultSet result = null;
		String query = "select count(distinct userid) from sii_challenge.user_ratedmovies";
		int count = -1;

		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();

			while (result.next()){
				count = result.getInt(1);
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
		
		return count;
	}
	public int getMovieRatingCount() throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		ResultSet result = null;
		String query = "select COUNT(*) from user_ratedmovies";
		int count = -1;

		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();

			while (result.next()){
				count = result.getInt(1);
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
		
		return count;
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
	

	
	/**
	 * Restituisce un Training Set costruito considerando tutti i K-1 set della tabella degli UserRatings (divisa in K set), tranne l'i-esimo.
	 * @return
	 * @throws Exception 
	 */
	public TrainingDataset getTrainingSet() throws Exception {
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;
		String query = "select * from user_ratedmovies order by movieid limit ?, ?";

		int count = this.MovieRatingCount;
		int ksetsize = count / this.K;
		int ithsetendingindex = ksetsize*(this.CurrentSetIndex+1);
		
		try {
			// prendo i record precedenti al set i-esimo
			statement = connection.prepareStatement(query);
			statement.setInt(1, 0);
			statement.setInt(2, ksetsize-1);
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
			
			// prendo i record successivi al set i-esimo
			statement = connection.prepareStatement(query);
			statement.setInt(1, ithsetendingindex);
			statement.setInt(2, count);
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
		
		// costruisce il trainingset
		TrainingDataset trainingdataset = new TrainingDataset();
		trainingdataset.setMovieratingmatrix(new RatingMatrix(count, this.UserCount, ratings));
		
		return trainingdataset;
	}
	
}
