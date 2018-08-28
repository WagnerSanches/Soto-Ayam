package function;
import ann.drv_ann;
import basis.atom_name;
import basis.data_json;
import basis.data_exponent;
import geo_molecule.distance;
import geo_molecule.geo;
import integral.eri_i;
import integral.eri_i_n;
import integral.electron_density;
import integral.f;
import integral.f_k;
import integral.kinetic_i;
import integral.nai_i;
import integral.nai_i_n;
import integral.normalize;
import jqc.mainintegral;
import jqc.getdata;
import jqc.getpointvalue;
import tools.gev;
import tools.matrix_operation;
/**
 *
 * @author Agung Danu Wijaya
 */
public class main_function {
	//==============================================
	public double input_mixer[];
	// =============================================
	public double num_random=1000;
	public double c_mix=0.9;
	public double iter=300;
	public int one_thread=500;
	public int two_thread=80000;
	public String URL_int;
	public int status_int;
	public String Ex = "LDA";
	public double weight[][][][];
	public double thv[][];
	public double th[];
	public String URL_ANN;
	public int c_node[];
	public String tipebasis = "3-21g";
	public double opti = -1;
	public int num_orbital = 0;
	public int spinup = 0;
	public int spindn = 0;
	public int verbose = 1;
	// =============================================
	public drv_ann ex_ann = new drv_ann();
	public atom_name atom_n = new atom_name();
	public String atom[] = atom_n.atom;
	public special_function specialFunc = new special_function();
	public f_k b = new f_k(this);
	public matrix_operation mp = new matrix_operation();
	public distance d = new distance();
	public f g = new f(this);
	public electron_density rhoe = new electron_density(this); // electron density
	public kinetic_i T = new kinetic_i(this); //
	public nai_i nai = new nai_i(this); // nuclie attraction interaction
	public eri_i eri = new eri_i(this); // electron repultion interaction
	public nai_i_n naiN = new nai_i_n(this); // Numerical nuclie attraction interaction
	public eri_i_n eriN = new eri_i_n(this); // Numerical electron repultion interaction
	public gev gev = new gev(); // General Eigen Value
	public normalize cn = new normalize(this);
	public data_exponent pkt = new data_exponent();
	public geo geo = new geo(this);
	public data_json basisdata_json = new data_json();
	public getdata gdata = new getdata(this);
	public mainintegral intg = new mainintegral(this);
	public getpointvalue gpoint = new getpointvalue(this);
}
