package utils.individuals.allocation;

import experiment.individual.Female;
import experiment.individual.Male;
import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public interface IIndividualsManager {

	public Male getMale(Genotype genotype, int age, Zone zone);
	public Female getFemale(Genotype genotype, int age, Zone zone);
	
	public void killMale(Male male);
	public void killFemale(Female female);
	
	int getCapacityOfPull();
}
