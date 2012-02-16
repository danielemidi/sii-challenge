package sii.challenge.prediction;

/**
 * Semplice predittore che assegna un valore standar di 2.5 per ogni tupla (user, movie, timestamp)
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class DumbPredictor implements IPredictor {
	
	@Override
	/**
	 * Si faccia riferimento alla descrizione della classe e alla descrizione del omonimo metodo in IPredictor
	 * @param userid
	 * @param movieid
	 * @param timestamp
	 * @return
	 */
	public float PredictRating(int userid, int movieid, long timestamp) {
		return 2.5F;
	}

}
