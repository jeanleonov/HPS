package distribution;

import java.io.Serializable;
import java.util.HashMap;

public class GenotypeDistribution implements Serializable {

	private static final long serialVersionUID = 1L;

	HashMap <Integer, Float> ageDistributions;
	
	public GenotypeDistribution() {
		ageDistributions = new HashMap <Integer, Float>();
	}
	
	public void addZoneDistribution (Integer age, Float measure) {
		ageDistributions.put(age,measure);
	}
	
	public HashMap <Integer, Float> getAgeDistributions(){
		return ageDistributions;
	}
}
