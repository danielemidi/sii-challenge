package sii.challenge.prediction;

import sii.challenge.repository.IRepository;
/**
 * Classico approccio User-Based NN, necessario il preprocessamento delle similaritÓ tra gli utenti con la Adjusted Cosine Similarity definita come funzione in MySQL 
 * per aumentare la velocitÓ di elaborazione.
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class UserBasedPredictor implements IPredictor {

	private final IRepository repository;

	/**
	 * Costruttore
	 * @param repository
	 */
	public UserBasedPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
	/**
	 * MODIFICARE LA QUERY!!!!!!
	 */
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		float p = 0;
		try {
			p = this.repository.getSingleFloatValue(
					"SELECT SUM(URM.rating * (ISS.genres))/SUM(ISS.genres) FROM " +
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
