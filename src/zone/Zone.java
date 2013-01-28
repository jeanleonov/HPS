package zone;

import genotype.Genome;
import genotype.Genotype;
import individual.Female;
import individual.Individual;
import individual.Male;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import settings.Settings;
import starter.Shared;
import utils.individuals.allocation.IIndividualsManager;
import utils.individuals.allocation.IndividualsManagerDispatcher;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;
	
	private static double feedingCoeficient;
	private static double capacityMultiplier;
	ArrayList<Male> males = new ArrayList<Male>();
	ArrayList<Female> females = new ArrayList<Female>();
	ArrayList<Individual> immatures = new ArrayList<Individual>();
	ArrayList<Individual> yearlings = new ArrayList<Individual>();
	IIndividualsManager individualsManager;
	
	AID statisticDispatcher;
	
	int experimentId;
	int zoneId;
	
	float capacity;
	private HashMap<Integer, Float> travelCosts;
	int iteration = -1;
	int maxSizeOfListOfFemales, minNumberOfMalesForContinue;
	
	@Override
	protected void setup() {
		travelCosts = Settings.getMovePosibilitiesFrom(this.getZoneNumber());
		ZoneDistribution zoneDistribution = (ZoneDistribution)getArguments()[0];
		experimentId = (Integer)getArguments()[1];
		zoneId = (Integer)getArguments()[2];
		individualsManager = IndividualsManagerDispatcher.getIndividualsManager(zoneId);
		statisticDispatcher = (AID)getArguments()[3];
		if (getArguments().length>4)
			maxSizeOfListOfFemales = (Integer)getArguments()[4];
		else
			maxSizeOfListOfFemales = Shared.DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES;
		if (getArguments().length>5)
			minNumberOfMalesForContinue = (Integer)getArguments()[5];
		else
			minNumberOfMalesForContinue = Shared.DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE;
		if (getArguments().length>6)
			minNumberOfMalesForContinue = (Integer)getArguments()[6];
		else
			minNumberOfMalesForContinue = Shared.DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE;
		createIndividuals(zoneDistribution);
		capacity = (float) (zoneDistribution.getCapacity()*capacityMultiplier);
		addBehaviour(new ZoneBehaviour());
	}
	
	public HashMap<Integer, Float> getZoneTravelPossibilities(){
		if(travelCosts != null)
			return (HashMap<Integer, Float>) travelCosts;
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
		if (!individual.isMature()){
			immatures.add(individual);
			if (individual.getAge()==0)
				yearlings.add(individual);
		}
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
	
	List<Individual> getIndividuals(){
		List<Individual> individuals = new ArrayList<Individual>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(immatures);
		return individuals;
	}
	
	public void killIndividual(Individual individual) {
		if (!males.remove(individual))
			if (!females.remove(individual)){
				immatures.remove(individual);
				yearlings.remove(individual);
			}
		if (individual.isFemale())
			individualsManager.killFemale((Female)individual);
		else
			individualsManager.killMale((Male)individual);
	}
	
	public void killMale(Male male) {
		males.remove(male);
		individualsManager.killMale(male);
	}
	
	public void killFemale(Female female) {
		females.remove(female);
		individualsManager.killFemale(female);
	}
	
	public void killImmature(Individual individual) {
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
		int numberOfMalesBefore = males.size(),
		numberOfFemalesBefore = females.size(),
		numberOfImmaturesBefore = immatures.size();
		for (Individual indiv : males)
			indiv.updateSettings();
		for (Individual indiv : females)
			indiv.updateSettings();
		ArrayList<Individual> immaturesToDelete = new ArrayList<Individual>();
		for (Individual indiv : immatures){
			indiv.updateSettings();
			if (indiv.getAge() == 1)
				yearlings.remove(indiv);
			if (indiv.isMature()){
				immaturesToDelete.add(indiv);
				if (indiv.isFemale())
					females.add((Female)indiv);
				else
					males.add((Male)indiv);
			}
		}
		for (Individual indiv : immaturesToDelete)
			immatures.remove(indiv);
		int numberOfMalesAfter = males.size(),
		numberOfFemalesAfter = females.size(),
		numberOfImmaturesAfter = immatures.size();
		if (numberOfMalesBefore>numberOfMalesAfter || numberOfFemalesBefore>numberOfFemalesAfter)
			return;
		return;
	}
	
	public static double getFeedingCoeficient(){
		return feedingCoeficient;
	}
	
	public static void setFeedingCoeficient(double c){
		feedingCoeficient = c;
	}
	
	public static void setCapacityMultiplier(double multiplier){
		capacityMultiplier = multiplier;
	}
	
}
