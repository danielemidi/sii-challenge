package sii.challenge.testing;

import java.util.List;

import sii.challenge.Recommender;
import sii.challenge.domain.*;
import sii.challenge.repository.KSetRepository;

/** 
 * 
 * - Legge i dati dal database
 * - Splitta il dataset in k set
 * - per ogni set:
 *   - crea Recommender passandogli k-1 set come Training set e 1 set come Test set (senza rating)
 *   - lancia il Recommender e attende le predizioni di risultato
 *   - confronta le predizioni con il rating reale e calcola il MAE
 * - aggrega i MAE e restituisce il MAE globale
 *
 */
public class CrossValidator {

	public final int K = 500;
	
	public float runTest()
	{
		float totalmae = 0;
		System.out.println("CV - Creating Repository...");
		KSetRepository repository = new KSetRepository(K);
		
		for(int i = 0; i < K; i++) {
			try {
				System.out.println("CV - Current i = " + i + "...");
				repository.setCurrentSetIndex(i);
				Recommender recommender = new Recommender(repository);
				System.out.println("CV - Loading testset...");
				List<MovieRating> testset = repository.getTestSet();
				List<MovieRating> predictions = recommender.recommend(testset);

				System.out.println("CV - Calculating MAE...");
				float mae = this.calculateMAE(testset, predictions);
				System.out.println("CV - MAE of iteration " + i + ": " + mae);
				
				totalmae += mae;
				System.out.println("CV - Global MAE (partial, after iteration " + i + "): " + (totalmae/(i+1)));
				System.out.print("PRESS ENTER TO CONTINUE...");
				System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		totalmae /= K;
		
		return totalmae;
	}
	
	
	private float calculateMAE(List<MovieRating> expected, List<MovieRating> actual) {
		float mae = 0;
		
		for(int i = 0; i<expected.size(); i++) {
			mae += Math.abs(actual.get(i).getRating() - expected.get(i).getRating()); 
		}
		
		mae /= expected.size();
		
		return mae;
	}
}
