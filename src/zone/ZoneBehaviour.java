package zone;

import genotype.Genotype;
import individual.Female;
import individual.Individual;
import individual.Male;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import messaging.Messaging;
import settings.Settings;
import starter.Shared;
import statistic.GenotypeAgeDistribution;
import statistic.StatisticPackage;
import experiment.ZoneCommand;



public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	private Zone myZone;
	
	private ScenarioExecutor scenarioExecutor = null;
	private Random rand = new Random();
	private int movedThisYear;
	private GenotypeAgeDistribution previousIterationGAD = null;
	
	@Override
	public void onStart(){
		myZone = (Zone)myAgent;
		scenarioExecutor = new ScenarioExecutor(myZone);
	}
	
	@Override
	public void action() {
		ACLMessage message = myAgent.blockingReceive();
		if(message.getPerformative() == ACLMessage.REQUEST){
			String language = message.getLanguage();
			ACLMessage reply = message.createReply();
			if(language.compareTo(START_MOVE) == 0)
				moveProcessing();
			else if(language.compareTo(SCENARIO) == 0)
				scenarioCommandProcessing(message);
			else if (language.compareTo(START_FIRST_PHASE) == 0){
				myZone.iteration++;
				firstPhaseProcessing();
			}
			else if (language.compareTo(I_KILL_YOU) == 0)
				killingSystemProcessing();
			else if (language.compareTo(MIGRATION) == 0)
				myZone.createIndividual(message.getContent());
			myZone.send(reply);
		}
	}

	private void scenarioCommandProcessing(ACLMessage message) {
		try {
			scenarioExecutor.action((ZoneCommand)message.getContentObject());
		} catch (UnreadableException e) {
			Shared.problemsLogger.error(e.getMessage());
		} catch (IOException e) {
			Shared.problemsLogger.error(e.getMessage());
		}
	}
	
	private void dieProcessing() {
		logPopulationSizes("Die         ");
		killDieLoosersIn(myZone.males);
		killDieLoosersIn(myZone.females);
		killDieLoosersIn(myZone.otherImmatures);
		killDieLoosersIn(myZone.yearlings);
	}
	
	private static void killDieLoosersIn(List<? extends Individual> indivs) {
		ListIterator<? extends Individual> iterator = indivs.listIterator();
		while (iterator.hasNext())
			if (iterator.next().isDead())
				iterator.remove();
	}

	private void moveProcessing() {
		logPopulationSizes("Move        ");
		movedThisYear=0;
		for (Individual indiv : myZone.getIndividuals())
			if (indiv.isGoingOut()){
				Integer outZone = indiv.whereDoGo();
				if(outZone == null){
					System.out.println("something wrong with whereDoGo function, technical (not idea) bug");
					break;
				}
				if(outZone != new Integer(-1) && outZone != myZone.zoneId){
					sendIndividualTo(indiv, outZone);
					myZone.killIndividual(indiv);
				}
			}
		waitForResponsesFromZones();
	}
	
	private void sendIndividualTo(Individual indiv, int zoneNumber){
		AID newZone = Settings.getZoneAID(zoneNumber);
		if (newZone != null){
			movedThisYear++;
			ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
			journey.addReceiver(newZone);
			journey.setLanguage(MIGRATION);
			journey.setContent(indiv.toString());
			myAgent.send(journey);
		}
	}
	
	private void waitForResponsesFromZones(){
		for (int i=0; i<movedThisYear;) {
			ACLMessage message = myAgent.blockingReceive(MessageTemplate.MatchLanguage(MIGRATION));
			if (message.getContent() == null)
				i++;
			else{
				ACLMessage reply = message.createReply();
				myZone.createIndividual(message.getContent());
				myZone.send(reply);
			}
		}
	}
	
	private void firstPhaseProcessing() {
		refreshStatistic(0);
		myZone.updateListsAndIndividualSettings();
		refreshStatistic(1);
		reproductionProcessing();
		refreshStatistic(2);
		competitionProcessing();
		refreshStatistic(3);
		dieProcessing();
		refreshStatistic(4);
	}
	
	private void killingSystemProcessing() {
		myAgent.doDelete();
	}
	
	private void reproductionProcessing() {
		logPopulationSizes("Reproduction");
		int readyMales, cicles=0;
		do{
			readyMales=0;
			for (Male male : myZone.males){
				if (male.isReadyToReproduction()){
					Female[] femaleList = male.getFemaleListForUpdating();
					randomFilling(femaleList);
					male.chooseFemale();
					readyMales++;
				}
			}
			for (Female female : myZone.females)
				myZone.createIndividuals(female.getPosterity());
		}while (readyMales>myZone.minNumberOfMalesForContinue && cicles++<Shared.MAX_NUMBER_OF_REPRODUCTION_CIRCLES);
	}
	
	private void competitionProcessing(){
		logPopulationSizes("Competition ");
		int individualsNumber = myZone.getIndividualsNumber();
		if(individualsNumber <= myZone.capacity)
			return;
		double totalSumOfAntiCompetetiveness = getTotalSumOfAntiCompetetiveness();
		killCompetitionLoosers(individualsNumber-myZone.capacity, totalSumOfAntiCompetetiveness, individualsNumber);
	}
	
	private double getTotalSumOfAntiCompetetiveness() {
		double totalSumOfAntiCompetetiveness=0;
		for (Individual element : myZone.males)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : myZone.females)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : myZone.otherImmatures)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		for (Individual element : myZone.yearlings)
			totalSumOfAntiCompetetiveness += element.getAntiCompetitiveness();
		return totalSumOfAntiCompetetiveness;
	}
	
	/** @return random elements according to specified probabilities */
	private void killCompetitionLoosers(float approximateQuantity, double totalSumOfAntiCompetetiveness, int indivsNumber) {
		double coeficient = approximateQuantity/indivsNumber/(totalSumOfAntiCompetetiveness/indivsNumber);
		killCompetitionLoosersIn(myZone.males, coeficient);
		killCompetitionLoosersIn(myZone.females, coeficient);
		killCompetitionLoosersIn(myZone.otherImmatures, coeficient);
		killCompetitionLoosersIn(myZone.yearlings, coeficient);
	}
	
	private static void killCompetitionLoosersIn(List<? extends Individual> indivs, double coeficient) {
		ListIterator<? extends Individual> iterator = indivs.listIterator();
		while (iterator.hasNext())
			if (Math.random() <= iterator.next().getAntiCompetitiveness()*coeficient)
				iterator.remove();
	}
	
	
	/*
	private void competitionProcessing(){
		logPopulationSizes("Competition ");
		List<Individual> individuals = myZone.getIndividuals();
		double totalVoracity = getTotalVoracity(individuals);
		if(totalVoracity <= myZone.capacity)
			return;
		//#ProbabilityCollection<Individual> probabilityCollection = new ProbabilityCollection<Individual>(individuals);
		//#Set<Individual> individualsToKill = probabilityCollection.getElements(individualsNumber-myZone.capacity);
		Set<Individual> individualsToKill = getLoosers(individuals, totalVoracity);
		//#List<Individual> individualsToKill = probabilityCollection.getElementsForCapacity(myZone.capacity);
		for (Individual indiv : individualsToKill)
			myZone.killIndividual(indiv);
	}
	
	private static double stubCoeficient = 1; */
	
	/** @return random elements according to specified probabilities */
	/*public Set<Individual> getLoosers(List<Individual> individuals, double totalVoracity) {
		double totalSumOfProbabilities = getTotalCompetitiveness(individuals);
		double voracityOverflow = totalVoracity - myZone.capacity;
		HashSet<Individual> result = new HashSet<Individual>();
		double coeficient = voracityOverflow/totalVoracity/(totalSumOfProbabilities/individuals.size())*stubCoeficient;
		for (Individual element : individuals)
			if (Math.random() <= element.getProbability() + (1-element.getProbability())*coeficient)
				result.add(element);
		double aa = voracityOverflow;								// 
		double a = getTotalVoracity(result) - voracityOverflow;		// #temporary.. collapse it to one line
		double correction = -a/aa;									// 
		//stubCoeficient += correction;
		return result;
	}
	
	private static double getTotalVoracity(Iterable<Individual> individuals) {
		float sum = 0;
		for (Individual indiv : individuals)
			sum += indiv.getVoracity();
		return sum;
	}
	
	private static double getTotalCompetitiveness(Iterable<Individual> individuals) {
		float sum = 0;
		for (Individual indiv : individuals)
			sum += indiv.getCompetitiveness();
		return sum;
	}*/
	
	private void randomFilling(Female[] females){
		int i=0;
		if (myZone.females.size()!=0){
			int numberOfFemales = Math.abs(rand.nextInt()%(myZone.maxSizeOfListOfFemales+1));
			for (i=0; i<numberOfFemales; i++)
				females[i] = myZone.females.get(Math.abs(rand.nextInt()%myZone.females.size()));
		}
		for (; i<myZone.maxSizeOfListOfFemales; i++)
			females[i] = null;
	}

	/**
	 * Создать и отослать пакет статистики
	 */
	private void refreshStatistic(int subStepNumber) {
		StatisticPackage currentPackage  = createStatisticPackage(subStepNumber);
		sendStatisticPackage(currentPackage);
	}
	
	/**
	 * Сгенерировать пакет статистики
	 * @return
	 */
	private StatisticPackage createStatisticPackage(int subStepNumber){
		int experimentId = myZone.experimentId;
		int zoneId = myZone.zoneId;
		int iterationId = myZone.iteration;
		GenotypeAgeDistribution gad = createGAD();
		StatisticPackage statisticPackage = new StatisticPackage(experimentId, zoneId, iterationId, subStepNumber, gad);
		return statisticPackage;
	}
	
	/**
	 * Сгенерировать распределение
	 * @return
	 */
	private GenotypeAgeDistribution createGAD() {
		GenotypeAgeDistribution gad = new GenotypeAgeDistribution();
		for (Individual indiv : myZone.males)
			gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge(), true);
		for (Individual indiv : myZone.females)
			gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge(), true);
		for (Individual indiv : myZone.otherImmatures)
			gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge(), false);
		for (Individual indiv : myZone.yearlings)
			gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge(), false);
		gad.setDifferencesWith(previousIterationGAD);
		previousIterationGAD = gad;
		return gad;
	}	
	
	/**
	 * Отослать пакет статистики диспетчеру
	 * @param currentPackage
	 */
	private void sendStatisticPackage(StatisticPackage currentPackage) {		
		try {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setContentObject(currentPackage);		
			message.addReceiver(myZone.statisticDispatcher);
			myAgent.send(message);
		} catch (IOException e) {
			Shared.problemsLogger.error(e.getMessage());
		}
	}
	
	private void logPopulationSizes(String beforePhaseName){
		int	males = myZone.males.size(),
			females = myZone.females.size(),
			immatures = myZone.otherImmatures.size(),
			yearlings = myZone.yearlings.size();
		Shared.debugLogger.debug(MessageFormat.format(
				"Before {0}: M-{1,number},\tF-{2,number},\tI-{3,number},\tY-{4,number}", 
				beforePhaseName, males, females, immatures, yearlings));
	}
}
