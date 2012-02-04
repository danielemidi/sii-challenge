package sii.challenge.preprocessing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.repository.Repository;
import sii.challenge.util.DataSource;

public class Preprocessor {

	protected DataSource dataSource;
	protected Connection connection;
	protected Repository repository;
	
	public Preprocessor()
	{
		this.dataSource = new DataSource();
		this.repository = new Repository();
	}

	public void preprocess() throws Exception
	{
		this.preprocessItemStaticSimilarity();
	}
	
	
	protected void preprocessItemStaticSimilarity() throws Exception
	{
		System.out.println("ID1\tID2\tACT\tDIR\tGEN\tCOU\tTAG\tALL\tTOP\tAUD\tDEC\t\tSIM");
		
		List<Integer> movieids = this.getMovieIDs();

		this.connection = this.dataSource.getConnection();
		for(int id1 : movieids)
		{
			if(id1>39) {
				for(int id2 : movieids)
				{
					calculateAndPersistSimilarity(id1, id2);
				}
			}
		}

		try {
			if (this.connection != null) this.connection.close();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
	}

	protected void calculateAndPersistSimilarity(int id1, int id2) throws Exception {
		float actorsincommon = this.getStuffInCommon("movie_actors", "actorID", id1, id2);
		float directorsincommon = this.getStuffInCommon("movie_directors", "directorID", id1, id2);
		float genresincommon = this.getStuffInCommon("movie_genres", "genre", id1, id2);
		float countriesincommon = this.getStuffInCommon("movie_countries", "country", id1, id2);
		float tagsincommon = this.getWeightedStuffInCommon("movie_tags", "tagID", "normalizedTagWeight", id1, id2);
		float allcriticsscorediscrepance = this.getDiscrepance("movies", "rtAllCriticsScore", 100, id1, id2);
		float topcriticsscorediscrepance = this.getDiscrepance("movies", "rtTopCriticsScore", 100, id1, id2);
		float audiencescorediscrepance = this.getDiscrepance("movies", "rtAudienceScore", 100, id1, id2);
		float decadediscrepance = this.getDecadeDiscrepance(id1, id2);
		float similarity = 
				actorsincommon * 6 + 
				directorsincommon * 3 + 
				genresincommon * 9 + 
				countriesincommon * 3 + 
				tagsincommon * 5 +
				allcriticsscorediscrepance * 5 + 
				topcriticsscorediscrepance * 5 + 
				audiencescorediscrepance * 5 + 
				decadediscrepance * 5;
		similarity /= 46;
		
		System.out.println(formatSimilarityLogLine(id1, id2, actorsincommon, directorsincommon, genresincommon, countriesincommon, tagsincommon, allcriticsscorediscrepance, topcriticsscorediscrepance, audiencescorediscrepance, decadediscrepance, similarity));
		
		PreparedStatement statement = null;
		String query = "INSERT INTO item_static_similarities (iditem1, iditem2, actors, directors, genres, countries, tags, allcriticsscore, topcriticsscore, audiencescore, decadediscrepance, similarity) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			
			statement = this.connection.prepareStatement(query);
			statement.setInt(1, id1);
			statement.setInt(2, id2);
			statement.setFloat(3, actorsincommon);
			statement.setFloat(4, directorsincommon);
			statement.setFloat(5, genresincommon);
			statement.setFloat(6, countriesincommon);
			statement.setFloat(7, tagsincommon);
			statement.setFloat(8, allcriticsscorediscrepance);
			statement.setFloat(9, topcriticsscorediscrepance);
			statement.setFloat(10, audiencescorediscrepance);
			statement.setFloat(11, decadediscrepance);
			statement.setFloat(12, similarity);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			//throw new Exception(e.getMessage());
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	protected String formatSimilarityLogLine(int id1, int id2, float actorsincommon, float directorsincommon, float genresincommon, float countriesincommon, float tagsincommon, float allcriticsscorediscrepance, float topcriticsscorediscrepance, float audiencescorediscrepance, float decadediscrepance, float similarity) {
		return id1+"\t"+id2+"\t"+actorsincommon+"\t"+directorsincommon+"\t"+genresincommon+"\t"+countriesincommon+"\t"+tagsincommon+"\t"+allcriticsscorediscrepance+"\t"+topcriticsscorediscrepance+"\t"+audiencescorediscrepance+"\t"+decadediscrepance+"\t\t"+similarity;
	}
	
	
	protected List<Integer> getMovieIDs() throws Exception
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
	protected List<Integer> getMovieIDs(int AfterIDIncluded) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<Integer> ids = new LinkedList<Integer>();
		ResultSet result = null;
		String query = "SELECT id FROM movies WHERE id>=?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, AfterIDIncluded);
			result = statement.executeQuery();

			while (result.next()) {
				ids.add(result.getInt(1));
			}
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
		
		return ids;
	}
	protected List<Integer> getRemainingMovieIDs(int iditem1) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<Integer> ids = new LinkedList<Integer>();
		ResultSet result = null;
		String query = "SELECT id from movies where id not in (SELECT iditem2 FROM item_static_similarities WHERE iditem1=?)";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, iditem1);
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
	
	
	protected float getStuffInCommon(String tablename, String idcolname, int idmovie1, int idmovie2) throws Exception
	{
		String query = "SELECT incommon/total as perc FROM " +
				       "(SELECT COUNT(*) as incommon " +
				   	   "FROM "+tablename+" A1 JOIN "+tablename+" A2 ON A1."+idcolname+"=A2."+idcolname + " " +
				       "WHERE A1.movieID = ? AND A2.movieID =?) G1, " + 
					   "(SELECT COUNT(*) as total " +
					   "FROM "+tablename+" T1 " +
					   "WHERE T1.movieID = ?) G2";
		
		return this.repository.getSingleFloatValue(query, new int[]{ idmovie1, idmovie2, idmovie1 }, this.connection);
	}

	
	
	protected float getWeightedStuffInCommon(String tablename, String idcolname, String weightcolname, int idmovie1, int idmovie2) throws Exception
	{
		String query = "SELECT incommon/total as perc FROM " +
				       "(SELECT SUM(1-ABS(A1."+weightcolname+"-A2."+weightcolname+")) as incommon " +
				   	   "FROM "+tablename+" A1 JOIN "+tablename+" A2 ON A1."+idcolname+"=A2."+idcolname + " " +
				       "WHERE A1.movieID = ? AND A2.movieID = ?) G1, " + 
					   "(SELECT COUNT(*) as total " +
					   "FROM "+tablename+" T1 " +
					   "WHERE T1.movieID = ?) G2";
		
		return this.repository.getSingleFloatValue(query, new int[]{ idmovie1, idmovie2, idmovie1 }, this.connection);
		
	}
	
	
	protected float getDiscrepance(String tablename, String colname, int normalizationfactor, int idmovie1, int idmovie2) throws Exception
	{
		String query = "SELECT 1-ABS((A1."+colname+" - A2."+colname+")/"+normalizationfactor+") as perc " +
				   	   "FROM "+tablename+" A1, "+tablename+" A2 " +
				       "WHERE A1.id = ? AND A2.id = ?";
		
		return this.repository.getSingleFloatValue(query, new int[]{ idmovie1, idmovie2 }, this.connection);
	}
	
	
	protected float getDecadeDiscrepance(int idmovie1, int idmovie2) throws Exception
	{
		String query = "SELECT 1-(ABS(FLOOR(A1.year/10) - FLOOR(A2.year/10))/10) as y " +
				   	   "FROM movies A1, movies A2 " +
				       "WHERE A1.id = ? AND A2.id = ?";
		
		float val = this.repository.getSingleFloatValue(query, new int[]{ idmovie1, idmovie2 }, this.connection);
		return val>=0 ? val : 0;
	}
	
}
