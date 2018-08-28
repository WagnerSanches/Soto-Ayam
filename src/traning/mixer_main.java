package traning;

public class mixer_main {
	public static void main(String url, int dat[]) throws ClassNotFoundException {
		mixer a = new mixer();
		a.url = url;
		data b = new data();
		a.input = new double[dat.length][];
		a.re = new double[dat.length][];
		a.name = new String[dat.length];
		for (int i = 0; i < dat.length; i++) {
			a.input[i] = b.input[dat[i]];
			a.re[i] = b.out[dat[i]];
			a.name[i] = b.name[dat[i]];
		}
		a.run_train();
		// a.test_all();
		// a.test_range();
	}

	public double get_mix(double input[]) throws ClassNotFoundException {
		mixer a = new mixer();
		String url[] = { "JQC_data/ann_2_mixer_2_0", "JQC_data/ann_2_mixer_2_1", "JQC_data/ann_2_mixer_2_2",
				"JQC_data/ann_2_mixer_2" };
		a.url = url[3];
		int g = a.get_one(input);
		a.url = url[g];
		double mix = a.get_mix(input);
		return mix;
	}

	public static void main(String args[]) throws ClassNotFoundException {
		int dat_2[] = { 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 35 };
		int dat_1[] = { 8, 9, 10, 11, 12, 13, 14, 15, 16, 30 };
		int dat_0[] = { 0, 1, 2, 3, 4, 5, 6, 7, 31, 32, 33, 34, 37, 38, 36 };// 36, 37, 38
		int dat[] = { 22, 9, 8, 6, 3, 16, 18, 7, 1, 4, 10, 13, 12, 19, 11, 27, 23, 2, 0, 5, 21, 25, 26, 14, };
		String url[] = { "JQC_data/ann_2_mixer_2_0", "JQC_data/ann_2_mixer_2_1", "JQC_data/ann_2_mixer_2_2",
				"JQC_data/ann_2_mixer_2" };
		main(url[0], dat_0);
	}
}
