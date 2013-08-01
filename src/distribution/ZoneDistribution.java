package distribution;

import java.util.ArrayList;
import java.util.List;

public class ZoneDistribution {
	
	private float capacity = 0;
	private List<GenotypeAgeCountTrio> genotypeAgeNumberTrio;
	
	public ZoneDistribution() {
		genotypeAgeNumberTrio = new ArrayList<GenotypeAgeCountTrio>();
	}
	
	public void addGenotypeDistribution (GenotypeAgeCountTrio trio) {
		genotypeAgeNumberTrio.add(trio);
	}
	
	public List<GenotypeAgeCountTrio> getGenotypeDistributions() {
		return genotypeAgeNumberTrio;
	}
	
	public float getCapacity(){
		return capacity;
	}
	
	public static ZoneDistribution parseZone(String resource) throws NumberFormatException {
		ZoneDistribution zoneDistribution = new ZoneDistribution();
		String[] t = resource.split("\\|");
		zoneDistribution.capacity = Integer.parseInt(t[0]);
		for(int i = 1; i < t.length; i++){
			if((t[i] != null) && !(t[i].equals("")))
				zoneDistribution.addGenotypeDistribution(GenotypeAgeCountTrio.parseGenotype(t[i]));
		}
		return zoneDistribution;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		for (GenotypeAgeCountTrio trio : genotypeAgeNumberTrio)
			str.append(trio.toString() + "; ");
		return str.toString();
	}
}
