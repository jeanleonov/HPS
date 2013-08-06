package experiment.individual;

import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;
import settings.Settings;

public class Male extends Individual {

	private static final long serialVersionUID = 1L;
	
	private Female[] femalesList;

	public Male(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
		femalesList = new Female[myZone.getMaxSizeOfListOfFemales()];
	}
	
	public Male reset(Genotype myGenotype, int age, Zone myZone){
		this.myGenotype = myGenotype;
		this.myZone = myZone;
		viabilitySettings = Settings.getViabilitySettings(getGenotype());
		for (this.age = 0; this.age <= age; this.age++)
			updater.updateSettings();
		this.age = age;
		return this;
	}
	
	// it's for Zone
	public Female[] getFemaleListForUpdating(){
		return femalesList;
	}

	public void chooseFemale() {
		if(femalesList.length==0 || femalesList[0]==null || Math.random()>=curReproduction)
			return;
		int femaleNumber;
		double attractivnessesSum=0, point = Math.random(), curSum=femalesList[0].getAttractivness();
		for (femaleNumber=0; femaleNumber<femalesList.length && femalesList[femaleNumber]!=null; femaleNumber++)
			attractivnessesSum += femalesList[femaleNumber].getAttractivness();
		point *= attractivnessesSum;
		for(femaleNumber=0; curSum<point /*#re TODO*/ && femaleNumber<femalesList.length-1; curSum+=femalesList[femaleNumber].getAttractivness(), femaleNumber++);
		femalesList[femaleNumber].addLover(this);
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
