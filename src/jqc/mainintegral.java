package jqc;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import function.main_function;
import geo_molecule.data_geo;
import jqc.getdata.datakHF;

/**
 *
 * @author Agung Danu Wijaya
 */
public class mainintegral {

    int s = 0;
    int ak;
    int akk;
    int ak1;
    int akk1;
    public double ints[][][][];
    public double[][] EV;
    public double[][] EK;
    public double[][] S;
    main_function kernel;

    public mainintegral(main_function kernel) {
        this.kernel = kernel;
    }

    public void one(String nama) throws InterruptedException {
        Counter_one_int con = new Counter_one_int();
        data_geo geo = kernel.geo.data.get(nama);
        double[][] R = geo.R;
        int[] Zp = geo.numProton;
        Map<Integer, datakHF> datahf = kernel.gdata.get(nama);
        getS den = new getS(kernel);
        getT ki = new getT(kernel);
        int N = datahf.size();
        double[][] EV = new double[N][N]; // energi potensial nuclie 3.152
        this.EV = EV;
        double[][] EK = new double[N][N]; // energi Kinetik 3.151
        double[][] S = new double[N][N]; // S --> overlap orthogonal [1,0] 3.136
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < geo.numProton.length; k++) {
                    ak1++;
                    while (Thread.activeCount() > 1000) {
                    }
                    Thread r = new worker_one_int(con, datahf.get(i), datahf.get(j), R[k], i, j, Zp[k]);
                    r.start();
                }
                S[i][j] = den.clc_getS(datahf.get(i), datahf.get(j)); // 3.136
                EK[i][j] = ki.clc_getT(datahf.get(i), datahf.get(j));
            }
        }
        while (ak1 != akk1) {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        this.EK = EK;
        this.S = S;

    }

    public class Counter_two_int {

        public synchronized void input(double R, int i, int j, int k, int l) {
            ints[i][j][k][l] = ints[j][i][k][l] = ints[i][j][l][k] = ints[j][i][l][k] = ints[k][l][i][j] = ints[l][k][i][j] = ints[k][l][j][i] = ints[l][k][j][i] = R;
            akk++;
        }
    }

    public class worker_two_int extends Thread {

        datakHF a, b, c, d;
        int i, j, k, l;
        Counter_two_int e;

        public worker_two_int(Counter_two_int e, datakHF a, datakHF b, datakHF c, datakHF d, int i, int j, int k, int l) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.e = e;
        }

        public void run() {
            getG ER = new getG(kernel);
            double R = ER.clc_getG(a, b, c, d);
            e.input(R, i, j, k, l);
        }
    }

    public class Counter_one_int {

        public synchronized void input(double NAI, int i, int j) {
            EV[i][j] += NAI;
            akk1++;
        }
    }

    public class worker_one_int extends Thread {

        datakHF a, b;
        Counter_one_int e;
        double R[];
        int i, j;
        double atom;

        public worker_one_int(Counter_one_int e, datakHF a, datakHF b, double R[], int i, int j, double atom) {
            this.a = a;
            this.b = b;
            this.R = R;
            this.e = e;
            this.i = i;
            this.j = j;
            this.atom = atom;
        }

        public void run() {
            getV AE = new getV(kernel);
            double NAI = AE.clc_getV(a, b, R) * atom;
            e.input(NAI, i, j);
        }
    }

    public void two(String name) throws InterruptedException {
        double iteration = 0;
        Map<Integer, datakHF> datahf = kernel.gdata.get(name);
        int n = datahf.size();
        double ints[][][][] = new double[n][n][n][n];
        this.ints = ints;
        double cek[][][][] = new double[n][n][n][n];
        Counter_two_int con = new Counter_two_int();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (kernel.verbose == 1) {
                    System.out.println((double) (iteration++ / (n * n)) * 100.0);
                }
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        if (cek[i][j][k][l] == 0) {
                            ak++;
                            while (Thread.activeCount() > 80000) {
                            }
                            Thread r = new worker_two_int(con, datahf.get(i), datahf.get(k), datahf.get(j), datahf.get(l), i, j,
                                    k, l);
                            r.start();
                            cek[i][j][k][l] = cek[j][i][k][l] = cek[i][j][l][k] = cek[j][i][l][k] = cek[k][l][i][j] = cek[l][k][i][j] = cek[k][l][j][i] = cek[l][k][j][i] = 10;
                        }
                    }
                }
            }
        }
        while (ak != akk) {
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
}
