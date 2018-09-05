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
public class jqc_db_save {

	public void save(String name_molecule) throws ClassNotFoundException, InterruptedException {
		main_function kernel = new main_function();
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
		driver_abinitio drv = new driver_abinitio();
		drv.RHFsetdata(name_molecule, kernel.URL_int, kernel);
	}

	public static void main(String args[]) throws ClassNotFoundException, InterruptedException {
		jqc_db_save a = new jqc_db_save();
		String name[] = {"CH3OH","O","H","C","H2", "Li", "LiH", "BeH" ,"O2","CO","OH","CH4","NH","N","NH2","NH3","HF","F","LiF",
				"C2H2","C2H4","C2H6","CN","HCN","HCO","H2CO","N2","N2H4","F2","CO2" ,"F2",
				"CO2","Si","SiH3","SiH4","P","PH2","PH3","CH3SH","S","SH2","Na2","Na","Si2","P2","S2","NaCl","P","S","CH3Cl"};
		for (int i = name.length-1; i < name.length; i++) {
		System.out.println(name[i]);
			a.save(name[i]);
		}
	}
}
