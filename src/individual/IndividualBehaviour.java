package individual;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class IndividualBehaviour extends CyclicBehaviour implements messaging.Messaging {

	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.blockingReceive();
		if (msg == null) {
			block();
			return;
		}

		try {
			//if (msg.getPerformative() == ACLMessage.INFORM) {
			switch(msg.getPerformative()){
				case ACLMessage.INFORM:{
					ACLMessage reply = msg.createReply();
					String query = msg.getContent();
					Individual indiv = (Individual)myAgent;
					if(query.equals(START_DIE)) {
						// TODO
						double randVal = Math.random();
						float prob = indiv.getSetting(settings.Vocabulary.Param.Survival);
						if(indiv.age != 0) prob *= indiv.getSetting(settings.Vocabulary.Param.CompetitivenessFactorFirst);
						if(randVal <= prob) {
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setContent(NO);
						}
						else {
							reply.setPerformative(ACLMessage.AGREE);
							reply.setContent(YES);
							myAgent.doDelete();
						}
						myAgent.send(reply);
					}
					else
					if(query.equals("Die")){
							myAgent.doDelete();
					}
					else{
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						myAgent.send(reply);
					}
					break;
				}
				case ACLMessage.REQUEST:{
					if(msg.getLanguage() == "Migration"){
						((Individual)myAgent).changeZone((AID)msg.getContentObject());
						
						Object[] params = new Object[3];
						params[0] = myAgent.getLocalName();
						params[1] = ((Individual)myAgent).getGenotype();
						params[2] = ((Individual)myAgent).getAge();
						
						ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
						journey.addReceiver((AID)msg.getContentObject());
						journey.setLanguage("immigration");
						journey.setContentObject(params);
						myAgent.send(journey);
					}
					break;
				}
				default:{
				}
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
