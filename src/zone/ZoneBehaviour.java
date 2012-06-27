package zone;

import genotype.Genotype;
import individual.Female;
import individual.Individual;
import individual.Male;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import messaging.Messaging;
import settings.Settings;
import statistic.GenotypeAgeDistribution;
import statistic.StatisticPackage;
import experiment.ZoneCommand;



public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	private Zone myZone;
	
	private StatisticPackage currentPackage;
	
	private ScenarioExecutor scenarioExecutor = null;
	private Random rand = new Random();
	
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
		ACLMessage message = myAgent.blockingReceive();/*#*/
		/*System.out.println("Zone " + myZone.zoneId + " in Experiment " + 
							myZone.experimentId + " got " + message.getContent());#lao*/
		if(message.getPerformative() == ACLMessage.REQUEST){
			String language = message.getLanguage();
			ACLMessage reply = message.createReply();
			if (language.compareTo(START_DIE) == 0){
				myZone.iteration++;
				refreshStatistic(); // TODO �������� !!!
				dieProcessing();
			}
			else if(language.compareTo(START_MOVE) == 0){
				moveProcessing();
			}
			else if(language.compareTo(SCENARIO) == 0){
				scenarioCommandProcessing(message);
			}
			else if (language.compareTo(START_LAST_PHASE) == 0){
				lastPhaseProcessing();
				myZone.updateLists();
				// TODO after all operations we have to send package to statistic Dispatcher (or Experiment) !!!!!!!
			}
			else if (language.compareTo(I_KILL_YOU) == 0){
				killingSystemProcessing();
			}
			else if (language.compareTo(MIGRATION) == 0){
				try{
					myZone.addIndividualToList((Individual)message.getContentObject());
				}
				catch(UnreadableException e){
					System.out.println("Unknown parameters for immigrating individuals");
					e.printStackTrace();
				}
			}
			myZone.send(reply);/*#*/
		}
	}

	private void scenarioCommandProcessing(ACLMessage message) {
		try {
			scenarioExecutor.action((ZoneCommand)message.getContentObject());
		} catch (UnreadableException e) {
			System.out.println("getting content object error");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error command executing");
			e.printStackTrace();
		}
	}
	
	private void dieProcessing() {
		for (Individual indiv : myZone.getIndividuals())
			if (indiv.isDead())
				myZone.killIndividual(indiv);
	}

	private void moveProcessing() {
		for (Individual indiv : myZone.getIndividuals())
			if (indiv.isGoingOut()){
				sendIndividualTo(indiv, indiv.whereDoGo());
				myZone.killIndividual(indiv);
			}
	}
	
	private void sendIndividualTo(Individual indiv, int zoneNumber){
		AID newZone = Settings.getZoneAID(zoneNumber);
		if (newZone != null){
			ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
			journey.addReceiver(newZone);
			journey.setLanguage(MIGRATION);
			try {
				journey.setContentObject(indiv);
			} catch (IOException e)  {e.printStackTrace();}
			myAgent.send(journey);
		}
	}
	
	
	private void lastPhaseProcessing() {
		reproductionProcessing();
		competitionProcessing();
	}
	
	private void killingSystemProcessing() {
		myAgent.doDelete();
	}
	
	private void reproductionProcessing() {
		for (Male male : myZone.males){
			Female[] femaleList = male.getFemaleListForUpdating();
			randomFilling(femaleList);
			male.chooseFemale();
		}
		for (Female female : myZone.females)
			myZone.createIndividuals(female.getPosterity());
	}
	
	private void competitionProcessing(){
		// TODO
	}
	
	private void randomFilling(Female[] females){
		int numberOfFemales = Math.abs(rand.nextInt()%(myZone.maxSizeOfListOfFemales+1)), i;
		for (i=0; i<numberOfFemales; i++)
			females[i] = myZone.females.get(rand.nextInt()%myZone.females.size());
		for (; i<myZone.maxSizeOfListOfFemales; i++)
			females[i] = null;
	}

	private void refreshStatistic() {
		currentPackage  = createStatisticPackage();
		sendStatisticPackage();
	}
	
	private StatisticPackage createStatisticPackage(){
		int experimentId = myZone.experimentId;
		int zoneId = myZone.zoneId;
		int iterationId = myZone.iteration;
		GenotypeAgeDistribution gad = createGAD();
		StatisticPackage statisticPackage = new StatisticPackage(experimentId, zoneId, iterationId, gad);
		return statisticPackage;
	}
		
	private GenotypeAgeDistribution createGAD() {
		GenotypeAgeDistribution gad = new GenotypeAgeDistribution();
		Vector<Individual> individuals = myZone.getIndividuals();
		for (Individual indiv : individuals)
			gad.addToGant(Genotype.getIdOf(indiv.getGenotype()), indiv.getAge());
		return gad;
	}	
	
	private void sendStatisticPackage() {		
		try {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		//	message.setContent(STATISTIC);		#lao
			message.setContentObject(currentPackage);		
			message.addReceiver(myZone.statisticDispatcher);
			myAgent.send(message);
		//	System.out.println("Zone " + myZone.zoneId + " sent statistic");	#lao
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
