package zone;

import genotype.Genome;
import genotype.Genotype;
import individual.Female;
import individual.IIndividualsManager;
import individual.Individual;
import individual.IndividualsManagerDispatcher;
import individual.Male;
import jade.core.AID;
import jade.core.Agent;

import java.util.HashMap;
import java.util.Vector;

import settings.Settings;

import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES = 10;			// improve it (move it to MainClass)
	private static final int DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE = 3;		// improve it (move it to MainClass)
	
	// DMY: for regulating competitiveness factor
	private static double feedingCoeficient = 1;

	private HashMap<Integer, Float> travelCosts;
	
	Vector<Male> males = new Vector<Male>();
	Vector<Female> females = new Vector<Female>();
	Vector<Individual> immatures = new Vector<Individual>();
	Vector<Individual> yearlings = new Vector<Individual>();
	IIndividualsManager individualsManager;
	
	AID statisticDispatcher;
	
	int experimentId;
	int zoneId;
	
	float capacity; 
	double totalCompetitiveness = 0.0001; // DMY: what's this (not mine, but i'm interested in)? 
	int iteration = -1;
	int maxSizeOfListOfFemales, minNumberOfMalesForContinue;
	
	// private Vector<Individual> strangers;
	
	@Override
	protected void setup() {
		travelCosts = Settings.getMovePosibilitiesFrom(this.getZoneNumber());
		
		ZoneDistribution zoneDistribution = (ZoneDistribution)getArguments()[0];
		/*System.out.println("=== " + zoneDistribution);#*/
		experimentId = (Integer)getArguments()[1];
		zoneId = (Integer)getArguments()[2];
		individualsManager = IndividualsManagerDispatcher.getIndividualsManager(zoneId);
		statisticDispatcher = (AID)getArguments()[3];
		if (getArguments().length>4)
			maxSizeOfListOfFemales = (Integer)getArguments()[4];
		else
			maxSizeOfListOfFemales = DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES;
		if (getArguments().length>5)
			minNumberOfMalesForContinue = (Integer)getArguments()[5];
		else
			minNumberOfMalesForContinue = DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE;
		if (getArguments().length>6)
			minNumberOfMalesForContinue = (Integer)getArguments()[6];
		else
			minNumberOfMalesForContinue = DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE;
		createIndividuals(zoneDistribution);
		capacity = zoneDistribution.getResourse();
		addBehaviour(new ZoneBehaviour());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Integer, Float> getZoneTravelPossibilities(){
		if(travelCosts != null){
			return (HashMap<Integer, Float>) travelCosts.clone();
		}
		else return null;
	}
	
	public void createIndividuals(ZoneDistribution zoneDistribution) {
		if (zoneDistribution == null)
			return;
		Vector<GenotypeAgeCountTrio> gants = zoneDistribution.getGenotypeDistributions();
		for (GenotypeAgeCountTrio gant : gants)
			createIndividualsByGant(gant);
	}

	private void createIndividualsByGant(GenotypeAgeCountTrio gant) {
		for (int i = 0; i < gant.getNumber(); i++)
			createIndividual(gant.getGenotype(), gant.getAge());
	}

	private void createIndividual(Genotype genotype, int age) {
		if (genotype.getGender() == Genome.X)
			addIndividualToList(individualsManager.getFemale(genotype, age, this));
		else
			addIndividualToList(individualsManager.getMale(genotype, age, this));
	}

	void createIndividual(String str) {
		String[] strs = str.split(" ");
		Genotype genotype = Genotype.getGenotype(strs[0]);
		int age = Integer.parseInt(strs[1]);
		if (genotype.getGender() == Genome.X)
			addIndividualToList(individualsManager.getFemale(genotype, age, this));
		else
			addIndividualToList(individualsManager.getMale(genotype, age, this));
	}
	
	public void addIndividualToList(Individual individual) {
		if (individual.getAge()==0)
			yearlings.add(individual);
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
		if (individual.isFemale())
			individualsManager.killFemale((Female)individual);
		else
			individualsManager.killMale((Male)individual);
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
	
	public static double getFeedingCoeficient(){
		return feedingCoeficient;
	}
	
	public static void setFeedingCoeficient(double c){
		feedingCoeficient = c;
	}
	
}
