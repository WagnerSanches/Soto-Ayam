/* Source : Computational Techniques in Quantum Chemistry
 * and Molecular Physics
 * see equation number 35 on page 366
*/
package integral;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class f {

	main_function kernel;

	public f(main_function kernel) {
		this.kernel = kernel;
	}

	public double clc_F(double m, double w) {
		double F = 0;
		int batas = 20;
		if (w == 0) {
			F = 1.0 / (2 * m + 1);
		} else if (m == 0 & w > 1) {
			F = Math.pow(w, -0.5);
			F *= kernel.specialFunc.erf(Math.pow(w, 0.5)) * Math.pow(Math.PI, 0.5) / 2.0;
		} else {
			for (double i = 0; i < batas; i++) {
				F += (Math.pow(w, i) / (kernel.specialFunc.gamma(m + i + 1.5)));
			}
			F *= 0.5 * kernel.specialFunc.gamma(m + 0.5) * Math.exp(-w);
		}
		return F;
	}
}
