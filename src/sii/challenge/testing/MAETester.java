package sii.challenge.testing;

import sii.challenge.domain.CompleteTestDataset;
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
		CompleteTestDataset dataset = Repository.getCompleteTestDataset();
		
		CrossValidator crossvalidator = new CrossValidator();
		float mae = crossvalidator.runTest(dataset);
		System.out.println("Cross Validator returned MAE " + mae);
	}
	
}
