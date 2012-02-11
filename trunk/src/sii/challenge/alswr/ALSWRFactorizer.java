package sii.challenge.alswr;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;

import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.als.AlternateLeastSquaresSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import sii.challenge.domain.DataModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ALSWRFactorizer extends AbstractFactorizer {

	private final DataModel dataModel;

	/** number of features used to compute this factorization */
	private final int numFeatures;
	/** parameter to control the regularization */
	private final double lambda;
	/** number of iterations */
	private final int numIterations;

	// � un logger utile durante le iterazioni per mostrare i passi... non ce ne frega nulla
	private static final Logger log = LoggerFactory.getLogger(ALSWRFactorizer.class);

	public ALSWRFactorizer(DataModel dataModel, int numFeatures, double lambda, int numIterations) throws Exception {
		super(dataModel);
		this.dataModel = dataModel;
		this.numFeatures = numFeatures;
		this.lambda = lambda;
		this.numIterations = numIterations;
	}

	/**
	 * questa classe � necessaria per la definizione delle feature !!!!!!
	 * */
	
	static class Features {

		private final DataModel dataModel;
		private final int numFeatures;

		private final double[][] M;
		private final double[][] U;

		Features(ALSWRFactorizer factorizer) throws Exception {
			this.dataModel = factorizer.dataModel;
			this.numFeatures = factorizer.numFeatures;
			Random random = RandomUtils.getRandom();
			M = new double[this.dataModel.getNumItems()][this.numFeatures];
			LongPrimitiveIterator itemIDsIterator = this.dataModel.getItemIDs();
			while (itemIDsIterator.hasNext()) {
				long itemID = itemIDsIterator.nextLong();
				int itemIDIndex = factorizer.itemIndex(itemID);
				M[itemIDIndex][0] = averateRating(itemID);
				for (int feature = 1; feature < this.numFeatures; feature++) {
					M[itemIDIndex][feature] = random.nextDouble() * 0.1;
				}
			}
			U = new double[this.dataModel.getNumUsers()][this.numFeatures];
		}

		double[][] getM() {
			return M;
		}

		double[][] getU() {
			return U;
		}

		DenseVector getUserFeatureColumn(int index) {
			return new DenseVector(U[index]);
		}

		DenseVector getItemFeatureColumn(int index) {
			return new DenseVector(M[index]);
		}

		void setFeatureColumnInU(int idIndex, Vector vector) {
			setFeatureColumn(U, idIndex, vector);
		}

		void setFeatureColumnInM(int idIndex, Vector vector) {
			setFeatureColumn(M, idIndex, vector);
		}

		protected void setFeatureColumn(double[][] matrix, int idIndex, Vector vector) {
			for (int feature = 0; feature < numFeatures; feature++) {
				matrix[idIndex][feature] = vector.get(feature);
			}
		}

		protected double averateRating(long itemID) throws Exception {
			PreferenceArray prefs = dataModel.getPreferencesForItem(itemID);
			RunningAverage avg = new FullRunningAverage();
			for (Preference pref : prefs) {
				avg.addDatum(pref.getValue());
			}
			return avg.getAverage();
		}
	}

	@Override
	public Factorization factorize() throws TasteException {
		log.info("starting to compute the factorization...");
		final AlternateLeastSquaresSolver solver = new AlternateLeastSquaresSolver();
		final Features features = new Features(this);

		for (int iteration = 0; iteration < numIterations; iteration++) {
			log.info("iteration {}", iteration);

			/* fix M - compute U */
			ExecutorService queue = createQueue();
			LongPrimitiveIterator userIDsIterator = dataModel.getUserIDs();
			try {
				while (userIDsIterator.hasNext()) {
					final long userID = userIDsIterator.nextLong();
					final LongPrimitiveIterator itemIDsFromUser = dataModel.getItemIDsFromUser(userID).iterator();
					final PreferenceArray userPrefs = dataModel.getPreferencesFromUser(userID);
					queue.execute(new Runnable() {
						@Override
						public void run() {
							List<Vector> featureVectors = new ArrayList<Vector>();
							while (itemIDsFromUser.hasNext()) {
								long itemID = itemIDsFromUser.nextLong();
								featureVectors.add(features.getItemFeatureColumn(itemIndex(itemID)));
							}
							Vector userFeatures = solver.solve(featureVectors, ratingVector(userPrefs), lambda, numFeatures);
							features.setFeatureColumnInU(userIndex(userID), userFeatures);
						}
					});
				}
			} finally {
				queue.shutdown();
				try {
					queue.awaitTermination(dataModel.getNumUsers(), TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					throw new IllegalStateException("Error when computing user features", e);
				}
			}

			/* fix U - compute M */
			queue = createQueue();
			LongPrimitiveIterator itemIDsIterator = dataModel.getItemIDs();
			try {
				while (itemIDsIterator.hasNext()) {
					final long itemID = itemIDsIterator.nextLong();
					final PreferenceArray itemPrefs = dataModel.getPreferencesForItem(itemID);
					queue.execute(new Runnable() {
						@Override
						public void run() {
							List<Vector> featureVectors = new ArrayList<Vector>();
							for (Preference pref : itemPrefs) {
								long userID = pref.getUserID();
								featureVectors.add(features.getUserFeatureColumn(userIndex(userID)));
							}
							Vector itemFeatures = solver.solve(featureVectors, ratingVector(itemPrefs), lambda, numFeatures);
							features.setFeatureColumnInM(itemIndex(itemID), itemFeatures);
						}
					});
				}
			} finally {
				queue.shutdown();
				try {
					queue.awaitTermination(dataModel.getNumItems(), TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					throw new IllegalStateException("Error when computing item features", e);
				}
			}
		}

		log.info("finished computation of the factorization...");
		return createFactorization(features.getU(), features.getM());
	}

	protected ExecutorService createQueue() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	protected Vector ratingVector(PreferenceArray prefs) {
		double[] ratings = new double[prefs.length()];
		for (int n = 0; n < prefs.length(); n++) {
			ratings[n] = prefs.get(n).getValue();
		}
		return new DenseVector(ratings);
	}
}
