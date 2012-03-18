package experiment;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Vector;

public class ExperimentBehaviour extends Behaviour /*!!! may be CyrclicBehaviour should be here*/ {

	private static final long serialVersionUID = 1L;
	
	// TODO
	
	public ExperimentBehaviour(Scenario scenario) {
		// TODO Auto-generated constructor stub
		((Experiment)myAgent).scenario = scenario;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// method for reading scenario commands
	private ACLMessage[] convertCommandsToACLMessages(Vector<ExperimentCommand> commands){
		// TODO
		return null;
	}

}
