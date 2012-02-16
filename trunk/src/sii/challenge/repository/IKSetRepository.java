package sii.challenge.repository;

import java.util.List;

import sii.challenge.domain.MovieRating;
/**
 * 
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public interface IKSetRepository extends IRepository {
	/**
	 * Restiuisce una lista di MovieRating che verrà utilizzata come set per il testing
	 * @return una lista di MovieRating
	 * @throws Exception
	 */
	List<MovieRating> getTestSet() throws Exception;

	/**
	 * Restituisce la grandezza del set
	 * @return un intero
	 */
	int getKSetSize();
	
	/**
	 * Setta l'indice del set corrente 
	 * @param index
	 */
	void setCurrentSetIndex(int index);
	
}
