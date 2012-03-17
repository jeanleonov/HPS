package distribution;

import java.io.Serializable;
import java.util.Vector;

public class ExperimentDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
