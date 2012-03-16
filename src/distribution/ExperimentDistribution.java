package distribution;

import java.util.Vector;

public class ExperimentDistribution {

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
}
