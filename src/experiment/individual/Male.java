package experiment.individual;

import java.util.LinkedList;

import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public class Male extends Individual {
	
	private LinkedList<Female> femalesList = null;

	public Male(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
	}
	
	public Male reset(Genotype myGenotype, int age, Zone myZone){
		this.myGenotype = myGenotype;
		this.myZone = myZone;
		viabilitySettings = myZone.getViabilitySettings(getGenotype());
		for (this.age = 0; this.age <= age; this.age++)
			updater.updateSettings();
		this.age = age;
		return this;
	}
	
	// it's for Zone
	public LinkedList<Female> getFemaleListForUpdating() {
		if (femalesList == null)
			femalesList = new LinkedList<>();
		else
			femalesList.clear();
		return femalesList;
	}

	public void chooseFemale() {
		if(femalesList.size()==0)
			return;
		Female myLover = null;
		double attractivnessesSum=0;
		for(Female female : femalesList)
			attractivnessesSum += female.curReproduction;
		double point = Math.random() * attractivnessesSum;
		double curSum = 0;
		for(Female female : femalesList) {
			curSum += female.curReproduction;
			if(point <= curSum + 0.000001) {
				myLover = female;
				break;
			}
		}
		myLover.addLover(this);
		readyToReproduction = (Math.random()<curAmplexusRepeat)?true:false;
	}

	@Override
	public boolean isFemale() {
		return false;
	}
}
