package individual;

import genotype.Genotype;
import settings.Settings;
import zone.Zone;

public class Male extends Individual {

	private static final long serialVersionUID = 1L;
	
	private Female[] femalesList;

	Male(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
		femalesList = new Female[myZone.getMaxSizeOfListOfFemales()];
	}
	
	Male reset(Genotype myGenotype, int age, Zone myZone){
		this.myGenotype = myGenotype;
		this.age = age;
		this.myZone = myZone;
		viabilitySettings = Settings.getViabilitySettings(getGenotype());
		updater.updateSettings();
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
		// TODO !!!!!!!!!!!!!!!!!!!!!
		return super.curSurvival;
	}

	@Override
	public boolean isFemale() {
		return false;
	}
	
	public void die(){
		IndividualsManager.getManager().killMale(this);
	}
}
