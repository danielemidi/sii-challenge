package sii.challenge.preprocessing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.MovieRating;

public class Preprocessor {

	
	
	public Preprocessor()
	{
		
	}
	
	public void Preprocess()
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<MovieRating> ratings = new LinkedList<MovieRating>();
		ResultSet result = null;
		String query = "";
		
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
	
	private List<Integer> getMovieIDs()
	{
		
	}
	
}
