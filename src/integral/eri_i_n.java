package integral;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class eri_i_n {

	main_function kernel;

	public eri_i_n(main_function kernel) {
		this.kernel = kernel;
	}

	public static double basis(double indeks, double r[], double l[]) {
		double besarr = Math.pow(r[0], 2) + Math.pow(r[1], 2) + Math.pow(r[2], 2);
		return Math.pow(r[0], l[0]) * Math.pow(r[1], l[1]) * Math.pow(r[2], l[2]) * Math.exp(-indeks * besarr);
	}

	public double ERI(double l1[], double l2[], double l3[], double l4[], double a1, double a2, double a3, double a4,
			double ra[], double rb[], double rc[], double rd[]) {
		double h = 0.8;
		double rentang = 6.0;
		double nx = rentang / h;
		double ny = rentang / h;
		double nz = rentang / h;
		double sum = 0;
		for (double i = -nx; i < nx; i++) {
			for (double j = -ny; j < ny; j++) {
				for (double k = -nz; k < nz; k++) {
					for (double iq = -nx; iq < nx; iq++) {
						for (double jq = -ny; jq < ny; jq++) {
							for (double kq = -nz; kq < nz; kq++) {
								double re1[] = { i * h, j * h, k * h };
								double re2[] = { iq * h, jq * h, kq * h };
								// r1-ra
								double k1 = basis(a1, kernel.mp.adddot(re1, kernel.mp.mdot(ra, -1)), l1);
								// r1-rb
								double k2 = basis(a2, kernel.mp.adddot(re1, kernel.mp.mdot(rb, -1)), l2);
								// r2-rc
								double k3 = basis(a3, kernel.mp.adddot(re2, kernel.mp.mdot(rc, -1)), l3);
								// r2-r3
								double k4 = basis(a4, kernel.mp.adddot(re2, kernel.mp.mdot(rd, -1)), l4);

								double r = kernel.d.distance_run(re1, re2);
								if (r > 0) {
									sum += (k1 * k2 * k3 * k4 / r) * Math.pow(h, 6);
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}
}
