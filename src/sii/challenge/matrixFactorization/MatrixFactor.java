package sii.challenge.matrixFactorization;

import java.util.LinkedList;
import java.util.List;

import Jama.*;

public class MatrixFactor {

	/*
		@INPUT:
		    R     : a matrix to be factorized, dimension N x M
		    P     : an initial matrix of dimension N x K
		    Q     : an initial matrix of dimension M x K
		    K     : the number of latent features
		    steps : the maximum number of steps to perform the optimisation
		    alpha : the learning rate
		    beta  : the regularization parameter
		@OUTPUT:
		    the final matrices P and Q
	 */
	
	final static int steps = 5000;
	final static float alpha = 0.0002F;
	final static float beta = 0.02F;
			
	//Regularization parameters
	public static List<Matrix> matrixFactorization(Matrix R, Matrix P, Matrix Q, int K,int step, float alpha, float beta) {
		
		List<Matrix> feature = new LinkedList<Matrix>();
		Q = Q.transpose();
		double eij, dotVal;
		Matrix eR;
		int colP = P.getColumnDimension();
		int rowsQ = Q.getRowDimension();
		for(int s=0; s<step ; s++ ){
			for(int i=0; i<R.getRowDimension(); i++ )
				for(int j=0; j<R.getColumnDimension(); j++ )
				
					if(R.get(i, j)>0){
						dotVal = dot(P.getMatrix(i, i, 0, colP-1),Q.getMatrix(0, rowsQ-1, j, j)).get(0, 0);
						// eij è l'errore (!) ottenuto come differenza tra il rating reale e quello predetto!
						eij = R.get(i, j) - dotVal;	
						for(int k=0; k<K; k++){
							double pik = P.get(i,k);
							double qkj = Q.get(k,j);
							pik = pik + alpha * (2 * eij * qkj - beta * pik);
							P.set(i, k, pik); 
							qkj = qkj + alpha * (2 * eij * P.get(i,k) - beta * qkj);
							Q.set(k, j, qkj);
						}	
					}
			// non ne ho compreso il motivo della sua esistenza!
			eR = dot(P,Q);
	        double e = 0;
	        for(int i=0; i<R.getRowDimension(); i++ )
				for(int j=0; j<R.getColumnDimension(); j++ )
					if(R.get(i,j)>0){
						dotVal = dot(P.getMatrix(i, i, 0, K-1),Q.getMatrix(0, K-1, j, j)).get(0, 0);
						e = e + Math.pow(R.get(i,j) - dotVal, 2);			                    
						for(int k=0; k<K; k++)		                    	      
							e = e + (beta/2) * ( Math.pow(P.get(i,k),2) + Math.pow(Q.get(k,j),2));
			                    
					}
			 		
	        if(e < 0.001){
	        	break;
	        }
		}
		
		Q= Q.transpose();
    	feature.add(P);
    	feature.add(Q);	
		
    	return feature;
	}
	
//	
//	def matrix_factorization(R, P, Q, K, steps=5000, alpha=0.0002, beta=0.02):
//	    Q = Q.T
//	    for step in xrange(steps):

//	        for i in xrange(len(R)):
//	            for j in xrange(len(R[i])):
//	                if R[i][j] > 0:
//	                    eij = R[i][j] - numpy.dot(P[i,:],Q[:,j])
//	                    for k in xrange(K):
//	                        P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k])
//	                        Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j])

//	        eR = numpy.dot(P,Q)
//	        e = 0
//	        for i in xrange(len(R)):
//	            for j in xrange(len(R[i])):
//	                if R[i][j] > 0:
//	                    e = e + pow(R[i][j] - numpy.dot(P[i,:],Q[:,j]), 2)
//	                    for k in xrange(K):
//	                        e = e + (beta/2) * ( pow(P[i][k],2) + pow(Q[k][j],2) )
//	        if e < 0.001:
//	            break
//	    return P, Q.T
//	    	

	
	
/*
 
    nP, nQ = matrix_factorization(R, P, Q, K)
 	nR = numpy.dot(nP, nQ.T)
 
 */
	
	public static Matrix dot(Matrix P, Matrix Q){
		return P.times(Q);
	}
	
	public static void printMatrix(Matrix R)
	{	
		for(int i=0; i<R.getRowDimension(); i++){
			for(int j=0; j<R.getColumnDimension(); j++)
				System.out.print(R.get(i, j)+ " ");
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args){
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
		
		Matrix R = new Matrix(value);
		
		int U = R.getRowDimension();
		int	M = R.getColumnDimension();
		

//		Matrix R1 = R.getMatrix(0, 3, 0, 10);
//		int U = R1.getRowDimension();
//		int	M = R1.getColumnDimension();
//		printMatrix(R1);
		
		int K = 2;
		
		
		Matrix P = Matrix.random(U,K);
		Matrix Q = Matrix.random(M,K);
		List<Matrix> matrixs = matrixFactorization(R, P, Q, K, steps, alpha, beta);
		
//		Matrix R2 = R.getMatrix(4, 15, 0, 10);
//		printMatrix(R2);
//		Matrix P2 = Matrix.random(R2.getRowDimension(),K);
//		Matrix Q2 = Matrix.random(R2.getColumnDimension(),K);
//		List<Matrix> matrixs2 = matrixFactorization(R2, P2, Q2, K, steps, alpha, beta);

		
		
		Matrix nP = matrixs.get(0);
		printMatrix(nP); 
		Matrix nQ = matrixs.get(1);
		nQ= nQ.transpose();
		printMatrix(nQ);
		Matrix nR = dot(nP,nQ);
		
//		Matrix nP2 = matrixs2.get(0);
//		Matrix nQ2 = matrixs2.get(1);
//		nQ2= nQ2.transpose();
//		Matrix nR2 = dot(nP2,nQ2);
		
		
		printMatrix(nR);
//		printMatrix(nR2);

		
		System.out.println("dimensioni matrice r:"+ U +" c:" +M);
		System.out.println();
		
	}
	


	
}
