/**
 * Source :, taketa1966
 * see page 2315 equation 2.2
 */
package integral;

import function.main_function;

/**
 * 
 * @author Agung Danu Wijaya
 */
public class normalize {

	main_function kernel;

	public normalize(main_function kernel) {
		this.kernel = kernel;
	}

	public double norm(double alpa, double l[]) {
		double norm = Math.sqrt(Math.pow(2, 2 * (l[0] + l[1] + l[2]) + 1.5) * Math.pow(alpa, l[0] + l[1] + l[2] + 1.5)
				/ (kernel.specialFunc.fac(kernel.specialFunc.fac(2 * l[0] - 1))
						* kernel.specialFunc.fac(kernel.specialFunc.fac(2 * l[1] - 1))
						* kernel.specialFunc.fac(kernel.specialFunc.fac(2 * l[2] - 1)) * Math.pow(Math.PI, 1.5)));
		return norm;
	}

}
