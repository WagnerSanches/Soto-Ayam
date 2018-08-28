/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import defacom.db_cmd;
import function.main_function;
import geo_molecule.data_geo;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Agung Danu Wijaya
 */
public class jqc_db_main {

	public double run(String name_molecule, main_function kernel) throws ClassNotFoundException {

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
		kernel.tipebasis = "6-31g";
		kernel.verbose = 0;
		kernel.URL_int = "JQC_data/int/" + name_molecule + "";
		kernel.status_int = 0;
		driver_abinitio drv = new driver_abinitio();
		drv.HF(name_molecule, kernel);
		return drv.input;
	}

	public static void main(String args[]) throws ClassNotFoundException {
		jqc_db_main h = new jqc_db_main();
		main_function kernel = new main_function();
		kernel.c_mix = 0.9;
		String name[] = { "OH", "LiH", "Cl2", "H2O2", "CH3OH", "CH3", "CH", "H2O", "HCl", "H2", "O2", "CO", "Li2",
				"CH4", "NH", "NH2", "NH3", "HF", "BeH", "LiF", "C2H2", "C2H4", "C2H6", "CN", "HCN", "HCO", "H2CO", "N2",
				"N2H4", "F2", "CO2", "SiH3", "H", "H2", "O", "C", "Li", "F", "N", "Si", "S", "Be", "P" };
		for (String string : name) {
			double t = h.run(string, kernel);
			System.out.println(string + " , " + t);
		}
	}
}
