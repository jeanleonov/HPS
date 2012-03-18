package settings;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings extends Agent implements Vocabulary {

	private static final long serialVersionUID = 1L;

	private final static String serviceName = "Settings";
	private Reader viabilityReader, posterityReader;
	private HashMap<ViabilityPair, Float> viabilityTable = new HashMap<ViabilityPair, Float>();
	private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();

	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args.length < 2)
			return;
		viabilityReader = (Reader) args[0];
		posterityReader = (Reader) args[1];

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
			sd.setType("Settings");
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

	class DataFill extends OneShotBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ViabilityFill();
			PosterityFill();
		}

		private void ViabilityFill() {
			try {
				genotype.Genotype[] genomeOrder = new genotype.Genotype[GenomeCount];
				BufferedReader reader = new BufferedReader(viabilityReader);
				String orderLine = reader.readLine();
				OrderFill(genomeOrder, orderLine, 3);

				String str;
				while ((str = reader.readLine()) != null) {
					String[] strArr = str.split(";");
					if (strArr.length < genomeOrder.length + 3)
						continue;
					for (genotype.Genotype gm : genomeOrder) {
						Param param = Convertor.keyToParam(Integer.parseInt(
								strArr[2], 10));
						for (int i = 3; i < genomeOrder.length; i++)
							viabilityTable.put(new ViabilityPair(gm, param),
									Float.parseFloat(strArr[i]));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private void PosterityFill() {
			try {
				genotype.Genotype[] genomeOrder = new genotype.Genotype[GenomeCount];
				BufferedReader reader = new BufferedReader(posterityReader);
				String orderLine = reader.readLine();
				OrderFill(genomeOrder, orderLine, 2);

				String str;
				while ((str = reader.readLine()) != null) {
					String[] strArr = str.split(";");
					if (strArr.length < genomeOrder.length + 2)
						continue;
					for (genotype.Genotype gm : genomeOrder) {
						genotype.Genotype gm1 = genotype.Genotype.getGenotype(strArr[0]),
								gm2 = genotype.Genotype.getGenotype(strArr[1]);
						for (int i = 2; i < genomeOrder.length; i++) {
							float value = Float.parseFloat(strArr[i]);
							if (value == 0)
								continue;

							PosterityParentsPair pair = new PosterityParentsPair(
									gm1, gm2);
							if (!posterityTable.keySet().contains(pair)) {
								posterityTable.put(new PosterityParentsPair(
										gm1, gm2),
										new ArrayList<PosterityResultPair>());
							}
							posterityTable.get(pair).add(
									new PosterityResultPair(gm, value));
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private void OrderFill(genotype.Genotype[] arr, String str, int startFrom) {
			String[] strArr = str.split(";");
			int size = Math.min(strArr.length, arr.length);
			try {
				for (int i = startFrom; i < size; i++) {
					arr[i - startFrom] = genotype.Genotype.getGenotype(strArr[i]);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
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
					if (content instanceof ViabilityPair) {
						ViabilityPair pair = (ViabilityPair) content;
						Float value = viabilityTable.get(pair);
						if (value == null)
							value = 0f;
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