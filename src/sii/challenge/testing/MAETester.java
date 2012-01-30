package sii.challenge.testing;

/**
 * 
 * - ottiene i dati dal DB
 * - crea CrossValidator (passandogli i dati del DB)
 * - lancia il CrossValidator e attende i MAE di risultato
 *
 */
public class MAETester {

	public void runTest()
	{
		CrossValidator crossvalidator = new CrossValidator();
		
		System.out.println("Starting CrossValidation...");
		float mae = crossvalidator.runTest();
		System.out.println("Cross Validator returned MAE " + mae);
	}
	
}
