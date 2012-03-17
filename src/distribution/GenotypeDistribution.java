package distribution;

import java.io.Serializable;
import java.util.HashMap;

public class GenotypeDistribution implements Serializable {

	private static final long serialVersionUID = 1L;

	HashMap <Integer, Integer> ageDistributions;
	
	public GenotypeDistribution() {
		ageDistributions = new HashMap <Integer, Integer>();
	}
	
	public void addZoneDistribution (Integer age, Integer count) {
		ageDistributions.put(age,count);
	}
	
	public HashMap <Integer, Integer> getAgeDistributions(){
		return ageDistributions;
	}
}
