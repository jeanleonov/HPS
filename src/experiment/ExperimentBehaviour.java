package experiment;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
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
		try {
			scenarioCommandsProcessing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			// yearCursor (in Scenario) move HERE!!!
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
	private ACLMessage[] convertCommandsToACLMessages(Vector<ExperimentCommand> commands) throws IOException{
		// TODO
		countOfMessages=0;
		int i=0;
		ACLMessage[] messages = new ACLMessage[commands.size()];
		for (ExperimentCommand command : commands){
			countOfMessages += command.zonesNumbers.length;
			messages[i] = new ACLMessage();
			for (int j=0; j<command.zonesNumbers.length; j++)
				messages[i].addReceiver(experiment.getZoneAID(command.zonesNumbers[i]));
			messages[i].setContentObject(command.command);
			// may be should be append...
		}
		return null;
	}

	private void scenarioCommandsProcessing() throws IOException{
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
