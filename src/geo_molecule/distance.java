/*calculate distance of two position of dot or vector*/
package geo_molecule;
/**
 *
 * @author Agung Danu Wijaya
 */
public class distance {

	public double distance_run(double a[], double b[]) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(sum);
	}

	public double distance_run(double a, double b) {
		return a - b;
	}
}
