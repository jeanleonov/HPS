package statistic;

import java.util.Random;


public class Tester {
	private static Random rnd = new Random();

	public static void main(String[] args) {
		StatisticPackage pack1 = createPackage1();
		StatisticPackage pack2 = createPackage2();
		StatisticPackage pack3 = createPackage3();

		/*
		 * pack1.print(); pack2.print(); pack3.print();
		 */

		StatisticDispatcher sd = new StatisticDispatcher();
		sd.addPackage(pack1);
		sd.addPackage(pack2);
		sd.addPackage(pack3);
		sd.exportToExl();
	}

	private static StatisticPackage createPackage1() {
		int experiment = 0;
		int zone = 0;
		int iteration = 0;
		GenotypeAgeDistribution gad = createGAD();
		return new StatisticPackage(experiment, zone, iteration, gad);
	}

	private static StatisticPackage createPackage2() {
		int experiment = 0;
		int zone = 1;
		int iteration = 0;
		GenotypeAgeDistribution gad = createGAD();
		return new StatisticPackage(experiment, zone, iteration, gad);
	}

	private static StatisticPackage createPackage3() {
		int experiment = 0;
		int zone = 2;
		int iteration = 0;
		GenotypeAgeDistribution gad = createGAD();
		return new StatisticPackage(experiment, zone, iteration, gad);
	}

	private static GenotypeAgeDistribution createGAD() {
		try {
			GenotypeAgeDistribution gad;
			gad = new GenotypeAgeDistribution(getIntDistr(10), getIntDistr(10),
					getIntDistr(10));
			return gad;
		} catch (NotSameParametersSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int[] getIntDistr(int n) {
		int[] distr = new int[n];
		for (int i = 0; i < n; i++) {
			distr[i] = Math.abs(rnd.nextInt()) % 10 + 1;
		}
		return distr;
	}
}
