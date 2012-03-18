package zone;


import java.io.IOException;

import messaging.Messaging;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void action() {
		String message = getMessage();
		if (message.compareTo(SCENARIO_COMMANDS) == 0){
			scenarioCommandProcessing();
		}
		else if (message.compareTo(START_DIE) == 0){
			dieProcessing();
		}
		else if (message.compareTo(START_MOVE) == 0){
			moveProcessing();
		}
		else if (message.compareTo(START_LAST_PHASE) == 0){
			lastPhaseProcessing();
		}
	}
	
	private void scenarioCommandProcessing() {
		// TODO Обработка комманд сценария
		
	}
	private void dieProcessing() {
		sendMessageToIndividuals(START_DIE);
	}

	private void moveProcessing() {
		sendMessageToIndividuals(START_MOVE);	
	}
	private void lastPhaseProcessing() {
		// TODO Auto-generated method stub
		
	}
	private String getMessage(){
		ACLMessage message = myAgent.blockingReceive();
		if (message != null){
			return message.getContent();
		}
		else
			return null;
	}	
	
	private void sendMessageToIndividuals(String message) {
		for (AID individual : ((Zone)myAgent).males){
			sendMessage(individual, message);
		}
		for (AID individual : ((Zone)myAgent).females){
			sendMessage(individual, message);
		}
		for (AID individual : ((Zone)myAgent).immatures){
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

}
