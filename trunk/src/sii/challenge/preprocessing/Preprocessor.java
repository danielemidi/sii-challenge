package sii.challenge.preprocessing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.MovieRating;
import sii.challenge.util.DataSource;

public class Preprocessor {

	private DataSource dataSource;
	
	public Preprocessor()
	{
		this.dataSource = new DataSource();
	}
	
	public void preprocess() throws Exception
	{
		List<Integer> movieids = this.getMovieIDs();
		
		for(int id1 : movieids)
			for(int id2 : movieids)
			{
				int actorsincommon = this.getStuffInCommon("movie_actors", "actorID", id1, id2);
				int directorsincommon = this.getStuffInCommon("movie_directors", "directorID", id1, id2);
				float similarity = actorsincommon + directorsincommon;
				
				System.out.println(id1+", "+id2+", "+actorsincommon+", "+similarity);
				
				Connection connection = this.dataSource.getConnection();
				PreparedStatement statement = null;
				String query = "INSERT INTO item_static_similarities (iditem1, iditem2, actors, directors, similarity) VALUES (?,?,?,?,?)";
				
				try {
					
					statement = connection.prepareStatement(query);
					statement.setInt(1, id1);
					statement.setInt(2, id2);
					statement.setInt(3, actorsincommon);
					statement.setInt(4, directorsincommon);
					statement.setFloat(5, similarity);
					statement.executeUpdate();
					
				} catch (SQLException e) {
					throw new Exception(e.getMessage());
				} finally {
					try {
						if (statement != null) statement.close();
						if (connection != null) connection.close();
					} catch (SQLException e) {
						throw new Exception(e.getMessage());
					}
				}
			}
	}
	
	
	private List<Integer> getMovieIDs() throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<Integer> ids = new LinkedList<Integer>();
		ResultSet result = null;
		String query = "SELECT id FROM movies";
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();

			while (result.next()) {
				ids.add(result.getInt(1));
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
		
		return ids;
	}
	
	
	private int getStuffInCommon(String tablename, String idcolname, int idmovie1, int idmovie2) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		int rescount = 0;
		ResultSet result = null;
		String query = "SELECT COUNT(*) " +
					   "FROM "+tablename+" A1 JOIN "+tablename+" A2 ON A1."+idcolname+"=A2."+idcolname + " " +
					   "WHERE A1.movieID = ? AND A2.movieID =?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idmovie1);
			statement.setInt(2, idmovie2);
			result = statement.executeQuery();

			while (result.next()) {
				rescount = result.getInt(1);
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
		
		return rescount;
	}
	
}
