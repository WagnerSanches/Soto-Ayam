package tools;

import Jama.CholeskyDecomposition;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 *
 * @author Agung Danu Wijaya
 */
public class gev {

	public Matrix cholorth(Matrix S) {
		CholeskyDecomposition c = S.chol();
		return c.getL().inverse().transpose();
	}

	public Matrix simx(Matrix A, Matrix B) {
		return B.transpose().times(A.times(B));
	}

	public double[][] gev_run(double S[][], double F[][]) {
		Matrix A = cholorth(new Matrix(S));
		EigenvalueDecomposition e = simx(new Matrix(F), A).eig();
		double u[][] = main(e.getD().getArray(), e.getV().getArray());
		return A.times(new Matrix(u)).getArray();
	}

	public double[][] main(double[][] Da, double V[][]) {
		double D[] = new double[Da.length];
		for (int i = 0; i < Da.length; i++) {
			D[i] = Da[i][i];
		}
		V = new Matrix(V).transpose().getArray();
		double D1[] = new double[D.length];
		double V1[][] = new double[V.length][V[0].length];
		double d = 10000;
		int in = 0;
		for (int j = 0; j < D.length; j++) {
			for (int i = 0; i < D.length; i++) {
				if (D[i] < d) {
					in = i;
					d = D[i];
				}
			}
			d = 10000;
			V1[j] = V[in];
			D1[j] = in;
			D[in] = 1000;
		}
		V1 = new Matrix(V1).transpose().getArray();
		return V1;
	}
}
