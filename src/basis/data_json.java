/* Source : Modern Quantum Chemistry: Introduction to Advanced Electronic Structure Theory
 * By Attila Szabo, Neil S. Ostlund
 * See section 3.5.1 page 153 
 */
package basis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import function.main_function;

/**
 *
 * @author Agung Danu Wijaya
 */
public class data_json {

	public Map<Integer, basis[]> data;

	public class basis {

		public String a;
		public double[][] b;

		public basis(String a, double[][] b) {
			this.a = a;
			this.b = b;
		}
	}

	public void data_Json_get(int ind, main_function kernel) {
		Map<Integer, basis[]> data = new HashMap<>();
		JSONParser parser = new JSONParser();
		atom_name atom_n = new atom_name();
		String atom[] = atom_n.atom;
		try {
			Object obj = parser.parse(new FileReader("JQC_data/basis/" + kernel.tipebasis + ".json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray data_basis = (JSONArray) jsonObject.get(atom[ind - 1]);
			basis data_slater[] = new basis[data_basis.size()];
			int re = 0;
			Iterator<JSONObject> iterator_data_basis = data_basis.iterator();
			while (iterator_data_basis.hasNext()) {
				JSONObject data_json = iterator_data_basis.next();
				String angular = (String) data_json.get("angular");
				JSONArray prim = (JSONArray) data_json.get("prim");
				Iterator<Double> iterator_data_prim = prim.iterator();
				double data_prim[] = new double[prim.size()];
				int i = 0;
				while (iterator_data_prim.hasNext()) {
					data_prim[i++] = iterator_data_prim.next();
				}
				JSONArray cont = (JSONArray) data_json.get("cont");
				double data_cont[][] = new double[cont.size()][];
				Iterator<JSONArray> iterator_data_cont = cont.iterator();
				int t = 0;
				while (iterator_data_cont.hasNext()) {
					JSONArray data_cont_child = iterator_data_cont.next();
					Iterator<Double> iterator_ata_cont_child = data_cont_child.iterator();
					double data_temp[] = new double[data_cont_child.size()];
					int j = 0;
					while (iterator_ata_cont_child.hasNext()) {
						data_temp[j++] = iterator_ata_cont_child.next();
					}
					data_cont[t++] = data_temp;
				}
				double data8[][] = new double[data_prim.length][];
				for (int j = 0; j < data_prim.length; j++) {
					double rer[] = { data_prim[j], data_cont[0][j] };
					data8[j] = rer;
				}
				data_slater[re++] = new basis(angular.toUpperCase(), data8);
			}
			data.put(ind, data_slater);
		} catch (FileNotFoundException e) {
		} catch (IOException | ParseException e) {
			System.err.println("Error dalam baca file json : Data_json.java");
		}
		this.data = data;
	}
}
