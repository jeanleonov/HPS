package zone;


import messaging.Messaging;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ZoneBehaviour extends CyclicBehaviour implements Messaging{

	private static final long serialVersionUID = 1L;
	
	private Zone zone;
	
	ZoneBehaviour(Zone zone){
		this.zone =zone;
	}
	@Override
	public void action() {
		String message = getMessage();
		if (message.compareTo(SCENARIO_COMMANDS) == 0){
			send
		}
		else if (message.compareTo(START_DIE) == 0){
			
		}
		else if (message.compareTo(STRAT_MOVE) == 0){
			
		}
		else if (message.compareTo(START_LAST_PHASE) == 0){
			
		}
		// check type ofmessage
		//sendMessages(message);
		//String answers[] 
	}
	
	private String getMessage(){
		ACLMessage message = zone.blockingReceive();
		if (message != null){
			return message.getContent();
		}
		else
			return null;
	}

}
