package traning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import Jama.Matrix;
import ann.ann_mixer;
import function.main_function;

public class mixer {

	public double input[][];
	public double re[][];
	public String name[];;
	main_function kernel;
	public String url;
	int ak = 0;
	int aj = 0;
	public HashMap<Integer, Double> jacobian = new HashMap<Integer, Double>();

	public int get_one(double input[]) throws ClassNotFoundException {
		prepare();
		init(1);
		cluster b = new cluster();
		double t = hitung(input);
		return b.make_cluster(t);
	}

	public double get_mix(double input[]) throws ClassNotFoundException {
		prepare();
		init(1);
		double t = hitung(input);
		return t;
	}

	public void test_range() throws ClassNotFoundException {
		prepare();
		init(1);
		cluster b = new cluster();
		for (int i = 0; i < re.length; i++) {
			double t = hitung(input[i]);
			System.out.println(name[i] + " " + re[i][0] + " " + t + " " + b.make_cluster(t));
			if (b.make_cluster(t) == 0) {
				System.out.println(i + ",");
			}
		}
	}

	public void test_all() throws ClassNotFoundException {
		prepare();
		init(1);
		cluster b = new cluster();
		data a = new data();
		for (int i = 0; i < a.out.length; i++) {
			double t = hitung(a.input[i]);
			if (b.make_cluster(t) == 1) {
				System.out.println(i + ",");
			}
		}
	}

	public void prepare() throws ClassNotFoundException {
		main_function kernel = new main_function();
		this.kernel = kernel;
		int[] simpul = { 5, 3, 3, 1 };
		this.kernel.c_node = simpul;
		this.kernel.URL_ANN = url;
		this.kernel.weight = new double[2][][][];

	}

	public void run_train() throws ClassNotFoundException {
		prepare();
		double re_t[][] = new double[re.length][re[0].length];
		double delta = Math.pow(10, -7);
		double learning = 0.9;
		double peredam = 0.2;
		init(1);
		System.out.println(re.length);
		save(this.kernel.weight);
		for (int l = 0; l < input.length; l++) {
			re_t[l][0] = hitung(input[l]);
		}
		double ERR = kernel.mp.sum(kernel.mp.adddotabs(re, kernel.mp.mdot(re_t, -1)));
		System.out.println(ERR);
		int rem = 0;
		for (int it = 0; it < 1; it++) {
			it--;
			double jacobi[][] = new double[input.length][];
			for (int l = 0; l < input.length; l++) {
				int tan = 0;
				for (int i = 0; i < this.kernel.weight.length; i++) {
					for (int j = 0; j < this.kernel.weight[i].length; j++) {
						for (int k = 0; k < this.kernel.weight[i][j].length; k++) {
							for (int m = 0; m < this.kernel.weight[i][j][k].length; m++) {
								int ijk[] = { i, j, k, m };
								worker a = new worker(delta, ijk, input[l], tan);
								a.run();
								ak++;
								tan += 1;
							}
						}
					}
				}

				while (ak != aj) {
				}
				double jaco[] = new double[jacobian.size()];
				for (int i = 0; i < jacobian.size(); i++) {
					jaco[i] = jacobian.get(i);
				}
				jacobi[l] = jaco;
				re_t[l][0] = hitung(input[l]);
			}
			double A[][] = a_t_a(jacobi, jacobi);
			for (int i = 0; i < A.length; i++) {
				A[i][i] += peredam;
			}

			double B[][] = a_t_a(jacobi, kernel.mp.adddot(re, kernel.mp.mdot(re_t, -1)));
			ERR = kernel.mp.sum(kernel.mp.adddotabs(re, kernel.mp.mdot(re_t, -1)));
			if (rem++ == 20) {
				rem = 0;
				System.out.println(ERR);
			}
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
		double input[];
		int tan;

		public worker(double delta, int[] ijk, double input[], int tan) {
			this.delta = delta;
			this.ijk = ijk;
			this.input = input;
			this.tan = tan;
		}

		public void run() {
			mixer k = new mixer();
			try {
				k.prepare();
				init(1);
				k.kernel = kernel;
				double grad = k.grad(delta, ijk, input, k.kernel.weight);
				input(grad, tan);
			} catch (ClassNotFoundException e2) {
			}
		}
	}

	public double hitung(double input[]) throws ClassNotFoundException {
		ann_mixer c = new ann_mixer();
		double r = c.f_ANN(input, kernel.c_node, kernel.weight, kernel.thv, kernel.th);
		return r;
	}

	public double grad(double delta, int[] i, double input[], double w[][][][]) throws ClassNotFoundException {
		double r = w[i[0]][i[1]][i[2]][i[3]];
		double t = hitung(input);
		w[i[0]][i[1]][i[2]][i[3]] += delta;
		double t1 = hitung(input);
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
				wel[j] = this.kernel.mp.adddot(wdummy, 10);
				wel_p[j] = this.kernel.mp.adddot(wdummy, 0.0001);
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