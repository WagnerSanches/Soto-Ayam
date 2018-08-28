/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integral;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class nai_i_n {

	main_function kernel;

	public nai_i_n(main_function kernel) {
		this.kernel = kernel;
	}

	public static double basis(double indeks, double r[], double l[]) {
		double besarr = Math.pow(r[0], 2) + Math.pow(r[1], 2) + Math.pow(r[2], 2);
		return Math.pow(r[0], l[0]) * Math.pow(r[1], l[1]) * Math.pow(r[2], l[2]) * Math.exp(-indeks * besarr);
	}

	public double NAI(double a1, double a2, double l1[], double l2[], double ra[], double rb[], double rc[]) {
		double h = 0.1;
		double rentang = 6.0;
		double nx = rentang / h;
		double ny = rentang / h;
		double nz = rentang / h;
		double sum = 0;
		for (double i = -nx; i < nx; i++) {
			for (double j = -ny; j < ny; j++) {
				for (double k = -nz; k < nz; k++) {
					double re[] = { i * h, j * h, k * h };
					// r-ra
					double k1 = basis(a1, kernel.mp.adddot(re, kernel.mp.mdot(ra, -1)), l1);
					// r-rb
					double k2 = basis(a2, kernel.mp.adddot(re, kernel.mp.mdot(rb, -1)), l2);
					double r = kernel.d.distance_run(re, rc);
					if (r > 0) {
						sum += (k1 * k2 / r) * Math.pow(h, 3);
					}
				}
			}
		}
		return sum;
	}

}
