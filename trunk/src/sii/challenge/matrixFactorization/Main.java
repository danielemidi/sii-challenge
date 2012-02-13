package sii.challenge.matrixFactorization;

import java.util.List;

import sii.challenge.repository.*;

import Jama.Matrix;

public class Main {
	


	public static void main(String[] args){

		MatrixFactorizationDataAdapter dataadapter = new MatrixFactorizationDataAdapter(new K3SetRepository());
		
		Matrix R = null;
		try {
			R = dataadapter.readAndAdapt();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int K = 2;

		int blocksize = 50;
		for(int blocki = 0; blocki < R.getRowDimension(); blocki+=blocksize) {
			for(int blockj = 0; blockj < R.getColumnDimension(); blockj+=blocksize) {
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
				
				try {
					dataadapter.adaptAndWrite(nR, blocki);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("done.");
			}
		}		
		
		/*Matrix nP = matrixs.get(0);
		MatrixFactorizer.printMatrix(nP); 
		Matrix nQ = matrixs.get(1);
		nQ= nQ.transpose();
		MatrixFactorizer.printMatrix(nQ);
		Matrix nR = MatrixFactorizer.dot(nP,nQ);*/
				
		//MatrixFactorizer.printMatrix(nR);
//		printMatrix(nR2);
		
		//System.out.println("dimensioni matrice r:"+ U +" c:" +M);
		//System.out.println();
	}

	
	
	
	
	/*
	public static void main(String[] args){

		Matrix RRR = null;
		try {
			RRR = new MatrixFactorizationDataAdapter().readAndAdapt(new Repository());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[][] value = {
	         {5,3,0,1,5,3,0,1,5,3,0.5,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {4.5,0,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1.5,1,0,5,5,3,0,0,3.5,2,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5.5,3,0,1,5,3,0,1,4.5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {3.5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {2.5,0,0,1,5,3,0,1,5,3,0,1,5,2.5,0,1,1.5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {4,0,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         

	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {4,0,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},

	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {4,0,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},

	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {4,0,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,1,0,5,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {1,0,0,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
	         {0,1,5,4,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1,5,3,0,1},
	         
		};
		
		Matrix R = RRR; 
		R = new Matrix(value);
		
		int U = R.getRowDimension();
		int	M = R.getColumnDimension();
		

//		Matrix R1 = R.getMatrix(0, 3, 0, 10);
//		int U = R1.getRowDimension();
//		int	M = R1.getColumnDimension();
//		printMatrix(R1);
		
		int K = 2;
		
		
		Matrix P = Matrix.random(U,K);
		Matrix Q = Matrix.random(M,K);
		List<Matrix> matrixs = MatrixFactorizer.factorize(R, P, Q, K, MatrixFactorizer.steps, MatrixFactorizer.alpha, MatrixFactorizer.beta);
		
//		Matrix R2 = R.getMatrix(4, 15, 0, 10);
//		printMatrix(R2);
//		Matrix P2 = Matrix.random(R2.getRowDimension(),K);
//		Matrix Q2 = Matrix.random(R2.getColumnDimension(),K);
//		List<Matrix> matrixs2 = matrixFactorization(R2, P2, Q2, K, steps, alpha, beta);

		
		
		Matrix nP = matrixs.get(0);
		MatrixFactorizer.printMatrix(nP); 
		Matrix nQ = matrixs.get(1);
		nQ= nQ.transpose();
		MatrixFactorizer.printMatrix(nQ);
		Matrix nR = MatrixFactorizer.dot(nP,nQ);
		
//		Matrix nP2 = matrixs2.get(0);
//		Matrix nQ2 = matrixs2.get(1);
//		nQ2= nQ2.transpose();
//		Matrix nR2 = dot(nP2,nQ2);
		
		
		MatrixFactorizer.printMatrix(nR);
//		printMatrix(nR2);

		
		System.out.println("dimensioni matrice r:"+ U +" c:" +M);
		System.out.println();
		
	}*/
	
}
