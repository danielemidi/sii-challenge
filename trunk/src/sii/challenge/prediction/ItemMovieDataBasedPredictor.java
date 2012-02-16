package sii.challenge.prediction;

import sii.challenge.repository.IRepository;
@Deprecated
/**
 * 
 * Come predizione usa la media pesata dei voti dati dall'utente UserID ai TopN (100) Movies pi� simili a MovieID.
 * La similarit� fra Movies � precalcolata in base ai valori statici posseduti, quali corrispondenze di attori, registi, generi, general ratings, ...
 *
 */
public class ItemMovieDataBasedPredictor implements IPredictor {

	private final IRepository repository;
	
	public ItemMovieDataBasedPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		float p = 0;
		try {
			p = this.repository.getSingleFloatValue(
					"SELECT SUM(URM.rating * (ISS.tags))/SUM(ISS.tags) FROM " +
					"(SELECT * FROM user_ratedmovies WHERE userID=? AND movieID<>?) URM " +
					"JOIN " +
					"(SELECT iditem2, similarity FROM item_static_similarities WHERE iditem1=? ORDER BY similarity DESC LIMIT 100) ISS " +
					"ON URM.movieID=ISS.iditem2", 
					new int[]{ userid, movieid, movieid } );
			
		} catch (Exception e) {
			return 0;
		}
		
		return p;
	}

}
