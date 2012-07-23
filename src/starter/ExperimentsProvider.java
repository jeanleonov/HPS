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
	long timeOfStart;
	boolean done;
	int numberOfRuningExperiments;
	
	@Override
	public void onStart() {
		super.onStart();
		starter = (SystemStarter)myAgent;
		numberOfRuningExperiments = 0;
		startExperiments();
		done = false;
		timeOfStart = System.currentTimeMillis();
	}

	@Override
	public void action() {
		ACLMessage response = starter.blockingReceive(MessageTemplate.MatchLanguage(I_FINISHED));
		numberOfRuningExperiments--;
		int nodeNumber = Integer.parseInt(response.getContent());
		if (starter.remainingExperiments > 0)
			sendToContainerNewExperiment(nodeNumber);
		else
			done = true;
	}

	@Override
	public boolean done() {
		return done;
	}
	
	@Override
	public int onEnd() {
		
		long executingTime = System.currentTimeMillis()-timeOfStart,
			 hour = executingTime/1000/60/60,
			 min = executingTime/1000/60 - hour*60,
			 sec = executingTime/1000 - min*60 - hour*3600,
			 msec = executingTime - sec*1000 - min*60000 - hour*3600000; 
		System.out.printf("--------------------------------\n"+
						  "Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec);
		return super.onEnd();
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
			numberOfRuningExperiments++;
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private String getExperimentName(int i){
		return "Experiment_" + i;
	}
}
