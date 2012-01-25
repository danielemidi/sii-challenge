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

public class Repository {

	private DataSource dataSource;
	
	public Repository()
	{
		this.dataSource = new DataSource();
	}
	

	public int getUserCount() throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		ResultSet result = null;
		String query = "select COUNT(*) from (select distinct userid from user_ratedmovies)";
		int count = -1;

		try {
			statement = connection.prepareStatement(query);
			statement.setInt(0, 0);
			statement.setInt(0, 100);
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
			statement.setInt(0, 0);
			statement.setInt(0, 100);
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
	public List<MovieRating> getTestSet(int k, int i) throws Exception {
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;
		String query = "select * from user_ratedmovies order by movieid limit ?, ?";

		int ksetsize = this.getMovieRatingCount() / k;
		int startrowindex = ksetsize*i;
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(0, startrowindex);
			statement.setInt(0, ksetsize);
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
	 * @param k
	 * @param i
	 * @return
	 * @throws Exception 
	 */
	public TrainingDataset getTrainingSet(int k, int i) throws Exception {
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;
		String query = "select * from user_ratedmovies order by movieid limit ?, ?";

		int count = this.getMovieRatingCount();
		int ksetsize = this.getMovieRatingCount() / k;
		int ithsetendingindex = ksetsize*(i+1);
		
		try {
			// prendo i record precedenti al set i-esimo
			statement = connection.prepareStatement(query);
			statement.setInt(0, 0);
			statement.setInt(0, ksetsize-1);
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
			statement.setInt(0, ithsetendingindex);
			statement.setInt(0, count);
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
		trainingdataset.setMovieratingmatrix(new RatingMatrix(count, this.getUserCount(), ratings));
		
		return trainingdataset;
	}
	
}
