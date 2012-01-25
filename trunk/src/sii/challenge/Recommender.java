package sii.challenge;

import java.util.List;

import sii.challenge.domain.*;

/**
 * 
 * - riceve in input Training set e Test set
 * - crea le strutture dati per il Predictor
 * - crea l'IPredictor (scelto magari da file di Configurazione)
 * - per ogni tupla del Test set:
 *   - lancia l'IPredictor e attende i risultati
 * - restituisce i risultati
 *
 */
public class Recommender {

	private PreprocessedRecommendationDataset recommendationdataset;
	
	public Recommender(PreprocessedRecommendationDataset recommendationdataset)
	{
		this.recommendationdataset = recommendationdataset;
	}
	
	public List<MovieRating> recommend(List<MovieRating> input)
	{
		
	}
	
}
