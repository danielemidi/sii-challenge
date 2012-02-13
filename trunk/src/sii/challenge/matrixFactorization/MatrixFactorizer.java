package sii.challenge.matrixFactorization;

import java.util.LinkedList;
import java.util.List;

import Jama.*;

public class MatrixFactorizer {

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
	public static List<Matrix> factorize(Matrix R, Matrix P, Matrix Q, int K,int step, float alpha, float beta) {
		
		List<Matrix> feature = new LinkedList<Matrix>();
		Q = Q.transpose();
		double eij, dotVal;
		//Matrix eR;
		int colP = P.getColumnDimension();
		int rowsQ = Q.getRowDimension();
		for(int s=0; s<step ; s++ ){
			for(int i=0; i<R.getRowDimension(); i++ ) {
				for(int j=0; j<R.getColumnDimension(); j++ ) {
					
					System.out.print(".");
			
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
				}

				System.out.println();
			}
			// non ne ho compreso il motivo della sua esistenza!
			//eR = dot(P,Q);
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



	
}
