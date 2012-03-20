package distribution;

import java.io.Serializable;
import java.util.Vector;

public class ZoneDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Vector<GenotypeAgeNumberTrio> genotypeAgeNumberTrio;
	
	public ZoneDistribution() {
		genotypeAgeNumberTrio = new Vector<GenotypeAgeNumberTrio>();
	}
	
	public void addGenotypeDistribution (GenotypeAgeNumberTrio trio) {
		genotypeAgeNumberTrio.add(trio);
	}
	
	public Vector<GenotypeAgeNumberTrio> getGenotypeDistributions(){
		return genotypeAgeNumberTrio;
	}
}
