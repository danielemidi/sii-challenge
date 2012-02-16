package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * Predittore che fa la media dei voti dell'utente UserID per tutti i movie che ha votato
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class DumbUserPredictor implements IPredictor {

	private final IRepository repository;
	
	/**
	 * Costruttore
	 * @param repository
	 */
	public DumbUserPredictor(IRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	/**
	 * Si faccia riferimento alla descrizione della classe e alla descrizione del omonimo metodo in IPredictor
	 * @param userid
	 * @param movieid
	 * @param timestamp
	 * @return
	 */
	public float PredictRating(int userid, int movieid, long timestamp) {
		try {
			return this.repository.getSingleFloatValue("select avg(rating) from user_ratedmovies where userID=? and movieID<>?", new int[]{userid, movieid});
		} catch (Exception e) {
			return 0;
		}
	}

}
