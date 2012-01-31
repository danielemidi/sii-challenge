package sii.challenge.prediction;

import java.util.List;

import sii.challenge.domain.MovieRating;
import sii.challenge.repository.KSetRepository;

/**
 * 
 * Come predizione usa la media dei voti dati dall'utente UserID ai TopN (100) Movies più simili a MovieID.
 * La similarità fra Movies è precalcolata in base ai valori statici posseduti, quali corrispondenze di attori, registi, generi, general ratings, ...
 *
 */
public class ItemBasedPredictor implements IPredictor {

	private final KSetRepository repository;
	
	public ItemBasedPredictor(KSetRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		float p = 0;
		try {
			List<MovieRating> ratings = this.repository.getMovieRatingList(
					"SELECT URM.userID, URM.movieID, URM.timestamp, URM.rating FROM " +
					"(SELECT * FROM user_ratedmovies WHERE userID=?) URM " +
					"JOIN " +
					"(SELECT iditem2 FROM item_static_similarities WHERE iditem1=? ORDER BY similarity ASC LIMIT 100) ISS " +
					"ON URM.movieID=ISS.iditem2", 
					new int[]{ userid, movieid } );
						
			for(MovieRating mr : ratings) p+=mr.getRating();
			p/=ratings.size();
			
		} catch (Exception e) {
			return 0;
		}
		
		return p;
	}

}
