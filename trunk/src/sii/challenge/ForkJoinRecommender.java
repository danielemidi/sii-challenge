package sii.challenge;

import java.util.List;

import sii.challenge.domain.*;
import sii.challenge.repository.IRepository;

public class ForkJoinRecommender extends Recommender {
	
	public ForkJoinRecommender(IRepository repository)
	{
		super(repository);
	}
	
	public List<MovieRating> recommend(List<MovieRating> input) throws Exception
	{
		return super.recommend(input);
	}
	
	
}
