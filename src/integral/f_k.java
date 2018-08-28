/* Source : Computational Techniques in Quantum Chemistry
 * and Molecular Physics
 * see equation on page 360
*/
package integral;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class f_k {

	main_function kernel;

	public f_k(main_function kernel) {
		this.kernel = kernel;
	}

	public double per(double a, double b) {
		double r = kernel.specialFunc.fac(a) / (kernel.specialFunc.fac(a - b) * kernel.specialFunc.fac(b));
		return r;
	}

	public double fk(int k, double l1, double l2, double PAx, double PBx) {
		double jumlah = 0;
		for (int i = 0; i <= l1; i++) {
			for (int j = 0; j <= l2; j++) {
				if (i + j == k) {
					jumlah = jumlah + Math.pow(PAx, l1 - i) * per(l1, i) * Math.pow(PBx, l2 - j) * per(l2, j);
				}
			}
		}
		return jumlah;
	}

}
