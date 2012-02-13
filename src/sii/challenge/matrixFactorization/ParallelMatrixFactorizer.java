package sii.challenge.matrixFactorization;

import java.util.List;

import sii.challenge.repository.K3SetRepository;

import Jama.Matrix;

public class ParallelMatrixFactorizer implements Runnable {
	
	private Matrix R;
	private boolean doEvenRows;
	private boolean doEvenCols;
	
	public ParallelMatrixFactorizer(Matrix R, boolean doEvenRows, boolean doEvenCols) {
		this.R = R;
		this.doEvenRows = doEvenRows;
		this.doEvenCols = doEvenCols;
	}
	
	public void factorize() throws Exception {
		int K = 2;
		
		MatrixFactorizationDataAdapter dataadapter = new MatrixFactorizationDataAdapter(new K3SetRepository());

		int blocksize = 50;
		for(int blocki = 0, i = 0; blocki < R.getRowDimension(); blocki+=blocksize, i++) {
			if((i%2==0) && doEvenRows) {
				for(int blockj = 50, j = 0; blockj < R.getColumnDimension(); blockj+=blocksize, j++) {
					if((j%2==0) && doEvenCols) {
						Matrix SubR = R.getMatrix(blocki, blocki+blocksize-1, blockj, blockj+blocksize-1);
						int U = SubR.getRowDimension();
						int	M = SubR.getColumnDimension();
						
						System.out.println("Starting factorization for submatrix "+blocki+","+blockj+"...");
						Matrix P = Matrix.random(U,K);
						Matrix Q = Matrix.random(M,K);
						List<Matrix> matrixs = MatrixFactorizer.factorize(SubR, P, Q, K, MatrixFactorizer.steps, MatrixFactorizer.alpha, MatrixFactorizer.beta);
						
						Matrix nP = matrixs.get(0);
						Matrix nQ = matrixs.get(1);
						nQ = nQ.transpose();
						Matrix nR = MatrixFactorizer.dot(nP,nQ);
						
						Matrix err = SubR.minus(nR);
						MatrixFactorizer.printMatrix(err);
						
						System.out.print("Factorization complete. Writing... ");
		
						dataadapter.adaptAndWrite(nR, blocki, blockj);
						
						System.out.println("done.");
					}
				}
			}
		}	
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
