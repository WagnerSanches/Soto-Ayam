package function;

/**
 * Robert Sedgewick and Kevin Wayne Agung Danu Wijaya
 */
public class special_function {
	/*
	 * Error Function :
	 * https://introcs.cs.princeton.edu/java/21function/ErrorFunction.java.html
	 */
	public double erf(double z) {
		double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

		// use Horner's method
		double ans = 1 - t * Math.exp(-z * z - 1.26551223
				+ t * (1.00002368 + t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 + t * (0.27886807
						+ t * (-1.13520398 + t * (1.48851587 + t * (-0.82215223 + t * (0.17087277))))))))));
		if (z >= 0) {
			return ans;
		} else {
			return -ans;
		}
	}

	/* gamma functions */
	public double gamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1) + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
				+ 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
		tmp = tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
		return Math.exp(tmp);
	}

	/* factorial */
	public double fac(double x) throws ArithmeticException {
		double r = 1;
		for (int i = 1; i <= x; i++)
			r = r * i;
		return r;
	}

}
