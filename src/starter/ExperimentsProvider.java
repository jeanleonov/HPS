package starter;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import messaging.Messaging;

public class ExperimentsProvider extends Behaviour implements Messaging {
	
	private static final long serialVersionUID = 1L;
	
	private SystemStarter starter;
	
	@Override
	public void onStart() {
		super.onStart();
		starter = (SystemStarter)myAgent;
		startExperiments();
	}

	@Override
	public void action() {
		ACLMessage response = starter.blockingReceive(MessageTemplate.MatchLanguage(I_FINISHED));
		int nodeNumber = Integer.parseInt(response.getContent());
		sendToContainerNewExperiment(nodeNumber);
	}

	@Override
	public boolean done() {
		return starter.remainingExperiments <= 0;
	}
	
	private void startExperiments(){
		for (int nodeNumber=0; nodeNumber<starter.containerControllers.size(); nodeNumber++)
			sendToContainerNewExperiment(nodeNumber);
	}
	
	private void sendToContainerNewExperiment(int nodeNumber){
		try {
			AgentController agent
				= starter.containerControllers.get(nodeNumber).createNewAgent(
								getExperimentName(starter.remainingExperiments), 
								"experiment.Experiment", 
								new Object[]{
									starter.dataFiller.getExperimentDistribution(),
									starter.dataFiller.getScenario(),
									starter.numberOfModelingYears,
									starter.multiplier,
									starter.curExperiment,
									starter.statisticAID,
									starter.getAID(),
									nodeNumber});
			agent.start();
			starter.remainingExperiments--;
			starter.curExperiment++;
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private String getExperimentName(int i){
		return "Experiment_" + i;
	}
}
