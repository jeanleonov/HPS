package experiment.individual;

import java.util.ArrayList;

import settings.PosterityResultPair;
import settings.Settings;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;
import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public class Female extends Individual {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Male> lovers;
	
	public Female(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
		lovers = new ArrayList<Male>();
	}
	
	public Female reset(Genotype myGenotype, int age, Zone myZone){
		this.myGenotype = myGenotype;
		this.age = age;
		this.myZone = myZone;
		viabilitySettings = Settings.getViabilitySettings(getGenotype());
		for (this.age = 0; this.age <= age; this.age++)
			updater.updateSettings();
		this.age = age;
		return this;
	}

	public ZoneDistribution getPosterity() {
		if(lovers.size()==0 || Math.random()>=curReproduction)
			return null;
		int maleNumber;
		Male male;
		double attractivnessesSum=0, point = Math.random(), curSum=lovers.get(0).getAttractivness();
		for (maleNumber=0; maleNumber<lovers.size(); maleNumber++)
			attractivnessesSum += lovers.get(maleNumber).getAttractivness();
		point *= attractivnessesSum;
		for(maleNumber=0; point > curSum /*#re TODO*/ && maleNumber<lovers.size()-1; curSum+=lovers.get(maleNumber).getAttractivness(), maleNumber++);
		male = lovers.get(maleNumber); 
		lovers.clear();
		readyToReproduction = false;
		return createPosterityWith(male);
	}
	
	ZoneDistribution createPosterityWith(Male male){
		ZoneDistribution posterity = new ZoneDistribution();
		int posteritySize = (int)(male.curFertility * curFertility);
		ArrayList<PosterityResultPair> resultsInterbreeding = Settings.getPosteritySettings(myGenotype, male.myGenotype);
		if (resultsInterbreeding != null)
			for(PosterityResultPair pair : resultsInterbreeding)
				posterity.addGenotypeDistribution(new GenotypeAgeCountTrio(pair.getGenotype(), 0, (int) (posteritySize*pair.getProbability())));
		return posterity;
	}
	
	double getAttractivness(){
		return 0.5;			//#Stub
	}
	
	void addLover(Male male){
		lovers.add(male);
	}

	@Override
	public boolean isFemale() {
		return true;
	}
}
		
