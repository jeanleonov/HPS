package experiment.individual;

import java.util.LinkedList;
import java.util.List;

import settings.PosterityResultPair;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;
import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public class Female extends Individual {
	
	private LinkedList<Male> lovers = null;
	
	public Female(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
	}
	
	public Female reset(Genotype myGenotype, int age, Zone myZone){
		this.myGenotype = myGenotype;
		this.age = age;
		this.myZone = myZone;
		viabilitySettings = myZone.getViabilitySettings(getGenotype());
		for (this.age = 0; this.age <= age; this.age++)
			updater.updateSettings();
		this.age = age;
		return this;
	}

	public ZoneDistribution getPosterity() {
		if(lovers == null || lovers.size()==0)
			return null;
		Male myLover = chooseLover();
		lovers.clear();
		readyToReproduction = false;
		return createPosterityWith(myLover);
	}
	
	private Male chooseLover() {
		Male myLover = null;
		double attractivnessesSum=0;
		for (Male lover : lovers)
			attractivnessesSum += lover.curReproduction;
		double point = Math.random() * attractivnessesSum;
		double curSum=0;
		for(Male lover : lovers) {
			curSum += lover.curReproduction;
			if (point <= curSum + 0.000001) {
				myLover = lover;
				break;
			}
		}
		return myLover;
	}
	
	public ZoneDistribution createPosterityWith(Male male){
		ZoneDistribution posterity = new ZoneDistribution();
		int posteritySize = (int)(male.curFertility * curFertility);
		List<PosterityResultPair> resultsInterbreeding = myZone.getPosteritySettings(myGenotype, male.myGenotype);
		if (resultsInterbreeding != null)
			for(PosterityResultPair pair : resultsInterbreeding) {
				Genotype genotype = pair.getGenotype();
				int age = 0;
				int size = (int) (posteritySize*pair.getProbability());
				GenotypeAgeCountTrio genotypeDistribution;
				genotypeDistribution = new GenotypeAgeCountTrio(genotype, age, size);
				posterity.addGenotypeDistribution(genotypeDistribution);
			}
		return posterity;
	}
	
	void addLover(Male male){
		if (lovers == null)
			lovers = new LinkedList<Male>();
		lovers.add(male);
	}

	@Override
	public boolean isFemale() {
		return true;
	}
}
		
