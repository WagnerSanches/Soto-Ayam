package traning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.json.simple.parser.ParseException;
import Jama.Matrix;
import defacom.db_cmd;
import function.main_function;
import geo_molecule.data_geo;
import run.driver_abinitio;

public class train_exc {
	main_function kernel;
	int ak = 0;
	int aj = 0;
	public HashMap<Integer, Double> jacobian = new HashMap<Integer, Double>();

	public double run(String name_molecule) throws ClassNotFoundException {
		db_cmd j = new db_cmd();
		try {
			j.get_mole(name_molecule, kernel);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		double[][] R = j.R;
		int numE[] = j.numE;
		String atom[] = new String[numE.length];
		for (int i = 0; i < atom.length; i++) {
			atom[i] = kernel.atom[numE[i] - 1];
		}
		data_geo mole = new data_geo(numE, atom, kernel.mp.mdot(R, kernel.geo.bondLenght));
		kernel.geo.data.put(name_molecule, mole);
		driver_abinitio drv = new driver_abinitio();
		// drv.HF_DFT(name_molecule, kernel);
		drv.DFT(name_molecule, kernel);
		// drv.HF(name_molecule, kernel);
		return drv.input;
	}

	public static void main(String args[]) throws ClassNotFoundException {
		train_exc a = new train_exc();
		// a.run_train();
		a.test2();
		// a.print_w();
	}

	public void prepare() throws ClassNotFoundException {
		main_function kernel = new main_function();
		this.kernel = kernel;
		this.kernel.tipebasis = "6-31g";
		this.kernel.verbose = 0; // 1 tambilkan proses
		int[] simpul = { 1, 2, 1 };
		this.kernel.c_node = simpul;
		this.kernel.URL_ANN = "JQC_data/ann_2_dft";
		this.kernel.status_int = 0; // 1 hitung integral
		this.kernel.Ex = "ANN";
		kernel.c_mix = 0.9;
		this.kernel.weight = new double[2][][][];
		init(1);
	}

	public void test1() throws ClassNotFoundException {
		prepare();
		double r = 0;
		String name[] = { "Si2", "Si", "Si" };// 74.0
		for (int i = 1; i <= 1; i++) {
			double en[] = new double[name.length];
			kernel.opti = 0.103;
			System.out.println(kernel.opti);
			r = hitung(name[0]);
			kernel.opti = 0;
			en[0] = r;
			for (int j = 1; j < name.length; j++) {
				double e = hitung(name[j]);
				en[j] = e;
				r -= e;
			}
			for (int j = 0; j < name.length; j++) {
				System.out.print(name[j] + " : " + en[j] + " ");
			}
			System.out.println("");
			System.out.println(name[0] + " : " + r * 627.5);
		}
	}

	public void test2() throws ClassNotFoundException {
		prepare();
		double r[] = { 0.51, 0.15, 0.05, 0.104 };
		String name[] = { "CH3SH", "H2S", "Na2", "Si2" };
		int a = 0;
		int b = name.length;
		for (int i = a; i < b; i++) {
			hitung(name[i]);
		}
		System.out.println(name.length);
		for (int i = a; i < b; i++) {
			System.out.print("{" + r[i] + "},");
		}
	}

	public void run_train() throws ClassNotFoundException {
		prepare();
		String name[] = { "H2", "Li", "LiH", "Cl2" };
		double re[][] = { { -1.166358 }, { -7.432217 }, { -8.022475 }, { -919.442209 } };
		double re_t[][] = new double[re.length][re[0].length];
		double delta = Math.pow(10, -7);
		double learning = 0.9;
		double peredam = 0.2;
		double iter = 10000;
		init(1);
		save(this.kernel.weight);
		for (int it = 0; it < iter; it++) {
			double jacobi[][] = new double[name.length][];
			for (int l = 0; l < name.length; l++) {
				int tan = 0;
				for (int i = 0; i < this.kernel.weight.length; i++) {
					for (int j = 0; j < this.kernel.weight[i].length; j++) {
						for (int k = 0; k < this.kernel.weight[i][j].length; k++) {
							for (int m = 0; m < this.kernel.weight[i][j][k].length; m++) {
								int ijk[] = { i, j, k, m };
								System.out.print((int) this.kernel.weight[i][j][k][m] + " : ");
								worker a = new worker(delta, ijk, name[l], tan);
								a.run();
								ak++;
								tan += 1;
							}
						}
					}
				}

				while (ak != aj) {
				}
				System.out.println("Selesai " + name[l]);
				double jaco[] = new double[jacobian.size()];
				for (int i = 0; i < jacobian.size(); i++) {
					jaco[i] = jacobian.get(i);
				}
				jacobi[l] = jaco;
				re_t[l][0] = hitung(name[l]);
			}
			double A[][] = a_t_a(jacobi, jacobi);
			for (int i = 0; i < A.length; i++) {
				A[i][i] += peredam;
			}

			double B[][] = a_t_a(jacobi, kernel.mp.adddot(re, kernel.mp.mdot(re_t, -1)));
			System.out.println(kernel.mp.sum(kernel.mp.adddotabs(re, kernel.mp.mdot(re_t, -1))));
			Matrix Aj = new Matrix(A);
			Aj = Aj.inverse();
			Aj = Aj.times(new Matrix(B));
			double dp[][] = Aj.getArray();
			int tan = 0;
			for (int i = 0; i < this.kernel.weight.length; i++) {
				for (int j = 0; j < this.kernel.weight[i].length; j++) {
					for (int k = 0; k < this.kernel.weight[i][j].length; k++) {
						for (int l = 0; l < this.kernel.weight[i][j][k].length; l++) {
							this.kernel.weight[i][j][k][l] += dp[tan][0] * learning;
							tan += 1;
						}
					}
				}
			}
			save(this.kernel.weight);
		}
	}

	public double[][] a_t_a(double a[][], double b[][]) {
		double r[][] = new double[a[0].length][a.length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a.length; j++) {
				r[i][j] = a[j][i];
			}
		}
		double dot[][] = new double[r.length][b[0].length];
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				for (int j2 = 0; j2 < b.length; j2++) {
					dot[i][j] += r[i][j2] * b[j2][j];
				}
			}
		}
		return dot;
	}

	public synchronized void input(double grad, int tan) {
		this.jacobian.put(tan, grad);
		aj++;
	}

	public class worker extends Thread {
		double delta;
		int[] ijk;
		String name;
		int tan;

		public worker(double delta, int[] ijk, String name, int tan) {
			this.delta = delta;
			this.ijk = ijk;
			this.name = name;
			this.tan = tan;
		}

		public void run() {
			train_exc k = new train_exc();
			try {
				k.prepare();
				k.kernel = kernel;
				double grad = k.grad(delta, ijk, name, k.kernel.weight);
				input(grad, tan);
			} catch (ClassNotFoundException e2) {
			}
		}
	}

	public double hitung(String name_molecule) throws ClassNotFoundException {
		kernel.URL_int = "JQC_data/int/" + name_molecule + "";
		double r = run(name_molecule);
		return r;
	}

	public double grad(double delta, int[] i, String name, double w[][][][]) throws ClassNotFoundException {
		double r = w[i[0]][i[1]][i[2]][i[3]];
		double t = hitung(name);
		w[i[0]][i[1]][i[2]][i[3]] += delta;
		double t1 = hitung(name);
		w[i[0]][i[1]][i[2]][i[3]] = r;
		double grad = (t1 - t) / delta;
		return grad;
	}

	public void init(int status) throws ClassNotFoundException {
		int[] node = new int[kernel.c_node.length - 1];
		for (int i = 1; i < kernel.c_node.length; i++) {
			node[i - 1] = kernel.c_node[i];
		}
		double w[][][] = new double[node.length][][];
		double w_p[][][] = new double[node.length][][];
		for (int i = 0; i < w.length; i++) {
			double wel[][] = new double[kernel.c_node[i]][];
			double wel_p[][] = new double[kernel.c_node[i]][];
			for (int j = 0; j < wel.length; j++) {
				double wdummy[] = new double[node[i]];
				wel[j] = this.kernel.mp.adddot(wdummy, 0.1);
				wel_p[j] = this.kernel.mp.adddot(wdummy, 1.000);
			}
			w[i] = wel;
			w_p[i] = wel_p;
		}

		this.kernel.weight[0] = w;
		this.kernel.weight[1] = w_p;
		if (status == 1) {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(kernel.URL_ANN));
				kernel.weight = (double[][][][]) inputStream.readObject();
				inputStream.close();
			} catch (FileNotFoundException ex) {
			} catch (IOException ex) {
			}
		}
		double thv[][] = new double[node.length][];
		double th[] = new double[node.length];
		for (int i = 0; i < thv.length; i++) {
			double w_l[] = new double[node[i]];
			w_l = kernel.mp.adddot(w_l, -0.00);
			thv[i] = w_l;
			th[i] = 0.00;
		}
		kernel.th = th;
		kernel.thv = thv;
	}

	public void save(double w[][][][]) {
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(kernel.URL_ANN));
			outputStream.writeObject(w);
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

}