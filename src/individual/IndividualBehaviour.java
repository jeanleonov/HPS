package individual;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

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
			ACLMessage reply = msg.createReply();

			if (msg.getPerformative() == ACLMessage.INFORM) {
				String query = msg.getContent();
				Individual indiv = (Individual)myAgent;
				if(query == START_DIE) {
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
					}
				}
				else if(query == START_MOVE) {
					
				}
			} else {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}

			myAgent.send(reply);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
