package jqc;

import java.util.HashMap;

import function.main_function;
import grid.grid;

/**
 *
 * @author Agung Danu Wijaya
 */
public class exc_functional {

	public double Vxc[][];
	public double Exc;

	public void LDA(double P[][], grid a, main_function kernel, double datagrid[][],
			HashMap<Integer, HashMap<Integer, double[]>> points) {
		double rho[] = new double[datagrid.length];
		for (int p = 0; p < datagrid.length; p++) {
			for (int i = 0; i < datagrid[p].length; i++) {
				for (int j = 0; j < datagrid[p].length; j++) {
					rho[p] += datagrid[p][i] * datagrid[p][j] * P[i][j];
				}
			}
		}

		double alpha = 2.0 / 3.0;
		double fac = -2.25 * alpha * Math.pow(0.75 / Math.PI, 1. / 3.);
		double rho3[] = kernel.mp.powdot(rho, 1. / 3.);
		double Fx[] = kernel.mp.mdot(kernel.mp.mdot(rho, rho3), fac);
		double[] dFxdn = kernel.mp.mdot(rho3, (4. / 3.) * fac);

		HashMap<Integer, double[]> pointmap = a.pointsmap(points);
		double Vxc[][] = new double[datagrid[0].length][datagrid[0].length];
		for (int p = 0; p < datagrid.length; p++) {
			for (int i = 0; i < datagrid[p].length; i++) {
				for (int j = 0; j < datagrid[p].length; j++) {
					double RG[] = pointmap.get(p);
					Vxc[i][j] += RG[3] * dFxdn[p] * datagrid[p][i] * datagrid[p][j];
				}
			}
		}
		double w[] = new double[datagrid.length];
		for (int p = 0; p < datagrid.length; p++) {
			double RG[] = pointmap.get(p);
			w[p] = RG[3];
		}
		double Exc = kernel.mp.sum(kernel.mp.mdot(w, kernel.mp.mdot(Fx, 2)));
		this.Exc = Exc;
		this.Vxc = Vxc;
	}

	public void ann(double P[][], grid a, main_function kernel, double datagrid[][],
			HashMap<Integer, HashMap<Integer, double[]>> points) {
		double rho[] = new double[datagrid.length];
		for (int p = 0; p < datagrid.length; p++) {
			for (int i = 0; i < datagrid[p].length; i++) {
				for (int j = 0; j < datagrid[p].length; j++) {
					rho[p] += datagrid[p][i] * datagrid[p][j] * P[i][j];
				}
			}
		}

		double delta = Math.pow(10, -7);
		double Fx[] = kernel.ex_ann.Ex(kernel, rho);
		double[] dFxdn = kernel.mp.mdot(kernel.mp.adddot(kernel.mp.mdot(Fx, -1), kernel.ex_ann.Ex(kernel, kernel.mp.adddot(rho, delta))),
				1.0 / delta);

		HashMap<Integer, double[]> pointmap = a.pointsmap(points);
		double Vxc[][] = new double[datagrid[0].length][datagrid[0].length];
		for (int p = 0; p < datagrid.length; p++) {
			for (int i = 0; i < datagrid[p].length; i++) {
				for (int j = 0; j < datagrid[p].length; j++) {
					double RG[] = pointmap.get(p);
					Vxc[i][j] += RG[3] * dFxdn[p] * datagrid[p][i] * datagrid[p][j];
				}
			}
		}
		double w[] = new double[datagrid.length];
		for (int p = 0; p < datagrid.length; p++) {
			double RG[] = pointmap.get(p);
			w[p] = RG[3];
		}
		double Exc = kernel.mp.sum(kernel.mp.mdot(w, kernel.mp.mdot(Fx, 2)));
		this.Exc = Exc;
		this.Vxc = Vxc;
	}
}
