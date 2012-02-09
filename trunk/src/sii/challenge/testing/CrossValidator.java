package sii.challenge.testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sii.challenge.Recommender;
import sii.challenge.domain.*;
import sii.challenge.repository.*;

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

	public final int K = 100;
	public final int STOP_AFTER_EVERY = 1;
	
	public float runTest()
	{
		float totalmae = 0;
		System.out.println("CV - Creating Repository...");
		KSetRepository repository = new KSetRepository(K);
		
		List<Integer> indexes = new ArrayList<Integer>(K);
		for(int i = 0; i < K; i++) indexes.add(i);
		
		Collections.shuffle(indexes);
		
		int c = 1;
		for(int i : indexes)
		{
			try {
				System.out.println("\nCV - Current testset index = " + i + "...");
				repository.setCurrentSetIndex(i);
				Recommender recommender = new Recommender(repository);
				System.out.println("CV - Loading testset...");
				List<MovieRating> testset = repository.getTestSet();

				System.out.println("CV - Recommending...");
				long start = System.currentTimeMillis();
				List<MovieRating> predictions = recommender.recommend(testset);
				long end = System.currentTimeMillis();

				float elapsedSeconds = (end-start)/1000F;
				System.out.println("CV - Elapsed time: "+elapsedSeconds + "seconds. ("+(repository.getKSetSize()/elapsedSeconds)+" predictions per second)");

				System.out.println("CV - Calculating MAE...");
				float mae = this.calculateMAE(testset, predictions);
				System.out.println("CV - MAE of iteration " + c + ": " + mae);
				
				totalmae += mae;
				System.out.println("CV - Total predictions count: " + (c*repository.getKSetSize()));
				System.out.println("CV - Global MAE (partial, after iteration " + c + "): " + (totalmae/(c)));
				
				if(c % STOP_AFTER_EVERY == 0) {
					System.out.print("PRESS ANY KEY TO CONTINUE...");
					new BufferedReader(new InputStreamReader(System.in)).readLine(); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			c++;
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
