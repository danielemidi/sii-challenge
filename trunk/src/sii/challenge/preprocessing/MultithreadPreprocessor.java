package sii.challenge.preprocessing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MultithreadPreprocessor extends Preprocessor implements Runnable {

	private int tot;
	private int mod;
	
	public MultithreadPreprocessor(int mod, int tot)
	{
		super();
		this.mod = mod;
		this.tot = tot;
	}
	
	public void run()
	{
		try {
			this.preprocess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void preprocessItemStaticSimilarity() throws Exception
	{
		System.out.println("ID1\tID2\tACT\tDIR\tGEN\tCOU\tTAG\tALL\tTOP\tAUD\tDEC\t\tSIM");
		
		int i = 0;
		List<Integer> movieids = this.getMovieIDs();
		
		int firstID1todo = (int) super.repository.getSingleFloatValue("SELECT iditem1 FROM item_static_similarities GROUP BY iditem1 HAVING iditem1 % ? = ? AND COUNT(iditem2)<10197 LIMIT 1", new int[]{this.tot, this.mod});
		int firstID2todo = (int) super.repository.getSingleFloatValue("SELECT MAX(iditem2) FROM item_static_similarities WHERE iditem1=?", new int[]{firstID1todo});
		
		for(int id1 : movieids)
		{
			if(id1>=firstID1todo && id1 % this.tot == this.mod) 
			{
				for(int id2 : movieids)
				{
					if(id2>=firstID2todo) 
					{
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
						
						System.out.println(id1+"\t"+id2+"\t"+actorsincommon+"\t"+directorsincommon+"\t"+genresincommon+"\t"+countriesincommon+"\t"+tagsincommon+"\t"+allcriticsscorediscrepance+"\t"+topcriticsscorediscrepance+"\t"+audiencescorediscrepance+"\t"+decadediscrepance+"\t\t"+similarity);
						
						Connection connection = this.dataSource.getConnection();
						PreparedStatement statement = null;
						String query = "INSERT INTO item_static_similarities (iditem1, iditem2, actors, directors, genres, countries, tags, allcriticsscore, topcriticsscore, audiencescore, decadediscrepance, similarity) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						
						try {
							
							statement = connection.prepareStatement(query);
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
				firstID2todo = 0;
			}
			//i++;
		}
	}
	
}
