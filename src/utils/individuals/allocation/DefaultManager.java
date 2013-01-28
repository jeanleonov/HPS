package utils.individuals.allocation;

import individual.Female;
import individual.Male;
import genotype.Genotype;
import zone.Zone;

public class DefaultManager implements IIndividualsManager {
	
	@Override
	public Male getMale(Genotype genotype, int age, Zone zone) {
		return new Male(genotype,age,zone);
	}

	@Override
	public Female getFemale(Genotype genotype, int age, Zone zone) {
		return new Female(genotype,age,zone);
	}

	@Override
	public void killMale(Male male) {
	}

	@Override
	public void killFemale(Female female) {
	}
	
	@Override
	public int getCapacityOfPull() {
		return -1;
	}
}
