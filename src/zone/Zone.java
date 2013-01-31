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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import settings.Settings;
import starter.Shared;
import utils.individuals.allocation.IIndividualsManager;
import utils.individuals.allocation.IndividualsManagerDispatcher;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;
	
	private static double capacityMultiplier;
	List<Male> males = new LinkedList<Male>();
	List<Female> females = new LinkedList<Female>();
	List<Individual> otherImmatures = new LinkedList<Individual>();
	List<Individual> yearlings = new LinkedList<Individual>();
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
		if (!individual.isMature())
			if (individual.getAge()==0)
				yearlings.add(individual);
			else
				otherImmatures.add(individual);
		else
			if (individual.isFemale())
				females.add((Female)individual);
			else
				males.add((Male)individual);
	}
	
	public void createYearlings(ZoneDistribution zoneDistribution) {
		if (zoneDistribution == null)
			return;
		List<GenotypeAgeCountTrio> gants = zoneDistribution.getGenotypeDistributions();
		for (GenotypeAgeCountTrio gant : gants)
			createYearlingsByGant(gant);
	}

	private void createYearlingsByGant(GenotypeAgeCountTrio gant) {
		for (int i = 0; i < gant.getNumber(); i++)
			createYearling(gant.getGenotype());
	}

	private void createYearling(Genotype genotype) {
		if (genotype.getGender() == Genome.X)
			yearlings.add(individualsManager.getFemale(genotype, 0, this));
		else
			yearlings.add(individualsManager.getMale(genotype, 0, this));
	}
	
	int getIndividualsNumber(){
		return males.size() + females.size() + otherImmatures.size() + yearlings.size();
	}
	
	public double getAttractivness() {
		return 0.5;			//#Stub
	}

	List<Individual> getIndividuals(){
		List<Individual> individuals = new ArrayList<Individual>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(otherImmatures);
		individuals.addAll(yearlings);
		return individuals;
	}
	
	public void killIndividual(Individual individual) {
		if (!males.remove(individual))
			if (!females.remove(individual))
				if (!otherImmatures.remove(individual))
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
		for (Individual indiv : males)
			indiv.updateSettings();
		for (Individual indiv : females)
			indiv.updateSettings();
		otherImmatures.addAll(yearlings);
		yearlings.clear();
		ListIterator<Individual> iterator = otherImmatures.listIterator();
		while (iterator.hasNext()) {
			Individual indiv = iterator.next();
			indiv.updateSettings();
			if (indiv.isMature()){
				iterator.remove();
				if (indiv.isFemale())
					females.add((Female)indiv);
				else
					males.add((Male)indiv);
			}
		}
	}
	
	public static void setCapacityMultiplier(double multiplier){
		capacityMultiplier = multiplier;
	}
	
}
