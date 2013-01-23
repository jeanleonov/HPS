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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import messaging.Messaging;
import settings.Settings;
import starter.Shared;
import statistic.GenotypeAgeDistribution;
import statistic.StatisticPackage;
import utils.ProbabilityCollection;
import experiment.ZoneCommand;



public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	private Zone myZone;
	
	private ScenarioExecutor scenarioExecutor = null;
	private Random rand = new Random();
	private int movedThisYear;
	
	@Override
	public void onStart(){
		myZone = (Zone)myAgent;
		scenarioExecutor = new ScenarioExecutor(myZone);
	}
	
	/*
	 * 	Zone creates new statisticPackage at the beginning of each year
	 *  (after getting a  SCENARIO_COMMANDS message)
	 */
	@Override
	public void action() {
		ACLMessage message = myAgent.blockingReceive();
		if(message.getPerformative() == ACLMessage.REQUEST){
			String language = message.getLanguage();
			ACLMessage reply = message.createReply();
	/*@#	if (language.compareTo(START_DIE) == 0){
				dieProcessing();
			}
			else*/
			if(language.compareTo(START_MOVE) == 0){
				moveProcessing();
			}
			else if(language.compareTo(SCENARIO) == 0){
				scenarioCommandProcessing(message);
			}
			else if (language.compareTo(START_FIRST_PHASE) == 0){
				myZone.iteration++;
				refreshStatistic();
				firstPhaseProcessing();
			}
			else if (language.compareTo(I_KILL_YOU) == 0){
				killingSystemProcessing();
			}
			else if (language.compareTo(MIGRATION) == 0){
				myZone.createIndividual(message.getContent());
			}
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
		ArrayList<Male> malesToKill = new ArrayList<Male>();
		ArrayList<Female> femalesToKill = new ArrayList<Female>();
		ArrayList<Individual> immaturesToKill = new ArrayList<Individual>();
		for (Male indiv : myZone.males)
			if (indiv.isDead())
				malesToKill.add(indiv);
		for (Female indiv : myZone.females)
			if (indiv.isDead())
				femalesToKill.add(indiv);
		for (Individual indiv : myZone.immatures)
			if (indiv.isDead())
				immaturesToKill.add(indiv);
		for (Male indiv : malesToKill)
			myZone.killMale(indiv);
		for (Female indiv : femalesToKill)
			myZone.killFemale(indiv);
		for (Individual indiv : immaturesToKill)
			myZone.killImmature(indiv);
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
		for (int i=0; i<movedThisYear;){				// BAD CODE! 
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
		myZone.updateListsAndIndividualSettings();
		reproductionProcessing();
		competitionProcessing();
		dieProcessing();
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
		List<Individual> individuals = myZone.getIndividuals();
		int individualsNumber = individuals.size();
		if(individualsNumber <= myZone.capacity)
			return;
		ProbabilityCollection<Individual> probabilityCollection = new ProbabilityCollection<Individual>(individuals);
		Set<Individual> individualsToKill = probabilityCollection.getElements(individualsNumber-myZone.capacity);
		for (Individual indiv : individualsToKill)
			myZone.killIndividual(indiv);
	}
	
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
	 * ������� � �������� ����� ����������
	 */
	private void refreshStatistic() {
		StatisticPackage currentPackage  = createStatisticPackage();
		sendStatisticPackage(currentPackage);
	}
	
	/**
	 * ������������� ����� ����������
	 * @return
	 */
	private StatisticPackage createStatisticPackage(){
		int experimentId = myZone.experimentId;
		int zoneId = myZone.zoneId;
		int iterationId = myZone.iteration;
		GenotypeAgeDistribution gad = createGAD();
		StatisticPackage statisticPackage = new StatisticPackage(experimentId, zoneId, iterationId, gad);
		return statisticPackage;
	}
	
	/**
	 * ������������� �������������
	 * @return
	 */
	private GenotypeAgeDistribution createGAD() {
		GenotypeAgeDistribution gad = new GenotypeAgeDistribution();
		List<Individual> individuals = myZone.getIndividuals();
		for (Individual indiv : individuals)
			if (indiv.isMature())
				gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge());
		return gad;
	}	
	
	/**
	 * �������� ����� ���������� ����������
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
			immatures = myZone.immatures.size(),
			yearlings = myZone.yearlings.size();
		Shared.debugLogger.debug(MessageFormat.format(
				"Before {0}: M-{1,number},\tF-{2,number},\tI-{3,number},\tY-{4,number}", 
				beforePhaseName, males, females, immatures, yearlings));
	}
}
