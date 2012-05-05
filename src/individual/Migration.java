package individual;

import java.io.IOException;

import messaging.Messaging;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Migration implements Messaging{
	Individual myAgent;
	
	public Migration(Individual agent){
		this.myAgent = agent;
	}
	
	public void action(AID newZone){
		try {
			
			((Individual)myAgent).changeZone(newZone);
			Object[] params = new Object[3];
			params[0] = myAgent.getLocalName();
			params[1] = ((Individual)myAgent).getGenotype();
			params[2] = ((Individual)myAgent).getAge();
			
			ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
			journey.addReceiver(newZone);
			journey.setLanguage(IMMIGRATION);
			
				journey.setContentObject(params);
	
			myAgent.send(journey);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Individual immigration error");
		}
	}
}
