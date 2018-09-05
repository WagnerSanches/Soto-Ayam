package run;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import org.json.simple.parser.ParseException;
import ann.ann_mixer;
import defacom.db_cmd;
import function.main_function;
import geo_molecule.data_geo;
import traning.mixer_main;
import traning.data;

public class ann_ann_dft_hf {
	main_function kernel;
	public HashMap<Integer, Double> jacobian = new HashMap<Integer, Double>();

	public static void main(String args[]) throws ClassNotFoundException {
		ann_ann_dft_hf a = new ann_ann_dft_hf();
		a.test();
	}

	public void test() throws ClassNotFoundException {
		double r = 0;
		String name[] = { "CH3Cl", "H", "H", "H", "Cl", "C" };
		prepare_mixer(name[0]);
		prepare_dft();
		kernel.c_mix = 0.9;
		System.out.println(kernel.opti);
		double en[] = new double[name.length];
		r = run_scf(name[0], "run");
		en[0] = r;
		kernel.opti = 0;
		for (int j = 1; j < name.length; j++) {
			double e = run_scf(name[j], "run");
			en[j] = e;
			r -= e;
		}
		for (int j = 0; j < name.length; j++) {
			System.out.print(name[j] + " : " + en[j] + " ");
		}
		System.out.println("");
		System.out.println(name[0] + " : " + r * 627.5);
	}

	public void test_all() throws ClassNotFoundException {
		double r = 0;
		data a = new data();
		String names[] = a.ae.split("}");
		// System.out.println("Energi atom dalam Hartree (627.5 kcal/mol)");
		for (int i = 0; i < names.length; i++) {
			String name[] = names[i].split(",");
			prepare_mixer(name[0]);
			prepare_dft();
			kernel.c_mix = 0.9;
			double en[] = new double[name.length];
			r = run_scf(name[0], "run");
			en[0] = r;
			kernel.opti = 0;
			for (int j = 1; j < name.length; j++) {
				double e = run_scf(name[j], "run");
				en[j] = e;
				r -= e;
			}
			for (int j = 0; j < name.length; j++) {
				// System.out.print(name[j] + " : " + en[j] + "");
			}
			System.out.println(name[0] + " : " + r * 627.5 + " kcal/mol");
		}
	}

	public void prepare_dft() throws ClassNotFoundException {
		this.kernel.tipebasis = "6-31g";
		this.kernel.verbose = 0; // 1 tambilkan proses
		int[] simpul = { 1, 2, 1 };
		this.kernel.c_node = simpul;
		this.kernel.URL_ANN = "JQC_data/ann_2_dft";
		this.kernel.Ex = "ANN";
		this.kernel.c_mix = 0.9;
		this.kernel.weight = new double[2][][][];
		init(1);
	}

	public void prepare_mixer(String name) throws ClassNotFoundException {
		main_function kernel = new main_function();
		this.kernel = kernel;
		prepare_dft();
		run_scf(name, "get_input");
		mixer_main mx = new mixer_main();
		double t = mx.get_mix(kernel.input_mixer);
		kernel.opti = t;
	}

	public double run_scf(String name_molecule, String code) throws ClassNotFoundException {
		kernel.URL_int = "JQC_data/int/" + name_molecule + "";
		double r = run(name_molecule, code);
		return r;
	}

	public double run_mixer(double input[]) throws ClassNotFoundException {
		ann_mixer c = new ann_mixer();
		double r = c.f_ANN(input, kernel.c_node, kernel.weight, kernel.thv, kernel.th);
		return r;
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

	public double run(String name_molecule, String code) throws ClassNotFoundException {
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
		if (code.equals("run")) {
			drv.HF_DFT(name_molecule, kernel);
		} else if (code.equals("get_input")) {
			drv.DFT(name_molecule, kernel);
		}
		return drv.input;
	}

}