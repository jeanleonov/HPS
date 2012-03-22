package zone;

import java.io.IOException;
import java.util.Vector;

import statistic.GenotypeAgeDistribution;
import statistic.GenotypeAgeNumberTrio;
import statistic.StatisticPackage;

import messaging.Messaging;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	private Zone myZone;
	
	private StatisticPackage currentPackage;
	
	ZoneBehaviour(){
		myZone = (Zone)myAgent;
		currentPackage = createStatisticPackage();
	}
	
	@Override
	public void action() {
		String message = getMessageContent();
		if (message.compareTo(SCENARIO_COMMANDS) == 0){
			// scenarioCommandProcessing(); TODO Realise scenario process
		}
		else if (message.compareTo(START_DIE) == 0){
			dieProcessing();
		}
		else if (message.compareTo(START_MOVE) == 0){
			// moveProcessing(); TODO Realise move process
		}
		else if (message.compareTo(START_LAST_PHASE) == 0){
			// lastPhaseProcessing(); TODO Realise last phase process
		}
		else if (message.compareTo(I_KILL_YOU) == 0){
			killingSystemProcessing();
		}
		refresfStatistic();
		myZone.iteration++;
	}

	private void scenarioCommandProcessing() {
		//String message = getMessageContent();
		// TODO 
	}
	
	private void dieProcessing() {
		sendMessageToIndividuals(START_DIE);
		getAnswersOnDieMessage();
	}

	private void getAnswersOnDieMessage() {
		ACLMessage message;
		int individualCounter = myZone.getIndividualsNumber();
		for (int i = individualCounter; i > 0; i--){	//warning
			message = getMessage();
			if (message.getContent().compareTo(YES) == 0){
				killIndividual(message.getSender());
			}
		}
	}

	private void killIndividual(AID individual) {
		myZone.males.remove(individual);
		myZone.females.remove(individual);
		myZone.immatures.remove(individual);
	}

	private void moveProcessing() {
		sendMessageToIndividuals(START_MOVE);
		// TODO
	}
	
	private void lastPhaseProcessing() {
		// TODO Auto-generated method stub
	}
	
	private void killingSystemProcessing() {
		sendMessageToIndividuals(I_KILL_YOU);
		killMyself();
	}
	
	private void killMyself() {
		myAgent.doDelete();
	}

	private ACLMessage getMessage(){
		return myAgent.blockingReceive();
	}
	
	private String getMessageContent(){
		ACLMessage message = myAgent.blockingReceive();
		return message.getContent();
	}	
	
	private void sendMessageToIndividuals(String message) {
		for (AID individual : myZone.getIndividuals()){
			sendMessage(individual, message);
		}
	}
	
	private void sendMessage(AID individual, String messageContent) {
		try {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setContentObject(messageContent);
			message.addReceiver(individual);
			myAgent.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void refresfStatistic() {
		//refreshCurrentStatisticPackage();
		
	}

	private StatisticPackage createStatisticPackage(){
		// TODO Experiment must give zone his own id and zone id 
		int experimentId = 0;
		int zoneId = 0;
		int iterationId = myZone.iteration;
		GenotypeAgeDistribution gad = createGAD();
		StatisticPackage statisticPackage = new StatisticPackage(experimentId, zoneId, iterationId, gad);
		return statisticPackage;
	}

	private GenotypeAgeDistribution createGAD() {
		GenotypeAgeDistribution gad = new GenotypeAgeDistribution();
		Vector<AID> individuals = myZone.getIndividuals();
		for (AID individualAID : individuals){
			int age = getIndividualAge(individualAID);
			int genotype = getIndividualGenotype(individualAID);
			gad.add
		}
		return gad;
	}
}
