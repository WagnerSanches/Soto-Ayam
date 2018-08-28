/* Source : Computational Techniques in Quantum Chemistry
 * and Molecular Physics : The overlap integral for unnormalized GTF 
 * see equation 25 to 26 on page 365
*/
package integral;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class electron_density {

	main_function kernel;

	public electron_density(main_function kernel) {
		this.kernel = kernel;
	}

	public double I(double PAx, double PBx, double l1, double l2, double y) {
		double Ix = 0;
		for (int i = 0; i <= (l1 + l2) * 0.5; i++) {
			Ix += kernel.b.fk(2 * i, l1, l2, PAx, PBx) * Math.pow(y, -(2 * i + 1) * 0.5)
					* kernel.specialFunc.gamma((2 * i + 1) * 0.5);
		}
		return Ix;
	}

	public double S(double a1, double a2, double l1[], double l2[], double ra[], double rb[]) {
		double y = a1 + a2;
		double rac[] = kernel.mp.mdot(ra, a1);
		double rbc[] = kernel.mp.mdot(rb, a2);
		double rp[] = kernel.mp.adddot(rac, rbc);
		rp = kernel.mp.mdot(rp, 1.0 / y);
		double AB = kernel.d.distance_run(ra, rb);
		double S = Math.exp(-a1 * a2 * AB * AB / y);
		double Ix = 1;
		for (int i = 0; i < ra.length; i++) {
			double PAx = kernel.d.distance_run(rp[i], ra[i]);
			double PBx = kernel.d.distance_run(rp[i], rb[i]);
			Ix *= I(PAx, PBx, l1[i], l2[i], y);
		}
		Ix *= S;
		return Ix;
	}

}
