package individual;

import genotype.Genome;
import genotype.Genotype;

import java.io.Serializable;
import java.util.ArrayList;

import settings.Settings;
import settings.ViabilityPair;
import settings.Vocabulary;
import zone.Zone;

public class Individual implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected Genotype myGenotype;
	protected int age;
	protected Zone myZone;
	
	protected float curSurvival;
	protected float curCompetitiveness;
	protected float curReproduction;
	protected float curFertility;
	protected float curAmplexusRepeat;
	protected boolean readyToReproduction=true;
	protected ArrayList<ViabilityPair> viabilitySettings;
	
	private SettingsUpdater updater;
	
	public Individual(Genotype myGenotype, int age, Zone myZone) {
		this.myGenotype = myGenotype;
		this.age = age;
		this.myZone = myZone;
		viabilitySettings = Settings.getViabilitySettings(getGenotype());
		updater = new SettingsUpdater();
		updater.updateSettings();
	}
	
	public void updateSettings(){
		readyToReproduction=true;
		updater.updateSettings();
	}
	
	public boolean isDead(){
		double randVal = Math.random();
		if(randVal <= curSurvival)
			return false;
		return true;
	}
	
	public boolean isGoingOut(){
		return Math.random() > myZone.getAttractivness();
	}
	
	public Integer whereDoGo(){
		ArrayList<Float> neighbours = Settings.getMovePosibilitiesFrom(myZone.getZoneNumber());
		double	weightSum = 0, 
				totalWeight = 0, 
				point = Math.random() * weightSum;
		int zoneNumber=0;
		for(Float movePosibility : neighbours)
			weightSum += movePosibility;
		while((point > totalWeight + neighbours.get(zoneNumber)) && zoneNumber < neighbours.size())
			totalWeight += neighbours.get(zoneNumber++);
		return zoneNumber;
	}

	public boolean isFemale(){
		return myGenotype.getGender() == Genome.X;
	}
	
	public boolean isMature(){
		return age >= getSetting(Vocabulary.Param.Spawning);
	}
	
	public boolean isReadyToReproduction(){
		return readyToReproduction;
	}

	public void changeZone(Zone newZone){
		myZone = newZone;
	}
	
	public Genotype getGenotype(){
		return myGenotype;
	}
	
	public int getAge(){
		return age;
	}

	public Zone getMyZone() {
		return myZone;
	}
	
	protected Float getSetting(Vocabulary.Param param) {
		for(ViabilityPair pair : viabilitySettings)
			if(pair.getParam() == param) return pair.getValue();
		return 0f;
	}
	
	private class SettingsUpdater{

		public void updateSettings(){
			if (age == 0)	updateSettingsForYearling();
			else			updateSettingsForNotYearling();
			if (isMature())	updateSettingsForMature();
			else			updateSettingsForImmature();
		}
		
		private void updateSettingsForYearling(){
			curSurvival = getSetting(Vocabulary.Param.SurvivalFactorFirst);
			curCompetitiveness = getSetting(Vocabulary.Param.CompetitivenessFactorFirst);
		}
		
		private void updateSettingsForNotYearling(){
			if (age > getSetting(Vocabulary.Param.Lifetime))
				curSurvival = curCompetitiveness = 0f;
			else{
				float survivalAgeCorrection = pow(getSetting(Vocabulary.Param.SurvivalFactor), age),
					  competitivenessAgeCorrection = pow(getSetting(Vocabulary.Param.CompetitivenessFactor), age);
				curSurvival = getSetting(Vocabulary.Param.Survival)*survivalAgeCorrection;
				curCompetitiveness = getSetting(Vocabulary.Param.Competitiveness)*competitivenessAgeCorrection;
			}
		}
		
		private void updateSettingsForMature(){
			float reprodAgeCorrection = pow(getSetting(Vocabulary.Param.ReproductionFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1)),
				  fertilityAgeCorrection = pow(getSetting(Vocabulary.Param.FertilityFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1)),
				  repeatAgeCorrection = pow(getSetting(Vocabulary.Param.AmplexusRepeatFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1));
			curReproduction = getSetting(Vocabulary.Param.Reproduction)*reprodAgeCorrection;
			curFertility = getSetting(Vocabulary.Param.Fertility)*fertilityAgeCorrection;
			curAmplexusRepeat = getSetting(Vocabulary.Param.AmplexusRepeat)*repeatAgeCorrection;
		}
		
		private void updateSettingsForImmature(){
			curReproduction = 0f;
			curFertility = 0f;
			curAmplexusRepeat = 0f;
		}
		
		private Float pow(Float a, int p){
			Float res;
			for (res=1f; p>0; res*=a, p--);
			return res;
		}
	}
}
