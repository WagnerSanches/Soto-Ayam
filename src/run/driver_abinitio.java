package run;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import function.main_function;
import jqc.dft;
import jqc.uhf;
import jqc.uhf_dft;

/**
 *
 * @author Agung Danu Wijaya
 */
public class driver_abinitio {

	public double input;

	public void DFT(String name, main_function kernel) throws ClassNotFoundException {
		dft a = new dft();
		try {
			double r = a.SCF(name, kernel);
			this.input = r;
		} catch (InterruptedException ex) {
		}
	}

	public void HF(String name, main_function kernel) {
		try {
			try {
				uhf HF = new uhf(kernel);
				this.input = HF.SCF(name);
			} catch (ClassNotFoundException ex) {
			}
		} catch (IOException | InterruptedException ex) {
		}
	}
	public void HF_DFT(String name, main_function kernel) {
		try {
			try {
				uhf_dft HF_DFT=new uhf_dft(kernel);
				this.input = HF_DFT.SCF(name);
			} catch (ClassNotFoundException ex) {
			}
		} catch (IOException | InterruptedException ex) {
		}
	}
	public void RHFsetdata(String nama, String tempat, main_function np)
			throws ClassNotFoundException, InterruptedException {
		np.intg.two(nama);
		np.intg.one(nama);
		double S[][] = np.intg.S;
		double T[][] = np.intg.EK;
		double V[][] = np.intg.EV;
		double G[][][][] = np.intg.ints;
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(tempat + "_S"));
			outputStream.writeObject(S);
			outputStream = new ObjectOutputStream(new FileOutputStream(tempat + "_T"));
			outputStream.writeObject(T);
			outputStream = new ObjectOutputStream(new FileOutputStream(tempat + "_V"));
			outputStream.writeObject(V);
			outputStream = new ObjectOutputStream(new FileOutputStream(tempat + "_G"));
			outputStream.writeObject(G);
		} catch (FileNotFoundException ex) {
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

}
