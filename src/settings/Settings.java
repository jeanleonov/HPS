package settings;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings extends Agent implements Vocabulary {

	private static final long serialVersionUID = 1L;

	private final static String serviceName = "Settings";
	private HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<genotype.Genotype, ArrayList<ViabilityPair>>();
	private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();

	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args.length < 2)
			return;
		viabilityTable = (HashMap<genotype.Genotype, ArrayList<ViabilityPair>>) args[0];
		posterityTable = (HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>) args[1];

		DFRegister();
		BehaviourRegister();
	}

	@Override
	protected void takeDown() {
		DFDeregister();
	}

	private void DFRegister() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName(serviceName);
			sd.setType(serviceName);
			dfd.addServices(sd);

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	private void DFDeregister() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	private void BehaviourRegister() {
		addBehaviour(new SettingsMessageListener());
	}

	class SettingsMessageListener extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage msg = blockingReceive();
			if (msg == null) {
				block();
				return;
			}

			try {
				ACLMessage reply = msg.createReply();

				if (msg.getPerformative() == ACLMessage.QUERY_REF) {
					Object content = msg.getContentObject();
					if (content instanceof genotype.Genotype) {
						genotype.Genotype pair = (genotype.Genotype) content;
						ArrayList<ViabilityPair> value = viabilityTable.get(pair);
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContentObject(value);
					} else if (content instanceof PosterityParentsPair) {
						PosterityParentsPair pair = (PosterityParentsPair) content;
						PosterityResultPair[] result = (PosterityResultPair[]) posterityTable
								.get(pair).toArray();
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContentObject(result);
					} else {
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					}
				} else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				}

				send(reply);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}