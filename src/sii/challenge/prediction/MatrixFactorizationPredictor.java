package sii.challenge.prediction;

import sii.challenge.repository.IRepository;

/**
 * Recupera dal database il rating per la tupla (user, movie, timestamp), che è stata precedentemente preprocessata tramite l'utilizzo del metodo factorize presente in MatrixFactorizer
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class MatrixFactorizationPredictor implements IPredictor {

	private final IRepository repository;
	
	/**
	 * Costruttore
	 * @param repository
	 */
	public MatrixFactorizationPredictor(IRepository repository)
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
			return this.repository.getSingleFloatValue("select rating from predictionmatrix where userID=? and movieID=?", new int[]{userid, movieid});
		} catch (Exception e) {
			return 0;
		}
	}

}
