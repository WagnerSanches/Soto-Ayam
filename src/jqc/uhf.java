package jqc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import Jama.Matrix;
import function.main_function;
import geo_molecule.data_geo;

/**
 *
 * @author Agung Danu Wijaya, Dedy Farhamsa
 */
public class uhf {
	main_function kernel;

	public uhf(main_function kernel) {
		this.kernel = kernel;
	}

	public double SCF(String geo) throws IOException, ClassNotFoundException, InterruptedException {
		Map<String, data_geo> data;
		data = kernel.geo.data;
		double S[][] = null;
		double T[][] = null;
		double V[][] = null;
		double G[][][][] = null;
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
		double kali = kernel.c_mix;
		double En = 0;
		int panjangu = 0;
		int panjangd = 0;
		panjangu = kernel.spinup;
		panjangd = kernel.spindn;
		double DU[][] = new double[C.length][panjangu];
		double DD[][] = new double[C.length][panjangd];
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < panjangu; j++) {
				DU[i][j] = C[i][j];
			}
			for (int j = 0; j < panjangd; j++) {
				DD[i][j] = C[i][j];
			}
		}
		Matrix UU = new Matrix(DU);
		Matrix DUB = UU.times(UU.transpose());
		Matrix UD = new Matrix(DD);
		Matrix DDB = UD.times(UD.transpose());
		double PU[][] = DUB.getArray();
		double PD[][] = DDB.getArray();
		double CUold[][] = kernel.mp.copy(PU);
		double CDold[][] = kernel.mp.copy(PD);
		double enold = 0;
		int stop = 0;
		while (stop == 0) {
			double RPU[][][] = new double[2][S.length][S.length];
			double RPD[][][] = new double[2][S.length][S.length];
			for (int i = 0; i < S.length; i++) {
				for (int j = 0; j < S.length; j++) {
					for (int k = 0; k < S.length; k++) {
						for (int l = 0; l < S.length; l++) {
							RPU[0][i][j] += G[i][j][k][l] * PU[k][l];
							RPU[1][i][j] += G[i][k][j][l] * PU[k][l];
							RPD[0][i][j] += G[i][j][k][l] * PD[k][l];
							RPD[1][i][j] += G[i][k][j][l] * PD[k][l];
						}
					}
				}
			}
			double FU[][] = kernel.mp.adddot(H,
					kernel.mp.adddot(RPU[0], kernel.mp.adddot(kernel.mp.mdot(RPU[1], -1.0), RPD[0])));
			double FD[][] = kernel.mp.adddot(H,
					kernel.mp.adddot(RPD[0], kernel.mp.adddot(kernel.mp.mdot(RPD[1], -1.0), RPU[0])));
			double CU[][] = kernel.gev.gev_run(S, FU);
			double CD[][] = kernel.gev.gev_run(S, FD);
			for (int i = 0; i < CU.length; i++) {
				for (int j = 0; j < panjangu; j++) {
					DU[i][j] = CU[i][j];
				}
			}
			for (int i = 0; i < CD.length; i++) {
				for (int j = 0; j < panjangd; j++) {
					DD[i][j] = CD[i][j];
				}
			}

			UU = new Matrix(DU);
			DUB = UU.times(UU.transpose());
			UD = new Matrix(DD);
			DDB = UD.times(UD.transpose());
			PU = DUB.getArray();
			PD = DDB.getArray();
			PU = kernel.mp.adddot(kernel.mp.mdot(PU, 1 - kali), kernel.mp.mdot(CUold, kali));
			PD = kernel.mp.adddot(kernel.mp.mdot(PD, 1 - kali), kernel.mp.mdot(CDold, kali));
			CUold = kernel.mp.copy(PU);
			CDold = kernel.mp.copy(PD);
			En = kernel.mp.sum(kernel.mp.mdot(kernel.mp.adddot(H, FU), PU));
			En += kernel.mp.sum(kernel.mp.mdot(kernel.mp.adddot(H, FD), PD));
			En /= 2.0;
			if (Math.abs(En - enold) < 0.0001) {
				stop = 1;
			} else {
				enold = En;
			}
			if (kernel.verbose == 1) {
				System.out.println(En + kernel.geo.energi(data.get(geo)));
			}
		}
		return En + kernel.geo.energi(data.get(geo));
	}

}
