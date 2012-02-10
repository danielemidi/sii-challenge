package sii.challenge.repository;

import java.util.List;

import sii.challenge.domain.MovieRating;

public interface IKSetRepository extends IRepository {

	List<MovieRating> getTestSet() throws Exception;

	int getKSetSize();
	
	void setCurrentSetIndex(int index);
	
}
