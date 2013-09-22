package distribution;

import java.util.ArrayList;
import java.util.List;

public class ZoneDistribution {
	
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
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		for (GenotypeAgeCountTrio trio : genotypeAgeNumberTrio)
			str.append(trio.toString() + "; ");
		return str.toString();
	}
}
