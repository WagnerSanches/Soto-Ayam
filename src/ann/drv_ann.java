package ann;

import ann.ann;
import function.main_function;

public class drv_ann {

	public double[] Ex(main_function kernel, double input[]) {
		double Ex_ann[] = new double[input.length];
		ann c = new ann();
		for (int i = 0; i < input.length; i++) {
			double []rho= {input[i]};
			Ex_ann[i] = c.f_ANN(rho, kernel.c_node, kernel.weight, kernel.thv, kernel.th);
		}
		return Ex_ann;
	}
}
