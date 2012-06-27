package zone;

import genotype.Genome;
import genotype.Genotype;
import individual.Female;
import individual.Individual;
import individual.Male;
import jade.core.AID;
import jade.core.Agent;

import java.util.Vector;

import distribution.GenotypeAgeNumberTrio;
import distribution.ZoneDistribution;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES = 5;
	
	// DMY: for regulating competitiveness factor in attractivness counting
	private static final double feedingCoeficient = 1;
	
	Vector<Male> males = new Vector<Male>();
	Vector<Female> females = new Vector<Female>();
	Vector<Individual> immatures = new Vector<Individual>();
	Vector<Individual> yearlings = new Vector<Individual>();
	
	AID statisticDispatcher;
	
	int experimentId;
	int zoneId;
	
	int resources;
	double totalCompetitiveness = 0.0001;
	int iteration = -1;
	int maxSizeOfListOfFemales;
	
	// private Vector<Individual> strangers;
	
	@Override
	protected void setup(){
		ZoneDistribution zoneDistribution = (ZoneDistribution)getArguments()[0];
		/*#*/System.out.println("=== " + zoneDistribution);
		experimentId = (Integer)getArguments()[1];
		zoneId = (Integer)getArguments()[2];
		statisticDispatcher = (AID)getArguments()[3];
		if (getArguments().length>4)
			maxSizeOfListOfFemales = (Integer)getArguments()[4];
		else
			maxSizeOfListOfFemales = DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES;
		createIndividuals(zoneDistribution);
		resources = zoneDistribution.getResourse();
		addBehaviour(new ZoneBehaviour());
	}
	
	public void createIndividuals(ZoneDistribution zoneDistribution) {
		if (zoneDistribution == null)
			return;
		Vector<GenotypeAgeNumberTrio> gants = zoneDistribution.getGenotypeDistributions();
		for (GenotypeAgeNumberTrio gant : gants)
			createIndividualsByGant(gant);
	}

	private void createIndividualsByGant(GenotypeAgeNumberTrio gant) {
		for (int i = 0; i < gant.getNumber(); i++)
			createIndividual(gant.getGenotype(), gant.getAge());
	}

	private void createIndividual(Genotype genotype, int age) {
		if (genotype.getGender() == Genome.X)
			addIndividualToList(new Female(genotype, age, this));
		else
			addIndividualToList(new Male(genotype, age, this));
	}

	void createIndividual(String str) {
		String[] strs = str.split(" ");
		Genotype genotype = Genotype.getGenotype(strs[0]);
		int age = Integer.parseInt(strs[1]);
		if (genotype.getGender() == Genome.X)
			addIndividualToList(new Female(genotype, age, this));
		else
			addIndividualToList(new Male(genotype, age, this));
	}
	
	public void addIndividualToList(Individual individual) {
		if (!individual.isMature())
			immatures.add(individual);
		else {
			if (individual.isFemale())
				females.add((Female)individual);
			else
				males.add((Male)individual);
		}
	}
	
	int getIndividualsNumber(){
		return males.size() + females.size() + immatures.size();
	}
	
	public double getAttractivness(){
		// DMY: It's desirable to return value between 0 and 1		
		// DMY: my version, totalCompetitiveness is taken from individuals while feeding
		double attractivness = 0.5; // LAO(temporery)(double)resources / (feedingCoeficient * totalCompetitiveness); 
		// LAO(temporery): if(attractivness > 1)
		// LAO(temporery)	attractivness = 1;
		return attractivness;
	}
	
	Vector<Individual> getIndividuals(){
		Vector<Individual> individuals = new Vector<Individual>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(immatures);
		return individuals;
	}
	
	public void killIndividual(Individual individual) {
		males.remove(individual);
		females.remove(individual);
		immatures.remove(individual);
		yearlings.remove(individual);
	}

	public int getZoneNumber() {
		return zoneId;
	}
	
	public int getMaxSizeOfListOfFemales(){
		return maxSizeOfListOfFemales;
	}
	
	void updateListsAndIndividualSettings(){
		Vector<Individual> individuals = getIndividuals();
		males.clear();
		females.clear();
		immatures.clear();
		yearlings.clear();
		for (Individual indiv : individuals){
			addIndividualToList(indiv);
			indiv.updateSettings();
		}
	}
	
}
