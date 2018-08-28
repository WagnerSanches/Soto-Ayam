package tools;

/**
 *
 * @author Agung Danu Wijaya
 */
public class matrix_operation {

	public double[][] mdot(double a[][], double b) {
		double c[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[i][j] = a[i][j] * b;
			}
		}
		return c;
	}

	public double[] mdot(double a[], double b) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = a[j] * b;
		}
		return c;
	}

	public double[] mdot(double a[], double b[]) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = a[j] * b[j];
		}
		return c;
	}

	public double[][] mdot(double a[][], double b[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[j].length; i++) {
				c[j][i] = a[j][i] * b[j][i];
			}
		}
		return c;
	}

	public double[][] copy(double a[][]) {
		double b[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[i][j] = a[i][j];
			}
		}
		return b;
	}

	public double[][][] copy(double a[][][]) {
		double b[][][] = new double[a.length][a[0].length][a[0][0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				for (int k = 0; k < a[0][0].length; k++) {
					b[i][j][k] = a[i][j][k];
				}
			}
		}
		return b;
	}

	public double[] copy(double a[]) {
		double b[] = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i];
		}
		return b;
	}

	public double[][] adddot(double a[][], double b) {
		double c[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[i][j] = a[i][j] + b;
			}
		}
		return c;
	}

	public double[] adddot(double a[], double b) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = a[j] + b;
		}
		return c;
	}

	public double[] adddot(double a[], double b[]) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = a[j] + b[j];
		}
		return c;
	}
	public double[] mindotabs(double a[], double b[]) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = Math.abs(a[j] - b[j]);
		}
		return c;
	}
	public double[][] adddot(double a[][], double b[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[0].length; i++) {
				c[j][i] = a[j][i] + b[j][i];
			}
		}
		return c;
	}

	public double[] adddotabs(double a[], double b[]) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = Math.abs(a[j] + b[j]);
		}
		return c;
	}

	public double[] adddotabs(double a[][], double b[][]) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[j].length; i++) {
				c[j] = Math.abs(a[j][i] + b[j][i]);
			}

		}
		return c;
	}

	public double[][] powdot(double a[][], double b) {
		double c[][] = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[i][j] = Math.pow(a[i][j], b);
			}
		}
		return c;
	}

	public double[] powdot(double a[], double b) {
		double c[] = new double[a.length];
		for (int j = 0; j < a.length; j++) {
			c[j] = Math.pow(a[j], b);
		}
		return c;
	}

	public void disp(double a[][]) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.format(a[i][j] + " ");
			}
			System.out.println("");
		}
	}

	public void disp(double a[]) {
		for (int j = 0; j < a.length; j++) {
			System.out.format(a[j] + " ");
		}
		System.out.println("");
	}

	public void disp(String a[]) {
		for (int j = 0; j < a.length; j++) {
			System.out.format(a[j] + " ");
		}
		System.out.println("");
	}

	public void dispa(double a[]) {
		System.out.print("{");
		for (int j = 0; j < a.length-1; j++) {
			System.out.format(a[j] + " , ");
		}
		System.out.println(a[a.length-1] + " },");
	}

	public String geta(double a[]) {
		String h = "";
		for (int j = 0; j < a.length; j++) {
			h += ("'" + a[j] + "' , ");
		}
		return h;
	}

	public void disp(int a[]) {
		for (int j = 0; j < a.length; j++) {
			System.out.format(a[j] + " ");
		}
		System.out.println("");
	}

	public void disp(String a) {
		System.out.println(a);
	}

	public double sum(double a[]) {
		double r = 0;
		for (int j = 0; j < a.length; j++) {
			r += a[j];
		}
		return r;
	}

	public double sum(int a[]) {
		double r = 0;
		for (int j = 0; j < a.length; j++) {
			r += a[j];
		}
		return r;
	}

	public double sum(double a[][]) {
		double r = 0;
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[j].length; i++) {
				r += a[j][i];
			}
		}
		return r;
	}

	public double sumabs(double a[][]) {
		double r = 0;
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[j].length; i++) {
				r += Math.abs(a[j][i]);
			}
		}
		return r;
	}
}
