package jqc;

import function.main_function;
import jqc.getdata.datakHF;

/**
 *
 * @author Agung Danu Wijaya
 */
public class getS {

	main_function kernel;

	public getS(main_function kernel) {
		this.kernel = kernel;
	}

	public double sumbasis(double ca[], double cb[], double la[], double lb[], double alphaa[], double alphab[],
			double Ra[], double Rb[]) {
		double sum = 0;
		for (int i = 0; i < cb.length; i++) {
			for (int m = 0; m < ca.length; m++) {
				double a = alphaa[m];
				double b = alphab[i];
				double res = kernel.rhoe.S(a, b, la, lb, Ra, Rb);
				sum += cb[i] * ca[m] * res * kernel.cn.norm(a, la) * kernel.cn.norm(b, lb);
			}
		}
		return sum;
	}

	public double clc_getS(datakHF a, datakHF b) {
		return sumbasis(a.c, b.c, a.l, b.l, a.alpa, b.alpa, a.R, b.R);
	}
}
