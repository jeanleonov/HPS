package experiment;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Vector;

public class ExperimentBehaviour extends Behaviour /*!!! may be CyrclicBehaviour should be here*/ {

	private static final long serialVersionUID = 1L;
	
	Experiment experiment;
	private int countOfMessages;		// shit-code	* see another shit-code
	
	// TODO
	
	public ExperimentBehaviour(){
		experiment = (Experiment)myAgent;
		experiment.scenario.start();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		scenarioCommandsProcessing();			// yearCursor (in Scenario) move HERE!!!
		dieProcessing();
		moveProcessing();
		lastPhaseProcessing();
	}

	@Override
	public boolean done() {
		if (experiment.scenario.getYearCursor() < experiment.numberOfModelingYears)
			return true;
		return false;
	}
	
	// method for reading scenario commands
	private ACLMessage[] convertCommandsToACLMessages(Vector<ExperimentCommand> commands){
		// TODO
		countOfMessages=0;
		// *shit-variable += command.zonesNumbers			// shit-code
		return null;
	}

	private void scenarioCommandsProcessing(){
		ACLMessage[] commands = convertCommandsToACLMessages(
									experiment.scenario.getCommandsForNextYear());			// yearCursor (in Scenario) move HERE!!!
		for (ACLMessage command : commands)				// send commands
			experiment.send(command);
		for (int i=0; i<countOfMessages; i++)			// waiting for reports 
			myAgent.blockingReceive();
	}

	private void dieProcessing(){
		// TODO
	}

	private void moveProcessing(){
		// TODO
	}

	private void lastPhaseProcessing(){
		// TODO
	}
}
