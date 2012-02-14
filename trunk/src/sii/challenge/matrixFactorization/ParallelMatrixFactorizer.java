package sii.challenge.matrixFactorization;

import java.util.List;
import java.util.Map;

import sii.challenge.repository.K3SetRepository;

import Jama.Matrix;

public class ParallelMatrixFactorizer implements Runnable {
	
	private String taskid;
	
	private Matrix R;
	private boolean doEvenRows;
	private boolean doEvenCols;
	private MatrixFactorizationDataAdapter dataadapter;
	
	public ParallelMatrixFactorizer(Matrix R, boolean doEvenRows, boolean doEvenCols, Map<Integer, Integer> i2user, Map<Integer, Integer> j2movie) {
		this.R = R;
		this.doEvenRows = doEvenRows;
		this.doEvenCols = doEvenCols;
		this.dataadapter = new MatrixFactorizationDataAdapter(new K3SetRepository());
		this.dataadapter.setI2user(i2user);
		this.dataadapter.setJ2movie(j2movie);
		
		this.taskid = "["+(doEvenRows?"P":"D") + (doEvenCols?"P":"D")+"]"; 
	}
	
	public void factorize() throws Exception {
		int K = 2;

		int blocksize = 50;
		for(int blocki = 0, i = 0; blocki < R.getRowDimension(); blocki+=blocksize, i++) {
			if(((i%2==0) && doEvenRows) || ((i%2!=0) && !doEvenRows)) {
				for(int blockj = 0, j = 0; blockj < R.getColumnDimension(); blockj+=blocksize, j++) {
					if((((j%2==0) && doEvenCols) || ((j%2!=0) && !doEvenCols) ) && !isAlreadyFactorized(blocki, blockj)) {
						
						Matrix SubR = R.getMatrix(blocki, blocki+blocksize-1, blockj, blockj+blocksize-1);
						int U = SubR.getRowDimension();
						int	M = SubR.getColumnDimension();
						
						System.out.println(this.taskid+" Starting factorization for submatrix "+blocki+","+blockj+"...");
						Matrix P = Matrix.random(U,K);
						Matrix Q = Matrix.random(M,K);
						List<Matrix> matrixs = MatrixFactorizer.factorize(SubR, P, Q, K, MatrixFactorizer.steps, MatrixFactorizer.alpha, MatrixFactorizer.beta);
						
						Matrix nP = matrixs.get(0);
						Matrix nQ = matrixs.get(1);
						nQ = nQ.transpose();
						Matrix nR = MatrixFactorizer.dot(nP,nQ);
						
						//Matrix err = SubR.minus(nR);
						//MatrixFactorizer.printMatrix(err);
						
						System.out.println(this.taskid+" Factorization complete. Writing... ");
		
						dataadapter.adaptAndWrite(nR, blocki, blockj);
						
						System.out.println("done.");
					}
				}
			}
		}	
	}

	
	/**
	 * Verifica se una certa sottomatrice � gi� stato fattorizzata in precedenza
	 * @throws Exception 
	 * 
	 */
	private boolean isAlreadyFactorized(int blocki, int blockj) throws Exception {
		int userID = 0;
		int movieID = 0;
		try{
		 userID = this.dataadapter.getI2user().get(blocki);
		 movieID = this.dataadapter.getJ2movie().get(blockj);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return this.dataadapter.getRepository().getSingleFloatValue("SELECT COUNT(*) FROM predictionmatrix WHERE userID=? AND movieID=?", new int[]{ userID, movieID }) != 0;
	}
	
	
	@Override
	public void run() {
		try {
			this.factorize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
