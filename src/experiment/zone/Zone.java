package experiment.zone;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import settings.Settings;
import starter.Shared;
import utils.individuals.allocation.IIndividualsManager;
import utils.individuals.allocation.IndividualsManagerDispatcher;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;
import experiment.Experiment;
import experiment.individual.Female;
import experiment.individual.Individual;
import experiment.individual.Male;
import experiment.individual.genotype.Genome;
import experiment.individual.genotype.Genotype;
import experiment.scenario.ScenarioExecutor;
import experiment.scenario.ZoneCommand;

public class Zone {
	
	private List<Male> males = new LinkedList<Male>();
	private List<Female> females = new LinkedList<Female>();
	private List<Individual> otherImmatures = new LinkedList<Individual>();
	private List<Individual> yearlings = new LinkedList<Individual>();
	private IIndividualsManager individualsManager;
	
	private ZoneDistribution initialDistribution;
	private Experiment experiment;
	private int zoneNumber;
	
	private double capacity;
	private HashMap<Integer, Double> travelCosts;
	private double sumOfTravelPossibilities = 0;
	private int maxSizeOfListOfFemales, minNumberOfMalesForContinue;
	
	private ScenarioExecutor scenarioExecutor;
	private Random rand = new Random();
	
	private double totalSumOfAntiCompetetiveness=0;
	private double totalSumOfVoracity=0;
	
	public Zone(
			ZoneDistribution firstZoneDistr,
			int myNumber,
			double capacityMultiplier) {
		travelCosts = Settings.getMovePosibilitiesFrom(zoneNumber);
		for (Integer zoneNumber : travelCosts.keySet())
			sumOfTravelPossibilities += travelCosts.get(zoneNumber);
		this.initialDistribution = firstZoneDistr;
		this.zoneNumber = myNumber;
		this.individualsManager = IndividualsManagerDispatcher.getIndividualsManager(zoneNumber);
		this.maxSizeOfListOfFemales = Shared.DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES;
		this.minNumberOfMalesForContinue = Shared.DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE;
		capacity = firstZoneDistr.getCapacity()*capacityMultiplier;
		scenarioExecutor = new ScenarioExecutor(this);
	}
	
	public void resetTo(int experimentNumber) {
		for (Male male : males)
			individualsManager.killMale(male);
		males.clear();
		for (Female female : females)
			individualsManager.killFemale(female);
		females.clear();
		for (Individual individual : otherImmatures) {
			if (individual.isFemale())
				individualsManager.killFemale((Female)individual);
			else
				individualsManager.killMale((Male)individual);
		}
		otherImmatures.clear(); {
		for (Individual individual : yearlings)
			if (individual.isFemale())
				individualsManager.killFemale((Female)individual);
			else
				individualsManager.killMale((Male)individual);
		}
		yearlings.clear();
		createIndividuals(initialDistribution);
	}

	public void scenarioCommand(ZoneCommand command) throws IOException {
		scenarioExecutor.action(command);
	}
	
	public void dieProcessing() {
		logPopulationSizes("Die         ");
		killDieLoosersIn(males);
		killDieLoosersIn(females);
		killDieLoosersIn(otherImmatures);
		killDieLoosersIn(yearlings);
	}
	
	private void killDieLoosersIn(List<? extends Individual> indivs) {
		ListIterator<? extends Individual> iterator = indivs.listIterator();
		while (iterator.hasNext()) {
			Individual indiv = iterator.next(); 
			if (indiv.isDead()) {
				if (indiv.isFemale())
					individualsManager.killFemale((Female) indiv);
				else
					individualsManager.killMale((Male) indiv);
				iterator.remove();
			}
		}
	}

	public void movePhase() {
		logPopulationSizes("Move        ");
		for (Individual individual : males)
			tryToMoveIndividual(individual);
		for (Individual individual :females)
			tryToMoveIndividual(individual);
		for (Individual individual : yearlings)
			tryToMoveIndividual(individual);
		for (Individual individual : otherImmatures)
			tryToMoveIndividual(individual);
	}
	
	private void tryToMoveIndividual(Individual individual) {
		if (individual.isGoingOut()){
			Integer outZone = individual.whereDoGo();
			if(outZone != new Integer(-1) && outZone != zoneNumber){
				sendIndividualTo(individual, outZone);
				killIndividual(individual);
			}
		}
	}
	
	private void sendIndividualTo(Individual indiv, int zoneNumber){
		Zone newZone = experiment.getZone(zoneNumber);
		if (newZone != null)
			newZone.createIndividual(indiv.getGenotype(), indiv.getAge());
	}
	
	public void reproductionProcessing() {
		logPopulationSizes("Reproduction");
		int readyMales, cicles=0;
		do{
			readyMales=0;
			for (Male male : males){
				if (male.isReadyToReproduction()){
					Female[] femaleList = male.getFemaleListForUpdating();
					randomFilling(femaleList);
					male.chooseFemale();
					readyMales++;
				}
			}
			for (Female female : females)
				createIndividuals(female.getPosterity());
		}while (readyMales>minNumberOfMalesForContinue && cicles++<Shared.MAX_NUMBER_OF_REPRODUCTION_CIRCLES);
	}
	
	public void competitionProcessing() {
		logPopulationSizes("Competition ");
		recalculateTotalSumOfAntiCompetetiveness();
		recalculateTotalSumOfVoracity();
		if(totalSumOfVoracity <= capacity)
			return;
		killCompetitionLoosers();
	}
	
	public HashMap<Integer, Double> getZoneTravelPossibilities(){
		if(travelCosts != null)
			return (HashMap<Integer, Double>) travelCosts;
		else return null;
	}
	
	public double getSumOfTravelPossibilities() {
		return sumOfTravelPossibilities;
	}
	
	public void createIndividuals(ZoneDistribution zoneDistribution) {
		if (zoneDistribution == null)
			return;
		List<GenotypeAgeCountTrio> gants = zoneDistribution.getGenotypeDistributions();
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
	
	private void addIndividualToList(Individual individual) {
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
	
	private int getIndividualsNumber(){
		return males.size() + females.size() + otherImmatures.size() + yearlings.size();
	}
	
	public double getAttractivness() {
		return 0.5;			//#Stub
	}
	
	private void killIndividual(Individual individual) {
		if (!males.remove(individual))
			if (!females.remove(individual))
				if (!otherImmatures.remove(individual))
					yearlings.remove(individual);
		if (individual.isFemale())
			individualsManager.killFemale((Female)individual);
		else
			individualsManager.killMale((Male)individual);
	}
	
	public int getMaxSizeOfListOfFemales() {
		return maxSizeOfListOfFemales;
	}
	
	public void updateListsAndIndividualSettings() {
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
	
	private void recalculateTotalSumOfAntiCompetetiveness() {
		totalSumOfAntiCompetetiveness = 0;
		for (Individual element : males)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : females)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : otherImmatures)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : yearlings)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
	}
	
	private void recalculateTotalSumOfVoracity() {
		totalSumOfVoracity = 0;
		for (Individual element : males)
			totalSumOfVoracity += element.getVoracity();
		for (Individual element : females)
			totalSumOfVoracity += element.getVoracity();
		for (Individual element : otherImmatures)
			totalSumOfVoracity += element.getVoracity();
		for (Individual element : yearlings)
			totalSumOfVoracity += element.getVoracity();
	}
	
	private double getWeightedTotalSumOfVoracity() {
		double weightedTotalSumOfVoracity = 0;
		for (Individual element : males)
			weightedTotalSumOfVoracity += element.getVoracity()*element.getCompetitiveness();
		for (Individual element : females)
			weightedTotalSumOfVoracity += element.getVoracity()*element.getCompetitiveness();
		for (Individual element : otherImmatures)
			weightedTotalSumOfVoracity += element.getVoracity()*element.getCompetitiveness();
		for (Individual element : yearlings)
			weightedTotalSumOfVoracity += element.getVoracity()*element.getCompetitiveness();
		return weightedTotalSumOfVoracity;
	}
	
	/** @return random elements according to specified probabilities */
	private void killCompetitionLoosers() {
		double coeficient =  (totalSumOfVoracity-capacity)/capacity
				*getIndividualsNumber()/totalSumOfAntiCompetetiveness
				*getWeightedTotalSumOfVoracity()/totalSumOfVoracity;
		killCompetitionLoosersIn(males, coeficient);
		killCompetitionLoosersIn(females, coeficient);
		killCompetitionLoosersIn(otherImmatures, coeficient);
		killCompetitionLoosersIn(yearlings, coeficient);
	}
	
	private void killCompetitionLoosersIn(List<? extends Individual> indivs, double coeficient) {
		ListIterator<? extends Individual> iterator = indivs.listIterator();
		while (iterator.hasNext()) {
			Individual indiv = iterator.next();
			if (Math.random() <= 1 - indiv.getCompetitiveness()/coeficient) {
				if (indiv.isFemale())
					individualsManager.killFemale((Female) indiv);
				else
					individualsManager.killMale((Male) indiv);
				iterator.remove();
			}
		}
	}
	
	private void randomFilling(Female[] femalesArray){
		int i=0;
		if (females.size()!=0){
			int numberOfFemales = Math.abs(rand.nextInt()%(maxSizeOfListOfFemales+1));
			for (i=0; i<numberOfFemales; i++)
				femalesArray[i] = females.get(Math.abs(rand.nextInt()%females.size()));
		}
		for (; i<maxSizeOfListOfFemales; i++)
			femalesArray[i] = null;
	}
	
	public List<Male> getMales() {
		return males;
	}

	public List<Female> getFemales() {
		return females;
	}

	public List<Individual> getOtherImmatures() {
		return otherImmatures;
	}

	public List<Individual> getYearlings() {
		return yearlings;
	}
	
	public int getZoneNumber() {
		return zoneNumber;
	}

	private void logPopulationSizes(String beforePhaseName){
		int	malesNumber = males.size(),
			femalesNumber = females.size(),
			immaturesNumber = otherImmatures.size(),
			yearlingsNumber = yearlings.size();
		Shared.debugLogger.debug(MessageFormat.format(
				"Before {0}: M-{1,number},\tF-{2,number},\tI-{3,number},\tY-{4,number}", 
				beforePhaseName, malesNumber, femalesNumber, immaturesNumber, yearlingsNumber));
	}
}
