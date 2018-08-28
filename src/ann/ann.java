package ann;

import tools.matrix_operation;

/**
 *
 * @author Agung Danu Wijaya
 */
public class ann {

	matrix_operation a = new matrix_operation();

	public double sigmoid(double x) {
		return x;
	}

	public double[] calcsig(double h[]) {
		for (int i = 0; i < h.length; i++) {
			h[i] = sigmoid(h[i]);
		}
		return h;
	}

	public double[] calc(double input, double w[], double p[]) {
		double hasil[] = new double[w.length];
		for (int i = 0; i < hasil.length; i++) {
			hasil[i] = w[i] * Math.pow(Math.abs(input), p[i]);
		}
		return hasil;
	}

	public double f_ANN(double[] input, int[] c_node, double w[][][][], double thv[][], double th[]) {
		int[] node = new int[c_node.length - 1];
		for (int i = 1; i < c_node.length; i++) {
			node[i - 1] = c_node[i];
		}
		for (int k = 0; k < node.length; k++) {
			double re[] = new double[node[k]];
			for (int i = 0; i < input.length; i++) {
				re = a.adddot(re, calc(input[i], w[0][k][i], w[1][k][i]));
			}
			input = a.adddot(re, calc(th[k], thv[k], thv[k]));
			input = calcsig(input);
		}
		return a.sum(input);
	}
}