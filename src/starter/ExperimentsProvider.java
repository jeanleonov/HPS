package starter;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import messaging.Messaging;

import org.apache.log4j.Logger;

import statistic.visualisation.VisualisationFrame;

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
		startExperiment();
		done = false;
		timeOfStart = System.currentTimeMillis();
	}

	@Override
	public void action() {
		starter.blockingReceive(MessageTemplate.MatchLanguage(I_FINISHED));
		numberOfRuningExperiments--;
		if (starter.remainingExperiments > 0)
			startExperiment();
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
		Logger.getLogger("runningTimeLogger").info(String.format("Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec) + "  With args: " + MainClass.getStartArgs());
		stopStatisticDispatcher();
		if (starter.shouldDisplayDiagram)
			new VisualisationFrame(starter.curStatisticFileURL, starter.shouldDisplayDetailedDiagram, starter.shouldDisplayImmatures);
		starter.doDelete();
		return super.onEnd();
	}
		
	private void stopStatisticDispatcher() {		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);	
		message.addReceiver(starter.statisticAID);
		myAgent.send(message);
	}

	private void startExperiment(){
		try {
			AgentController agent
				= starter.container.createNewAgent(
								getExperimentName(starter.remainingExperiments), 
								"experiment.Experiment", 
								new Object[]{
									starter.dataFiller.getExperimentDistribution(),
									starter.dataFiller.getScenario(),
									starter.numberOfModelingYears,
									starter.curExperiment,
									starter.statisticAID,
									starter.getAID()});
			agent.start();
			starter.remainingExperiments--;
			starter.curExperiment++;
			numberOfRuningExperiments++;
		} catch (StaleProxyException e) {
			Shared.problemsLogger.error(e.getMessage());
		}
	}
	
	private String getExperimentName(int i){
		return "Experiment_" + i;
	}
}
