package distribution;

import java.util.HashMap;

public class GenotypeDistribution {

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
