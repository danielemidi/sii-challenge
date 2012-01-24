package sii.challenge.testing;

/** 
 * 
 * - Riceve un dataset di training
 * - Splitta il dataset in k set
 * - per ogni set:
 *   - crea Recommender passandogli k-1 set come Training set e 1 set come Test set (senza rating)
 *   - lancia il Recommender e attende le predizioni di risultato
 *   - confronta le predizioni con il rating reale e calcola il MAE
 * - aggrega i MAE e restituisce il MAE globale
 *
 */
public class CrossValidator {

	public float runTest(List<MovieRating> dataset)
	{
		
	}
	
}
