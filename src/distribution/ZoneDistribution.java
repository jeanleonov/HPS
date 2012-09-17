package distribution;

import java.io.Serializable;
import java.util.Vector;

public class ZoneDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int resource = 0;
	Vector<GenotypeAgeCountTrio> genotypeAgeNumberTrio;
	
	public ZoneDistribution() {
		genotypeAgeNumberTrio = new Vector<GenotypeAgeCountTrio>();
	}
	
	public void addGenotypeDistribution (GenotypeAgeCountTrio trio) {
		genotypeAgeNumberTrio.add(trio);
	}
	
	public Vector<GenotypeAgeCountTrio> getGenotypeDistributions(){
		return genotypeAgeNumberTrio;
	}
	
	public int getResourse(){
		return resource;
	}
	
	// by DMY
	public static ZoneDistribution parseZone(String resource) throws NumberFormatException{
		// Later I'm plan to throw my own exception, if it will be necessary 
		
		ZoneDistribution zone = new ZoneDistribution();
		String[] t = resource.split("\\|");
		
		zone.resource = Integer.parseInt(t[0]);
		
		for(int i = 1; i < t.length; i++){
			try{
				if((t[i] != null) && !(t[i].equals("")))
					zone.addGenotypeDistribution(GenotypeAgeCountTrio.parseGenotype(t[i]));
			}
			catch(NumberFormatException e){
				throw e;
			}
		}
		
		return zone;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		for (GenotypeAgeCountTrio trio : genotypeAgeNumberTrio)
			str.append(trio.toString() + "; ");
		return str.toString();
	}
}
