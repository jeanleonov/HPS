package distribution;

import genotype.Genotype;

import java.io.Serializable;
import java.util.HashMap;

public class ZoneDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	HashMap <Genotype, GenotypeDistribution> genotypeDistributions;
	
	public ZoneDistribution() {
		genotypeDistributions = new HashMap <Genotype, GenotypeDistribution>();
	}
	
	public void addGenotypeDistribution (Genotype genotype, GenotypeDistribution distribution) {
		genotypeDistributions.put(genotype,distribution);
	}
	
	public HashMap <Genotype, GenotypeDistribution> getGenotypeDistributions(){
		return genotypeDistributions;
	}
}
