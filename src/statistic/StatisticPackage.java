package statistic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import jxl.write.WriteException;

public class StatisticPackage implements Serializable {
	
	private int experimentId;
	private int iteration; // year
	private int zoneId;
	
	private GenotypeAgeDistribution genotypeAgeDistribution;
	
	public StatisticPackage(int experimentId, int zoneId, int iteration, 
							GenotypeAgeDistribution genotypeAgeDistribution) {
		this.experimentId = experimentId;
		this.iteration = iteration;
		this.zoneId = zoneId;
		
		this.genotypeAgeDistribution = genotypeAgeDistribution;
	}
	
	// Test version
	public void print(){
		System.out.println("Statistic package");
		System.out.println("Experiment id " + experimentId);
		System.out.println("Iteration " + iteration);
		System.out.println("Zone id " + zoneId);	
		genotypeAgeDistribution.print();
	}
	
	public void writeToFile(BufferedWriter bw) throws WriteException, IOException {		
		String header = String.valueOf(experimentId) +";" +
				     	String.valueOf(zoneId) + ";" +
				     	String.valueOf(iteration) + ";";
		genotypeAgeDistribution.writeToFile(bw, header);	
	}
}
