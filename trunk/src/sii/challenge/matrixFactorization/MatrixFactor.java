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
	
	final int steps = 5000;
	final float alpha = 0.0002F;
	final float beta = 0.02F;
			
	public List<Matrix> matrixFactorization(Matrix R, Matrix P, Matrix Q, int step, float alpha, float beta) {
		
		List<Matrix> feature = new LinkedList<Matrix>();
		Matrix QT = Q.transpose();
		int eij;
		for(int s=0; s<step ; s++ ){
			for(int i=0; i<R.getRowDimension() ; i++ ){
				for(int j=0; j<R.getColumnDimension() ; j++ ){
					if(R.get(i, j)>0){
						
						eij = R.get(i, j) - ;
						
						
					}
				}
			}

			
		}
		
		if(e<0.001)
	        break;
		
		
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
 if __name__ == "__main__":
    R = [
         [5,3,0,1],
         [4,0,0,1],
         [1,1,0,5],
         [1,0,0,4],
         [0,1,5,4],
        ]

    R = numpy.array(R)

    N = len(R)
    M = len(R[0])
    K = 2

    P = numpy.random.rand(N,K)
    Q = numpy.random.rand(M,K)

    nP, nQ = matrix_factorization(R, P, Q, K)
 	nR = numpy.dot(nP, nQ.T)
 
 */
	public static void main(String[] args){
		double[][] value = {
	         {5,3,0,1},
	         {4,0,0,1},
	         {1,1,0,5},
	         {1,0,0,4},
	         {0,1,5,4},
		};
		
		Matrix R = new Matrix(value);
		Matrix RT = R.transpose();
		for(int i=0; i<R.getRowDimension(); i++){
			for(int j=0; j<R.getColumnDimension(); j++)
				System.out.print(R.get(i, j)+ " ");
			System.out.println();
			
		}
		
		System.out.println();System.out.println();
		for(int i=0; i<RT.getRowDimension(); i++){
			for(int j=0; j<RT.getColumnDimension(); j++)
				System.out.print(RT.get(i, j)+ " ");
			System.out.println();
			
		}
		
		System.out.println("\n done");
		
		
	}
	


	
}
