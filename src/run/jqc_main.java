/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import function.main_function;
import geo_molecule.data_geo;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Agung Danu Wijaya
 */
public class jqc_main {

	public void run() throws ClassNotFoundException {
		Date date_start = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
		String start = ft.format(date_start);
		main_function kernel = new main_function();
		driver_abinitio drv = new driver_abinitio();
		String name_molecule = "C";
		double[][] R = { { 0.00000000, 0.00000000, 0.04851804 } };
		int numE[] = { 6 };
		String atom[] = new String[numE.length];
		for (int i = 0; i < atom.length; i++) {
			atom[i] = kernel.atom[numE[i] - 1];
		}
		data_geo mole = new data_geo(numE, atom, kernel.mp.mdot(R, kernel.geo.bondLenght));
		kernel.geo.data.put(name_molecule, mole);
		kernel.tipebasis = "6-31g";
		kernel.verbose = 0;
		kernel.spindn = 3;
		kernel.spinup = 3;
		kernel.status_int=1;
		drv.DFT(name_molecule, kernel);
		System.out.println(drv.input);
		Date date_stop = new Date();
		String stop = ft.format(date_stop);
		System.out.println("Waktu awal : " + start + " Waktu awal : " + stop);
	}

	public static void main(String args[]) throws ClassNotFoundException {
		jqc_main a = new jqc_main();
		a.run();
	}
}
