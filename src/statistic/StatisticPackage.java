package statistic;

import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class StatisticPackage {
	
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

	public void writeToSheet(WritableSheet sheet) throws RowsExceededException, WriteException {		
		Number number;
		number = new Number(StatisticDispatcher.EXPERIMENT_ID_POS, StatisticDispatcher.currentRow, experimentId); 
		sheet.addCell(number);	
		number = new Number(StatisticDispatcher.ZONE_ID_POS, StatisticDispatcher.currentRow, zoneId);
		sheet.addCell(number);
		number = new Number(StatisticDispatcher.ITERATION_POS, StatisticDispatcher.currentRow, iteration);
		sheet.addCell(number);
		
		genotypeAgeDistribution.writeToSheet(sheet);	
	}
}
