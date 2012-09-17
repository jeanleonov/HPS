package statistic;

import java.io.Serializable;

public class StatisticPackage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int experimentId;
	private int zoneId;
	private int iteration; // year
	private GenotypeAgeDistribution genotypeAgeDistribution;
	
	public StatisticPackage(int experimentId, int zoneId, int iteration, 
							GenotypeAgeDistribution genotypeAgeDistribution) {
		this.experimentId = experimentId;
		this.zoneId = zoneId;
		this.iteration = iteration;
		this.genotypeAgeDistribution = genotypeAgeDistribution;
	}
	
	public void print(){
		System.out.println("Statistic package");
		System.out.println("Experiment id " + experimentId);
		System.out.println("Zone id " + zoneId);
		System.out.println("Iteration " + iteration);
		genotypeAgeDistribution.print();
	}
	
	public String toStringMain() {
		return experimentId + ";" + zoneId + ";" + iteration;
	}
	
	public String toString() {
		String header = toStringMain() + ";";
		return genotypeAgeDistribution.toString(header);
	}
}
