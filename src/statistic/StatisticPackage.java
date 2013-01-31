package statistic;

import java.io.Serializable;

public class StatisticPackage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int experimentId;
	private int iteration; // year
	private int subIteration;
	private int zoneId;
	
	private GenotypeAgeDistribution genotypeAgeDistribution;
	
	public StatisticPackage(int experimentId, int zoneId, int iteration, int subIteration, 
							GenotypeAgeDistribution genotypeAgeDistribution) {
		this.experimentId = experimentId;
		this.iteration = iteration;
		this.zoneId = zoneId;
		this.subIteration = subIteration;
		this.genotypeAgeDistribution = genotypeAgeDistribution;
	}
	
	// Test version
	public void print(){
		System.out.println("Statistic package");
		System.out.println("Experiment id " + experimentId);
		System.out.println("Iteration " + iteration);
		System.out.println("Subiteration " + subIteration);
		System.out.println("Zone id " + zoneId);	
		genotypeAgeDistribution.print();
	}
	
	public String toStringMain() {
		return experimentId + ";" + zoneId + ";" + iteration + ";" + subIteration;
	}
	
	public String toString() {
		String header = toStringMain() + ";";
		return genotypeAgeDistribution.toString(header);
	}
}
