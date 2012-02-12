package sii.challenge.matrixFactorization;

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
			
	public List<Matrix> matrixFactorization(Matrix R,Matrix P,Matrix Q, int step, float alpha, float beta) {
		
		
		return null;
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
	
}
