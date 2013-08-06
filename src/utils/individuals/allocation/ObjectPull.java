package utils.individuals.allocation;

import experiment.individual.Female;
import experiment.individual.Male;
import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public class ObjectPull implements IIndividualsManager{

	private Male[] maleStorage;
	private Female[] femaleStorage;
	private int	numberOfMales,
				numberOfFemales;
	
	public ObjectPull(){
		maleStorage = new Male[1];
		femaleStorage = new Female[1];
		numberOfMales=0;
		numberOfFemales=0;
	}
	
	@Override
	public Male getMale(Genotype genotype, int age, Zone zone){
		Male male=null;
		if (numberOfMales==0)
			return new Male(genotype, age, zone);
		male = removeMale();
		return (Male)male.reset(genotype, age, zone);
	}
	
	@Override
	public Female getFemale(Genotype genotype, int age, Zone zone){
		Female female = null;
		if (numberOfFemales==0)
			return new Female(genotype, age, zone);
		female = removeFemale();
		return (Female)female.reset(genotype, age, zone);
	}

	@Override
	public void killMale(Male male) {
		if (maleStorage.length <= numberOfMales)
			increaseMaleStorage();
		maleStorage[numberOfMales++] = male;
	}

	@Override
	public void killFemale(Female female) {
		if (femaleStorage.length <= numberOfFemales)
			increaseFemaleStorage();
		femaleStorage[numberOfFemales++] = female;
	}
	
	Male removeMale(){
		return maleStorage[--numberOfMales];
	}
	
	Female removeFemale(){
		return femaleStorage[--numberOfFemales];
	}
	
	private void increaseMaleStorage(){
		Male[] temp = maleStorage;
		maleStorage = new Male[temp.length*2];
		for(int i=0; i<temp.length; i++)
			maleStorage[i] = temp[i];
	}
	
	private void increaseFemaleStorage(){
		Female[] temp = femaleStorage;
		femaleStorage = new Female[temp.length*2];
		for(int i=0; i<temp.length; i++)
			femaleStorage[i] = temp[i];
	}

	public int getCapacityOfPull() {
		return femaleStorage.length + maleStorage.length;
	}
}
