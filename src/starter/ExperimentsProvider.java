package starter;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import messaging.Messaging;

public class ExperimentsProvider extends Behaviour implements Messaging {
	
	private static final long serialVersionUID = 1L;
	
	private ContainerController controller;
	private SystemStarter starter;
	
	ExperimentsProvider(ContainerController controller){
		this.controller = controller;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		starter = (SystemStarter)myAgent;
		if (controller == null)/*#may be temporary*/
			System.out.println("FIIIIIRRREEEEEEe");
	}

	@Override
	public void action() {
		starter.remainingExperiments--;
		sendToContainerNewExperiment();
		waitToResponse();
	}

	@Override
	public boolean done() {
		return starter.remainingExperiments <= 0;
	}
	
	private void sendToContainerNewExperiment(){
		try {
			AgentController agent
				= controller.createNewAgent(
								getExperimentName(starter.remainingExperiments), 
								"experiment.Experiment", 
								new Object[]{
									starter.dataFiller.getExperimentDistribution(),
									starter.dataFiller.getScenario(),
									starter.dataFiller.getNumberOfModelingYears(),
									starter.remainingExperiments,
									starter.statisticAID,
									starter.getAID()});
			agent.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void waitToResponse(){
		starter.blockingReceive(MessageTemplate.MatchContent(I_FINISHED));
	}
	
	private String getExperimentName(int i){
		return "Experiment_" + i;
	}
}
