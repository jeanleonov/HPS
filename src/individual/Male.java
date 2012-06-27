package individual;

import genotype.Genotype;
import zone.Zone;

public class Male extends Individual {

	private static final long serialVersionUID = 1L;
	
	private Female[] femalesList;

	public Male(Genotype myGenotype, int age, Zone myZone) {
		super(myGenotype, age, myZone);
		femalesList = new Female[myZone.getMaxSizeOfListOfFemales()];
	}
	
	// it's for Zone
	public Female[] getFemaleListForUpdating(){
		return femalesList;
	}

	public void chooseFemale() {
		if(femalesList.length==0 || femalesList[0]==null)
			return;
		int femaleNumber;
		double attractivnessesSum=0, point = Math.random(), curSum=femalesList[0].getAttractivness();
		for (femaleNumber=0; femaleNumber<femalesList.length && femalesList[femaleNumber]!=null; femaleNumber++)
			attractivnessesSum += femalesList[femaleNumber].getAttractivness();
		point *= attractivnessesSum;
		for(femaleNumber=0; point > curSum; curSum+=femalesList[femaleNumber].getAttractivness(), femaleNumber++);
		femalesList[femaleNumber].addLover(this);
		readyToReproduction = false;
	}
	
	double getAttractivness(){
		// TODO !!!!!!!!!!!!!!!!!!!!!
		return Math.random();
	}
}
