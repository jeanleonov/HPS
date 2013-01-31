package individual;

import genotype.Genotype;

import java.util.ArrayList;

import settings.PosterityResultPair;
import settings.Settings;
import settings.ViabilityPair;
import settings.Vocabulary;
import starter.Shared;
import zone.Zone;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;

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
		updater.updateSettings();
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
		int posteritySize = (int)(male.curFertility * curFertility * Shared.POSTERITY_SIZE_MULTUPLIER);			// TODO delete this shit
		ArrayList<PosterityResultPair> resultsInterbreeding = Settings.getPosteritySettings(myGenotype, male.myGenotype);
		if (resultsInterbreeding != null)
			for(PosterityResultPair pair : resultsInterbreeding)
				posterity.addGenotypeDistribution(
						new GenotypeAgeCountTrio(
							pair.getGenotype(),
							0,
							(int) (posteritySize*pair.getProbability()
							/**getSetting(Settings.getViabilitySettings(pair.getGenotype()), Param.SurvivalFactorFirst)*/)));
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
	
	protected Float getSetting(ArrayList<ViabilityPair> viabilitySettings, Vocabulary.Param param) {
		for(ViabilityPair pair : viabilitySettings)
			if(pair.getParam() == param)
				return pair.getValue();
		return 0f;
	}
}
		
