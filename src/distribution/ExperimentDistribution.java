package distribution;

import java.io.Serializable;
import java.util.Vector;

public class ExperimentDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double feedingCoeficient = 1;;
	Vector <ZoneDistribution> zoneDistributions;
	
	public ExperimentDistribution() {
		zoneDistributions = new Vector<ZoneDistribution>();
	}
	
	public void addZoneDistribution (ZoneDistribution distribution) {
		zoneDistributions.add(distribution);
	}
	
	public Vector <ZoneDistribution> getZoneDistributions(){
		return zoneDistributions;
	}
	
	public double getFeedingCoeficient(){
		return feedingCoeficient;
	}
	
	// by DMY
	public static ExperimentDistribution parseExperiment(String resource) throws NumberFormatException{
		// Later I'm plan to throw my own exception, if it will be necessary 
		
		ExperimentDistribution experiment = new ExperimentDistribution();
		
		String[] t = resource.split(";");
		experiment.feedingCoeficient = Double.parseDouble(t[0]);
		for(int i = 1; i < t.length; i++){
			try{
				if((t[i] != null) && !(t[i].equals("")))
					experiment.addZoneDistribution(ZoneDistribution.parseZone(t[i]));
			}
			catch(NumberFormatException e){
				throw e;
			}
		}
		
		return experiment;
	}
}
