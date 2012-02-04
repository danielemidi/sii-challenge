package sii.challenge.preprocessing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MultithreadPreprocessor extends Preprocessor implements Runnable {

	private int tot;
	private int mod;
	private int id1lessthan;
	private int id1greaterthan;
	
	public MultithreadPreprocessor(int mod, int tot, int id1lessthan, int id1greaterthan)
	{
		super();
		this.mod = mod;
		this.tot = tot;
		this.id1lessthan = id1lessthan;
		this.id1greaterthan = id1greaterthan;
	}
	
	public void run()
	{
		try {
			this.preprocess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void preprocessItemStaticSimilarity() throws Exception
	{
		List<Integer> extmovieids;
		List<Integer> intmovieids;
		boolean incompletemovie = false;
		
		System.out.println("["+this.mod+"] ID1\tID2\tACT\tDIR\tGEN\tCOU\tTAG\tALL\tTOP\tAUD\tDEC\t\tSIM");
		
		int firstID1todo = (int) super.repository.getSingleFloatValue("SELECT MIN(iditem1) FROM item_static_similarities GROUP BY iditem1 HAVING (iditem1<? OR iditem1>=?) AND iditem1 % ? = ? AND COUNT(iditem2)<10197", new int[]{this.id1lessthan, this.id1greaterthan, this.tot, this.mod});
		if(firstID1todo > 0) {
			// ce n'è uno incompleto
			intmovieids = this.getRemainingMovieIDs(firstID1todo);
			incompletemovie = true;
		} else {
			// bisogna cominciarne uno nuovo
			firstID1todo = ((int)super.repository.getSingleFloatValue("SELECT MAX(iditem1) FROM item_static_similarities WHERE iditem1 % ? = ?", new int[]{this.tot, this.mod})) + 1;
			intmovieids = this.getMovieIDs();
		}
		
		extmovieids = this.getMovieIDs(firstID1todo, this.tot, this.mod);
		
		this.connection = this.dataSource.getConnection();
		for(int id1 : extmovieids)
		{
				for(int id2 : intmovieids) this.calculateAndPersistSimilarity(id1, id2);
				
				if(incompletemovie){
					intmovieids = this.getMovieIDs();
					incompletemovie = false;
				}
		}

		try {
			if (this.connection != null) this.connection.close();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
	}
	

	protected String formatSimilarityLogLine(int id1, int id2, float actorsincommon, float directorsincommon, float genresincommon, float countriesincommon, float tagsincommon, float allcriticsscorediscrepance, float topcriticsscorediscrepance, float audiencescorediscrepance, float decadediscrepance, float similarity) {
		return "["+this.mod+"] " + super.formatSimilarityLogLine(id1, id2, actorsincommon, directorsincommon, genresincommon, countriesincommon, tagsincommon, allcriticsscorediscrepance, topcriticsscorediscrepance, audiencescorediscrepance, decadediscrepance, similarity);
	}
	
	
	protected List<Integer> getMovieIDs(int AfterIDIncluded, int tot, int mod) throws Exception
	{
		Connection connection = this.dataSource.getConnection();
		PreparedStatement statement = null;
		List<Integer> ids = new LinkedList<Integer>();
		ResultSet result = null;
		String query = "SELECT id FROM movies WHERE id>=? AND id % ? = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, AfterIDIncluded);
			statement.setInt(2, tot);
			statement.setInt(3, mod);
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
	
}
