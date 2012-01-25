package sii.challenge.testing;

import sii.challenge.domain.PreprocessedRecommendationDataset;
import sii.challenge.repository.Repository;

/**
 * 
 * - ottiene i dati dal DB
 * - crea CrossValidator (passandogli i dati del DB)
 * - lancia il CrossValidator e attende i MAE di risultato
 *
 */
public class MAETester {

	public MAETester()
	{
		PreprocessedRecommendationDataset recommendationdataset = Repository.getPreprocessedRecommendationDataset();
		
		CrossValidator crossvalidator = new CrossValidator();
		float mae = crossvalidator.runTest(recommendationdataset);
		System.out.println("Cross Validator returned MAE " + mae);
	}
	
}
