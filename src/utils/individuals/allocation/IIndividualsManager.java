package utils.individuals.allocation;

import individual.Female;
import individual.Male;
import genotype.Genotype;
import zone.Zone;

public interface IIndividualsManager {

	public Male getMale(Genotype genotype, int age, Zone zone);
	public Female getFemale(Genotype genotype, int age, Zone zone);
	
	public void killMale(Male male);
	public void killFemale(Female female);
}
