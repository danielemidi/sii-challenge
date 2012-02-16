package sii.challenge.prediction;

/**
 * Semplice predittore che assegna un valore standar di 2.5 per ogni tupla (user, movie, timestamp)
 * @author Daniele Midi, Antonio Tedeschi
 *
 */
public class DumbPredictor implements IPredictor {

	@Override
	public float PredictRating(int userid, int movieid, long timestamp) {
		return 2.5F;
	}

}
