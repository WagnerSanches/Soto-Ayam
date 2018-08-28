package jqc;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import Jama.Matrix;
import function.main_function;
import geo_molecule.data_geo;
import grid.grid;

/**
 *
 * @author Agung Danu Wijaya, Dedy Farhamsa
 */
public class dft {

	public double SCF(String geo, main_function kernel) throws InterruptedException, ClassNotFoundException {
		grid a = new grid(geo, kernel);
		HashMap<Integer, HashMap<Integer, double[]>> points = a.points;
		Map<Integer, getdata.datakHF> bfs = kernel.gdata.get(geo);
		double[][] datagrid = a.setbfamps(kernel, bfs, points);
		Map<String, data_geo> data = kernel.geo.data;
		double Ej = 0, Exc = 0, Eh = 0;
		double G[][][][] = null;
		double S[][] = null;
		double T[][] = null;
		double V[][] = null;
		int id = 0;
		double totalma[] = new double[2];
		if (kernel.status_int == 1) {
			kernel.intg.one(geo);
			kernel.intg.two(geo);
			G = kernel.intg.ints;
			S = kernel.intg.S;
			T = kernel.intg.EK;
			V = kernel.intg.EV;
		} else {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(kernel.URL_int + "_G"));
				G = (double[][][][]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream(kernel.URL_int + "_S"));
				S = (double[][]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream(kernel.URL_int + "_T"));
				T = (double[][]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream(kernel.URL_int + "_V"));
				V = (double[][]) inputStream.readObject();
				inputStream.close();

			} catch (FileNotFoundException ex) {
				System.out.println(ex);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
		double H[][] = kernel.mp.adddot(V, T);
		double C[][] = kernel.gev.gev_run(S, H);
		double Cold[][];
		double mix_c = kernel.c_mix;
		int panjangc = Math.abs(kernel.spinup - kernel.spindn);
		int panjango = (int) ((kernel.spinup + kernel.spindn - panjangc) * 0.5);
		double[][] Do = new double[C.length][panjango];
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < panjango; j++) {
				Do[i][j] = C[i][j];
			}
		}
		Matrix Uc = new Matrix(Do);
		Matrix DBc = Uc.times(Uc.transpose());
		double[][] Dc = new double[C.length][panjangc];
		for (int i = 0; i < C.length; i++) {
			for (int j = panjango; j < panjangc + panjango; j++) {
				Dc[i][j - panjango] = C[i][j];
			}
		}
		Matrix Uo = new Matrix(Dc);
		Matrix DBo = Uo.times(Uo.transpose());
		double P[][] = new Matrix(kernel.mp.mdot(DBo.getArray(), 0.5)).plus(DBc).getArray();
		double enold = 0;
		int stop = 0;
		while (stop == 0) {
			double J[][] = new double[S.length][S[0].length];
			for (int i = 0; i < S.length; i++) {
				for (int j = 0; j < S.length; j++) {
					for (int k = 0; k < S.length; k++) {
						for (int l = 0; l < S.length; l++) {
							J[i][j] += G[i][j][k][l] * P[k][l];
						}
					}
				}
			}
			Ej = 2 * kernel.mp.sum(kernel.mp.mdot(J, P));
			Eh = 2 * kernel.mp.sum(kernel.mp.mdot(H, P));
			double Ek = 2 * kernel.mp.sum(kernel.mp.mdot(T, P));
			double Ev = 2 * kernel.mp.sum(kernel.mp.mdot(V, P));
			double total = Eh + Exc + Ej + kernel.geo.energi(data.get(geo));
			if (kernel.spindn + kernel.spinup == 1) {
				total = Ek + Ev;
				stop = 1;
				totalma[0] = total;
				totalma[1] = total;
			}
			totalma[id] = total;
			id++;
			if (id == 2) {
				id = 0;
			}
			if (kernel.verbose == 1) {
				System.out.println("Energi sistem " + total);
			}
			if (Math.abs(total - enold) < 0.00001) {
				stop = 1;
			} else {
				enold = total;
			}
			exc_functional FX = new exc_functional();
			if (kernel.Ex.equals("LDA")) {
				FX.LDA(P, a, kernel, datagrid, points);
			} else if (kernel.Ex.equals("ANN")) {
				FX.ann(P, a, kernel, datagrid, points);
			}
			double Vxc[][] = FX.Vxc;
			Exc = FX.Exc;
			double RP[][] = kernel.mp.adddot(kernel.mp.mdot(J, 2), Vxc);
			double F[][] = kernel.mp.adddot(H, RP);
			C = kernel.gev.gev_run(S, F);
			for (int i = 0; i < C.length; i++) {
				for (int j = 0; j < panjango; j++) {
					Do[i][j] = C[i][j];
				}
			}
			Uc = new Matrix(Do);
			DBc = Uc.times(Uc.transpose());
			Cold = kernel.mp.copy(P);
			for (int i = 0; i < C.length; i++) {
				for (int j = panjango; j < panjangc + panjango; j++) {
					Dc[i][j - panjango] = C[i][j];
				}
			}
			Uo = new Matrix(Dc);
			DBo = Uo.times(Uo.transpose());
			P = new Matrix(kernel.mp.mdot(DBo.getArray(), 0.5)).plus(DBc).getArray();
			P = kernel.mp.adddot(kernel.mp.mdot(P, 1 - mix_c), kernel.mp.mdot(Cold, mix_c));
		}
		if (kernel.verbose == 4) {
			kernel.mp.disp(totalma);
		}
		double Ek = kernel.mp.sum(kernel.mp.mdot(T, P));
		double Ev = kernel.mp.sum(kernel.mp.mdot(V, P));
		double Nai = kernel.geo.energi(data.get(geo));
		double rt[] = { Math.abs(Exc / Math.min(totalma[0], totalma[1])),
				Math.abs(Ej / Math.min(totalma[0], totalma[1])), Math.abs(Ek / Math.min(totalma[0], totalma[1])),
				Math.abs(Ev / Math.min(totalma[0], totalma[1])), Math.abs(Nai / Math.min(totalma[0], totalma[1])) };
		kernel.input_mixer = rt;
		kernel.mp.dispa(rt);
		return Math.min(totalma[0], totalma[1]);
	}
}