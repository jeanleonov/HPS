package individual;

import genotype.Genotype;

import java.io.Serializable;
import java.util.ArrayList;

import settings.Settings;
import settings.ViabilityPair;
import settings.Vocabulary;
import zone.Zone;

public abstract class Individual implements Serializable{
	
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
	
	public void setZone(Zone newZone){
		myZone = newZone;
	}
	
	public void updateSettings(){
		age++;
		readyToReproduction=true;
		updater.updateSettings();
	}
	
	public boolean isDead(){
		double randVal = Math.random();
		if(randVal <= curSurvival * curCompetitiveness)			//# temporery
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
				point = Math.random();
		int zoneNumber=0;
		for(Float movePosibility : neighbours)
			weightSum += movePosibility;
		point *=  weightSum;
		while((point > totalWeight + neighbours.get(zoneNumber)) && zoneNumber < neighbours.size())
			totalWeight += neighbours.get(zoneNumber++);
		return zoneNumber;
	}

	public abstract boolean isFemale();
	
	public boolean isMature(){
		return age >= getSetting(Vocabulary.Param.Spawning);
	}
	
	public boolean isReadyToReproduction(){
		return readyToReproduction;
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
			curSurvival = getSetting(Vocabulary.Param.SurvivalFactorFirst)*0.1f;			//# 0.1 is TEMPORERY!!!!!
			curCompetitiveness = getSetting(Vocabulary.Param.CompetitivenessFactorFirst);
		}
		
		private void updateSettingsForNotYearling(){
			if (age > getSetting(Vocabulary.Param.Lifetime))
				curSurvival = curCompetitiveness = 0f;
			else{
				float survivalAgeCorrection = pow(1+getSetting(Vocabulary.Param.SurvivalFactor), age),
					  competitivenessAgeCorrection = pow(1+getSetting(Vocabulary.Param.CompetitivenessFactor), age);
				curSurvival = getSetting(Vocabulary.Param.Survival)*survivalAgeCorrection;
				curCompetitiveness = getSetting(Vocabulary.Param.Competitiveness)*competitivenessAgeCorrection;
			}
		}
		
		private void updateSettingsForMature(){
			float reprodAgeCorrection = pow(1+getSetting(Vocabulary.Param.ReproductionFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1)),
				  fertilityAgeCorrection = pow(1+getSetting(Vocabulary.Param.FertilityFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1)),
				  repeatAgeCorrection = pow(1+getSetting(Vocabulary.Param.AmplexusRepeatFactor), (int)(age-getSetting(Vocabulary.Param.Spawning)+1));
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
	
	public String toString(){
		return myGenotype.toString() + " " + age;
	}
}
