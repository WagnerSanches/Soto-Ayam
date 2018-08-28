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
public class eri_i {

	main_function kernel;

	public eri_i(main_function kernel) {
		this.kernel = kernel;
	}

	public double[] X(int in, double l1[], double l2[], double l3[], double l4[], double epsilon1, double epsilon2,
			double QA[][], double PA[][], double VPQ[], double del) {
		int index = (int) (l1[in] + l2[in] + l3[in] + l4[in]);
		double G[] = new double[index + 1];
		for (int l = 0; l <= l1[in] + l2[in]; l++) {
			double res = (Math.pow(-1, l) * kernel.b.fk(l, l1[in], l2[in], PA[0][in], PA[1][in]))
					* kernel.specialFunc.fac(l);
			for (int laksen = 0; laksen <= l3[in] + l4[in]; laksen++) {
				for (int q = 0; q <= l / 2.0; q++) {
					for (int qaksen = 0; qaksen <= laksen / 2.0; qaksen++) {
						int L = l - 2 * q;
						int Laksen = laksen - 2 * qaksen;
						double res1 = res
								* (Math.pow(epsilon1, l - q) / (kernel.specialFunc.fac(q) * kernel.specialFunc.fac(L)))
								* (kernel.b.fk(laksen, l3[in], l4[in], QA[0][in], QA[1][in]))
								* (Math.pow(epsilon2, laksen - qaksen) * kernel.specialFunc.fac(laksen)
										/ (kernel.specialFunc.fac(qaksen) * kernel.specialFunc.fac(Laksen)));
						for (int t = 0; t <= 0.5 * (L + Laksen); t++) {
							if (L + Laksen - t <= index) {
								G[L + Laksen - t] += res1 * kernel.specialFunc.fac(L + Laksen) * Math.pow(-1, t)
										* Math.pow(VPQ[in], L + Laksen - 2 * t)
										/ (kernel.specialFunc.fac(t) * kernel.specialFunc.fac(L + Laksen - 2 * t)
												* Math.pow(del, L + Laksen - t));
							}
						}
					}
				}
			}
		}
		return G;
	}

	public double ERI(double l1[], double l2[], double l3[], double l4[], double a1, double a2, double a3, double a4,
			double ra[], double rb[], double rc[], double rd[]) {

		double y1 = a1 + a2;
		double y2 = a3 + a4;
		double epsilon1 = 1.0 / (4.0 * y1);
		double epsilon2 = 1.0 / (4.0 * y2);
		double rac[] = kernel.mp.mdot(ra, a1);
		double rbc[] = kernel.mp.mdot(rb, a2);
		double rp[] = kernel.mp.adddot(rac, rbc);
		double rac1[] = kernel.mp.mdot(rc, a3);
		double rbc1[] = kernel.mp.mdot(rd, a4);
		double rq[] = kernel.mp.adddot(rac1, rbc1);
		rp = kernel.mp.mdot(rp, 1.0 / y1);
		rq = kernel.mp.mdot(rq, 1.0 / y2);
		double AB = kernel.d.distance_run(ra, rb);
		double CD = kernel.d.distance_run(rc, rd);
		double PQ = kernel.d.distance_run(rp, rq);
		double K1 = Math.exp(-a1 * a2 * AB * AB / y1);
		double K2 = Math.exp(-a3 * a4 * CD * CD / y2);
		double PA[][] = new double[2][ra.length];
		double QA[][] = new double[2][ra.length];
		double VPQ[] = new double[ra.length];
		for (int j = 0; j < ra.length; j++) {
			VPQ[j] = kernel.d.distance_run(rp[j], rq[j]);
			PA[0][j] = kernel.d.distance_run(rp[j], ra[j]);
			PA[1][j] = kernel.d.distance_run(rp[j], rb[j]);
			QA[0][j] = kernel.d.distance_run(rq[j], rc[j]);
			QA[1][j] = kernel.d.distance_run(rq[j], rd[j]);
		}
		double del = epsilon1 + epsilon2;
		double sum = 0;
		double X1[] = X(0, l1, l2, l3, l4, epsilon1, epsilon2, QA, PA, VPQ, del);
		double X2[] = X(1, l1, l2, l3, l4, epsilon1, epsilon2, QA, PA, VPQ, del);
		double X3[] = X(2, l1, l2, l3, l4, epsilon1, epsilon2, QA, PA, VPQ, del);
		for (int i = 0; i <= l1[0] + l2[0] + l3[0] + l4[0]; i++) {
			for (int j = 0; j <= l1[1] + l2[1] + l3[1] + l4[1]; j++) {
				for (int k = 0; k <= l1[2] + l2[2] + l3[2] + l4[2]; k++) {
					sum += X1[i] * X2[j] * X3[k] * kernel.g.clc_F(i + j + k, PQ * PQ / (4 * del));
				}
			}
		}
		sum *= 2 * Math.pow(Math.PI, 2.5) * K1 * K2 / (y1 * y2 * Math.pow(y1 + y2, 0.5));
		return sum;
	}

}
