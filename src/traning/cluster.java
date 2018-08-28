package traning;

public class cluster {
	public int make_cluster(double t) {
		if (t < 0.35) {
			return 0;
		} else if (t < 0.65) {
			return 1;
		}
		return 2;
	}
}
