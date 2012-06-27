package individual;

import genotype.Genotype;

import java.util.ArrayList;

import settings.PosterityResultPair;
import settings.Settings;
import zone.Zone;
import distribution.GenotypeAgeNumberTrio;
import distribution.ZoneDistribution;

public class Female extends Individual {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Male> lovers;
	
	public Female(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
		lovers = new ArrayList<Male>();
	}

	public ZoneDistribution getPosterity() {
		if(lovers.size()==0)
			return null;
		int maleNumber;
		Male male;
		double attractivnessesSum=0, point = Math.random(), curSum=lovers.get(0).getAttractivness();
		for (maleNumber=0; maleNumber<lovers.size(); maleNumber++)
			attractivnessesSum += lovers.get(maleNumber).getAttractivness();
		point *= attractivnessesSum;
		for(maleNumber=0; point > curSum; curSum+=lovers.get(maleNumber).getAttractivness(), maleNumber++);
		male = lovers.get(maleNumber); 
		lovers.clear();
		readyToReproduction = false;
		return createPosterityWith(male);
	}
	
	ZoneDistribution createPosterityWith(Male male){
		ZoneDistribution posterity = new ZoneDistribution();
		int posteritySize = (int)(male.curFertility * curFertility);
		ArrayList<PosterityResultPair> resultsInterbreeding = Settings.getPosteritySettings(myGenotype, male.myGenotype);		
		for(PosterityResultPair pair : resultsInterbreeding)
			posterity.addGenotypeDistribution(new GenotypeAgeNumberTrio(pair.getGenotype(), 0, (int) (posteritySize*pair.getProbability())));
		return posterity;
	}
	
	double getAttractivness(){
		// TODO !!!!!!!!!!!!!!!!!!!!!
		return Math.random();
	}
	
	void addLover(Male male){
		lovers.add(male);
	}
}
		
