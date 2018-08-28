/**
 * Setting koordinat atom --> interface
 */
package geo_molecule;

import java.util.HashMap;
import java.util.Map;

import function.main_function;

/**
 *
 * @author Agung Danu Wijaya, Farhamsa D
 */
public class geo {

    public Map<String, data_geo> data;
    main_function kernel;
    public double bondLenght = 1.889725989; // Bond lengths in Bohr (Angstrom)

    public geo(main_function kernel) {
        this.kernel = kernel;
        Map<String, data_geo> data = new HashMap<>();
        this.data = data;
    }

    public double energi(data_geo v) {
        double energi = 0;
        double d1[][] = v.R;
        int c[] = v.numProton;
        for (int i = 0; i < d1.length; i++) {
            for (int j = 0; j < d1.length; j++) {
                if (i != j) {
                    energi += c[i] * c[j] * 1.0 / kernel.d.distance_run(d1[i], d1[j]);
                }
            }
        }
        return (energi / 2.0);
    }

    public int tengah(data_geo v) {
        return (int) kernel.mp.sum(v.numProton) / 2;
    }

}
