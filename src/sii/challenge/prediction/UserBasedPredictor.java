package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

public class UserBasedPredictor implements IPredictor {

	private final IRepository repository;

	// IN PRECEDENZA PRENDEVA UN TRANING DATASET
	public UserBasedPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
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
