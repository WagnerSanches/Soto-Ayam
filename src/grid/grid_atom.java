/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.util.HashMap;

/**
 *
 * @author Richard P. Muller, Agung Danu Wijaya
 */
public class grid_atom {

	public HashMap<Integer, double[]> rungrid(double batom, double[] R) {
		HashMap<Integer, double[]> point = new HashMap<>();
		lebedev a = new lebedev();
		double[][] grid_params = LegendreGrid(batom);
		int i = 0;
		for (double[] grid_param : grid_params) {
			double rrad, wrad, nang;
			rrad = grid_param[0];
			wrad = grid_param[1];
			nang = grid_param[2];
			double xyzw[][] = a.data.get((int) nang);
			for (double[] ds : xyzw) {
				double xang, yang, zang, wang;
				xang = ds[0];
				yang = ds[1];
				zang = ds[2];
				wang = ds[3];
				double w = wrad * wang;
				double f[] = { rrad * xang + R[0], rrad * yang + R[1], rrad * zang + R[2], w };
				point.put(i++, f);
			}
		}
		return point;
	}

	public double BeckeRadMap(double x, double Rmax) {
		return Rmax * (1.0 + x) / (1.0 - x);
	}

	public double ang_mesh(double frac, double fineness) {
		double[][] ang_levels = { { 6, 14, 26, 26, 14 }, // Coarse
				{ 50, 50, 110, 50, 26 }, // Medium
				{ 50, 110, 194, 110, 50 }, // Fine
				{ 194, 194, 194, 194, 194 } // ultrafine
		};
		double alevs[] = ang_levels[(int) fineness];
		double nang = alevs[0];
		if (frac > 0.4) {
			nang = alevs[1];
		}
		if (frac > 0.5) {
			nang = alevs[2];
		}
		if (frac > 0.7) {
			nang = alevs[3];
		}
		if (frac > 0.8) {
			nang = alevs[4];
		}
		return nang;
	}

	public double[][] LegendreGrid(double batom) {
		bragg a = new bragg();
		legendre b = new legendre();
		double Rmax = 0.5 * a.Bragg[(int) batom] * 1.889725989;
		double nrad = 32;
		double fineness = 2;
		double grid[][] = new double[(int) nrad][3];
		double radial[][] = b.data.get((int) nrad);
		for (int i = 0; i < radial.length; i++) {
			double xrad = radial[i][0];
			double wrad = radial[i][1];
			double rrad = BeckeRadMap(xrad, Rmax);
			double dr = 2 * Rmax / Math.pow(1 - xrad, 2);
			double vol = 4 * Math.PI * rrad * rrad * dr;
			double nangpts = ang_mesh((float) (i + 1) / nrad, fineness);
			grid[i][0] = rrad;
			grid[i][1] = wrad * vol;
			grid[i][2] = nangpts;
		}
		return grid;
	}
}
