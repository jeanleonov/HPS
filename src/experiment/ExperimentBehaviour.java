package experiment;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

import messaging.Messaging;
import starter.Shared;
import utils.individuals.allocation.IndividualsManagerDispatcher;

public class ExperimentBehaviour extends Behaviour implements Messaging {

	private static final long serialVersionUID = 1L;
	
	private ACLMessage iFinished;
	Experiment experiment;
	private int countOfMessages;			// shit-code	* see another shit-code
	int yearCursore;
	

	@Override
	public void onStart(){
		experiment = (Experiment)myAgent;
		experiment.scenario.start();
		iFinished = new ACLMessage(ACLMessage.INFORM);
		iFinished.addReceiver(experiment.myProvider);
		iFinished.setLanguage(I_FINISHED);
		yearCursore = 0;
	}

	@Override
	public void action() {
		Shared.infoLogger.info("YEAR NUMBER\t" + yearCursore + "\tSTARTED IN\tEXPERIMENT_" + experiment.experimentNumber);
		firstPhaseProcessing();
	//@#	dieProcessing();
		try {
			scenarioCommandsProcessing();
		} catch (IOException e) {e.printStackTrace();}
		moveProcessing();
		int capacityOfPull = IndividualsManagerDispatcher.getCapacityOfPull();
		Shared.infoLogger.info((capacityOfPull!=-1)?("  Capacity of individuals pull: " + capacityOfPull):"  Capacity of individuals pull: UNDEF");
		yearCursore++;
	}

	@Override
	public boolean done() {
		if (yearCursore < experiment.numberOfModelingYears)
			return false;
		return true;
	}
	
	@Override
	public int onEnd() {
		ACLMessage message = getMessageForMassMailing();
		message.setLanguage(I_KILL_YOU);
		experiment.send(message);
		killMySelf();
		experiment.send(iFinished);
		return 0;
	}
	
	// method for reading scenario commands
	private ACLMessage[] convertCommandsToACLMessages(ArrayList<Action> commands) throws IOException{
		// TODO
		countOfMessages=0;
		ACLMessage[] messages = new ACLMessage[commands.size()];
		for (int i=0; i<messages.length; i++){							// TODO merge it with dieOff tick 
																	// TODO send to Zone array of ZoneCommand
			countOfMessages += commands.get(i).zonesNumbers.size();
			messages[i] = new ACLMessage(ACLMessage.REQUEST);
			messages[i].setLanguage(SCENARIO);
			for (int j=0; j<commands.get(i).zonesNumbers.size(); j++)
				messages[i].addReceiver(experiment.getZoneAID(commands.get(i).zonesNumbers.get(j)));
			messages[i].setContentObject(commands.get(i).command);
		}
		return messages;
	}

	private void scenarioCommandsProcessing() throws IOException{
		ArrayList<Action> actions = experiment.scenario.getCommandsForNextYear(yearCursore);
		if (actions.size() == 0)
			return;
		ACLMessage[] commands 
					= convertCommandsToACLMessages(actions);
		for (ACLMessage command : commands)
			experiment.send(command);
		ignoreNMessages(countOfMessages);
	}

	/*@# private void dieProcessing(){
		ACLMessage message = getMessageForMassMailing();
		message.setLanguage(START_DIE);
		experiment.send(message);
		ignoreNMessages(experiment.zonesAIDs.size());
	}*/

	private void moveProcessing(){
		ACLMessage message = getMessageForMassMailing();
		message.setLanguage(START_MOVE);
		experiment.send(message);
		ignoreNMessages(experiment.zonesAIDs.size());
	}

	private void firstPhaseProcessing(){
		ACLMessage message = getMessageForMassMailing();
		message.setLanguage(START_FIRST_PHASE);
		experiment.send(message);
		ignoreNMessages(experiment.zonesAIDs.size());
	}
	
	private ACLMessage getMessageForMassMailing(){
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		for (AID zoneAID : experiment.zonesAIDs)
			message.addReceiver(zoneAID);
		return message;
	}
	
	private void ignoreNMessages(int N){
		for (int i=0; i<N; i++)
			myAgent.blockingReceive();
	}
	
	void killMySelf(){
		experiment.doDelete();
	}
}
