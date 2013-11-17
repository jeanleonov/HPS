package experiment.individual;

import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public class Male extends Individual {
	
	private Female[] femalesList = null;

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
	public Female[] getFemaleListForUpdating() {
		if (femalesList == null)
			femalesList = new Female[myZone.getMaxSizeOfListOfFemales()];
		return femalesList;
	}

	public void chooseFemale() {
		if(femalesList.length==0 || femalesList[0]==null || Math.random()>=curReproduction)
			return;
		Female myLover = null;
		double attractivnessesSum=0;
		for(Female female : femalesList) {
			if (female == null)
				break;
			attractivnessesSum += female.getAttractivness();
		}
		double point = Math.random() * attractivnessesSum;
		double curSum = 0;
		for(Female female : femalesList)
			if (female != null) {
				curSum += female.getAttractivness();
				if(point <= curSum + 0.000001) {
					myLover = female;
					break;
				}
			}
			else {
				myLover = female;
				break;
			}
		myLover.addLover(this);
		readyToReproduction = (Math.random()<curAmplexusRepeat)?true:false;
	}
	
	double getAttractivness(){
		return 0.5;			//#Stub
	}

	@Override
	public boolean isFemale() {
		return false;
	}
}
