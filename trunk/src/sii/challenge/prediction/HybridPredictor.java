package sii.challenge.prediction;

import sii.challenge.domain.*;
import sii.challenge.repository.IRepository;

public class HybridPredictor implements IPredictor {

	private float userbasedpredictionweight;
	private float itembasedpredictionweight;
	
	private UserBasedPredictor userbp;
	private ItemBasedPredictor itembp;
	
	// farci passare matrice, lista user e lista movie e passarle agli oggetti che creo
	public HybridPredictor(IRepository repository)
	{
		this.userbp = new UserBasedPredictor(repository);
		//this.itembp = new ItemBasedPredictor(trainingdataset);
		
		this.userbasedpredictionweight = .5F;
		this.itembasedpredictionweight = .5F;
	}
	
	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		
		return this.userbasedpredictionweight * this.userbp.PredictRating(userid, movieid, timestamp) + 
			   this.itembasedpredictionweight * this.itembp.PredictRating(userid, movieid, timestamp);
		
	}

}
