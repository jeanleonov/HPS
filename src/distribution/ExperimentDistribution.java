package distribution;

import java.util.ArrayList;
import java.util.List;

public class ExperimentDistribution {
	
	private List<ZoneDistribution> zoneDistributions;
	
	public ExperimentDistribution() {
		zoneDistributions = new ArrayList<ZoneDistribution>();
	}
	
	public void addZoneDistribution (ZoneDistribution distribution) {
		zoneDistributions.add(distribution);
	}
	
	public List<ZoneDistribution> getZoneDistributions() {
		return zoneDistributions;
	}
	
	public static ExperimentDistribution parseExperiment(String resource) throws Exception {
		ExperimentDistribution experimentDistribution = new ExperimentDistribution();
		String[] t = resource.split(";");
		for(int i = 0; i < t.length; i++) {
			if(t[i] != null && !t[i].equals(""))
				experimentDistribution.addZoneDistribution(ZoneDistribution.parseZone(t[i]));
		}
		return experimentDistribution;
	}
}
