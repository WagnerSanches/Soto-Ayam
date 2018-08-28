package jqc;

import function.main_function;
import jqc.getdata.datakHF;

/**
 *
 * @author Agung Danu Wijaya
 */
public class getpointvalue {

	main_function kernel;

	public getpointvalue(main_function kernel) {
		this.kernel = kernel;
	}

	public double GTO(double indeks, double r[], double l[]) {
		double besarr = Math.pow(r[0], 2) + Math.pow(r[1], 2) + Math.pow(r[2], 2);
		return Math.pow(r[0], l[0]) * Math.pow(r[1], l[1]) * Math.pow(r[2], l[2]) * Math.exp(-indeks * besarr);
	}

	public double sumbasis(double ca[], double la[], double alphaa[], double Ra[]) {
		double sum = 0;
		for (int m = 0; m < ca.length; m++) {
			double a = alphaa[m];
			double res = GTO(a, Ra, la);
			sum += ca[m] * res * kernel.cn.norm(a, la);
		}
		return sum;
	}

	public double bf(datakHF a, double R[]) {
		double r[] = kernel.mp.adddot(R, kernel.mp.mdot(a.R, -1));
		return sumbasis(a.c, a.l, a.alpa, r);
	}
}
