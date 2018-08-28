/* Source : Computational Techniques in Quantum Chemistry
 * and Molecular Physics
 * see equation 3 on page 350
 */
package basis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Agung Danu Wijaya
 */
public class data_exponent {

	public Map<String, double[][]> data;

	public data_exponent() {

		Map<String, double[][]> data = new HashMap<>();
		double[][] S_lmn = { { 0, 0, 0 } };
		data.put("S", S_lmn);

		double[][] P_lmn = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		data.put("P", P_lmn);

		double[][] D_lmn = { { 2, 0, 0 }, { 1, 1, 0 }, { 1, 0, 1 }, { 0, 2, 0 }, { 0, 1, 1 }, { 0, 0, 2 } };
		data.put("D", D_lmn);

		double[][] F_lmn = { { 3, 0, 0 }, { 2, 1, 0 }, { 2, 0, 1 }, { 1, 2, 0 }, { 1, 1, 1 }, { 1, 0, 2 }, { 0, 3, 0 },
				{ 0, 2, 1 }, { 0, 1, 2 }, { 0, 0, 3 } };
		data.put("F", F_lmn);

		this.data = data;

	}

}
