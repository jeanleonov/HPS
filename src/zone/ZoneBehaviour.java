package zone;

import java.io.IOException;

import messaging.Messaging;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	private Zone myZone; 
	
	ZoneBehaviour(){
		myZone = (Zone)myAgent;
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
		ACLMessage message = myAgent.blockingReceive();
		if (message != null){
			return message;
		}
		return null;
	}
	
	private String getMessageContent(){
		ACLMessage message = myAgent.blockingReceive();
		if (message != null){
			return message.getContent();
		}
		return null;
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
}
