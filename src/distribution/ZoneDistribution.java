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

	// by DMY
	public static ZoneDistribution parseZone(String resource) throws NumberFormatException{
		// Later I'm plan to throw my own exception, if it will be necessary 

		ZoneDistribution zone = new ZoneDistribution();
		String[] t = resource.split("\\|");

		for(int i = 0; i < t.length; i += 2){
			try{
				if((t[i] != null) && !(t[i].equals("")))
					zone.addGenotypeDistribution(GenotypeAgeNumberTrio.parseGenotype(t[i]));
			}
			catch(NumberFormatException e){
				throw e;
			}
		}

		return zone;
	}
}